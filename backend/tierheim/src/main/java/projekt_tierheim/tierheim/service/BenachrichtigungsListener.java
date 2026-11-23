package projekt_tierheim.tierheim.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import projekt_tierheim.tierheim.db.Benachrichtigung.Benachrichtigung;
import projekt_tierheim.tierheim.db.Benachrichtigung.BenachrichtigungRepository;
import projekt_tierheim.tierheim.db.Benachrichtigung.BenachrichtigungsTyp;
import projekt_tierheim.tierheim.db.Reservierung.Akteur;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BenachrichtigungsListener {
    private static final Logger log =  LoggerFactory.getLogger(BenachrichtigungsListener.class);
    private static final DateTimeFormatter DATUM = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter ZEIT = DateTimeFormatter.ofPattern("HH:mm");

    private final ReservierungRepository reservierungRepository;
    private final BenachrichtigungRepository benachrichtigungRepository;

    public BenachrichtigungsListener(ReservierungRepository reservierungRepository,
                                     BenachrichtigungRepository benachrichtigungRepository) {
        this.reservierungRepository = reservierungRepository;
        this.benachrichtigungRepository = benachrichtigungRepository;
    }

    // Listener läuft erst, wenn die Stornierung sicher in der DB steht
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onStorniert(ReservierungenStorniertEvent event) {
        //log.info("Storniert durch {}: Reservierungen {} ({})", event.akteur(), event.reservierungsIds(), event.grund());
        try {
            List<Reservierung> reservierungen = reservierungRepository.findAllById(event.reservierungsIds());
            if (event.akteur() == Akteur.MITGLIED) {
                benachrichtigeTierheim(reservierungen);
            } else {
                benachrichtigeMitglieder(reservierungen, event);
            }
        } catch (Exception ex) {
            // Die Stornierung ist bereits gespeichert und darf nicht an einer Nachricht scheitern
            log.error("Benachrichtigung konnte nicht erstellt werden: {}", event, ex);
        }
    }

    private void benachrichtigeTierheim(List<Reservierung> reservierungen) {
        for (Reservierung r : reservierungen) {
            String text = "Mitglied " + r.getMitglied().getMitgliedsnummer()
                    + " hat die Reservierung für " + r.getHund().getName()
                    + " am " + beschreibe(r) + " storniert.";
            benachrichtigungRepository.save(Benachrichtigung.fuerTierheim(
                    r.getHund().getTierheim(), BenachrichtigungsTyp.STORNIERUNG_MITGLIED, text));
        }
    }

    private void benachrichtigeMitglieder(List<Reservierung> reservierungen, ReservierungenStorniertEvent e) {
        boolean vomTierheim = e.akteur() == Akteur.TIERHEIM;
        String wer = vomTierheim ? "vom Tierheim" : "automatisch";
        BenachrichtigungsTyp typ = vomTierheim
                ? BenachrichtigungsTyp.STORNIERUNG_TIERHEIM : BenachrichtigungsTyp.STORNIERUNG_SYSTEM;

        // eine Nachricht pro Mitglied, auch wenn mehrere Termine betroffen sind
        Map<Integer, List<Reservierung>> proMitglied = reservierungen.stream()
                .collect(Collectors.groupingBy(r -> r.getMitglied().getId()));

        for (List<Reservierung> liste : proMitglied.values()) {
            String text;
            if (liste.size() == 1) {
                Reservierung r = liste.get(0);
                text = "Deine Reservierung für " + r.getHund().getName() + " am " + beschreibe(r)
                        + " wurde " + wer + " storniert.";
            } else {
                text = "Deine folgenden Reservierungen wurden " + wer + " storniert: "
                        + liste.stream()
                        .map(r -> r.getHund().getName() + " am " + beschreibe(r))
                        .collect(Collectors.joining("; ")) + ".";
            }
            if (e.grund() != null && !e.grund().isBlank()) {
                text += " Grund: " + e.grund();
            }
            benachrichtigungRepository.save(
                    Benachrichtigung.fuerMitglied(liste.get(0).getMitglied(), typ, text));
        }
    }
    private String beschreibe(Reservierung r) {
        return r.getDatum().format(DATUM) + ", " + r.getZeitAb().format(ZEIT) + "–"
                + r.getZeitBis().format(ZEIT) + " Uhr";
    }

}
