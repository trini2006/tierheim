package projekt_tierheim.tierheim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungDTO;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservierungService {
    private final ReservierungRepository reservierungRepository;
    private final HundRepository hundRepository;
    private final MitgliedRepository mitgliedRepository;
    private final TierheimRepository tierheimRepository;

    @Autowired
    public ReservierungService(ReservierungRepository reservierungRepository, HundRepository hundRepository, MitgliedRepository mitgliedRepository, TierheimRepository tierheimRepository) {
        this.reservierungRepository = reservierungRepository;
        this.hundRepository = hundRepository;
        this.mitgliedRepository = mitgliedRepository;
        this.tierheimRepository = tierheimRepository;
    }

    public Reservierung erstelleReservierung(ReservierungDTO reservierungDTO) {
        Mitglied mitglied = mitgliedRepository.findMitgliedById(reservierungDTO.mitgliedId());
        Hund hund = hundRepository.findHundById(reservierungDTO.hundId());

        if(mitglied == null || hund == null) {
            throw new IllegalArgumentException("Hund oder Mitglied nicht gefunden");
        }
        if(hund.isIstGesperrt()) {
            throw new IllegalArgumentException("Hund ist aktuell gesperrt");
        }

        pruefeVerfuegbarkeit(hund, reservierungDTO.datum(), reservierungDTO.zeitAb(),  reservierungDTO.zeitBis());
        pruefeDauer(reservierungDTO.zeitAb(), reservierungDTO.zeitBis(), hund);
        pruefeErfahrung(hund, mitglied);

        Reservierung reservierung = new Reservierung();
        reservierung.setMitglied(mitglied);
        reservierung.setHund(hund);
        reservierung.setDatum(reservierungDTO.datum());
        reservierung.setZeitAb(reservierungDTO.zeitAb());
        reservierung.setZeitBis(reservierungDTO.zeitBis());

        return reservierungRepository.saveAndFlush(reservierung);
    }

    // Holt bestehende Reservierungen des Hundes an diesem Tag und prüft auf zeitliche Überlappung und bestehende Ruhezeiten
    private void pruefeVerfuegbarkeit(Hund hund, LocalDate datum, LocalTime zeitAb, LocalTime zeitBis) {
        List<Reservierung> reservierungenAmTag = reservierungRepository
                .findByHundIdAndDatumAndStatus(
                        hund.getId(),
                        datum,
                        Reservierungsstatus.AKTIV
                );

        int ruhezeitMinuten = hund.getTierheim().getRuhezeit();

        for(Reservierung bestehende : reservierungenAmTag) {
            boolean ueberschneidet = bestehende.getZeitAb().isBefore(zeitBis) && bestehende.getZeitBis().isAfter(zeitAb);

            if(ueberschneidet) {
                throw new IllegalArgumentException("Hund ist in diesem Zeitraum bereits reserviert");
            }

            // Bestehende Reservierung liegt vor dem neuen Termin
            if(!bestehende.getZeitBis().isAfter(zeitAb)) {
                long abstand = ChronoUnit.MINUTES.between(bestehende.getZeitBis(), zeitAb);
                if(abstand < ruhezeitMinuten) {
                    throw new IllegalArgumentException("Hund braucht noch Ruhe nach dem letzten Spaziergang");
                }
            }

            // Bestehende Reservierung liegt nach dem neuen Termin
            if(!bestehende.getZeitAb().isBefore(zeitBis)) {
                long abstand = ChronoUnit.MINUTES.between(zeitBis, bestehende.getZeitAb());
                if(abstand < ruhezeitMinuten) {
                    throw new IllegalArgumentException("Hund braucht noch Ruhe vor dem nächsten Spaziergang");
                }
            }
        }
    }

    // Vergleicht mit tierheim.getMinGassi() / getMaxGassi()
    private void pruefeDauer(LocalTime zeitAb, LocalTime zeitBis, Hund hund) {
        Tierheim tierheim = hund.getTierheim(); // aktuell nur Tierheim Weiden

        long dauer = ChronoUnit.MINUTES.between(zeitAb, zeitBis);
        if( dauer > tierheim.getMaxGassi()) {
            throw new IllegalArgumentException("Die Dauer dieses Spaziergangs ist zu lang");
        }
        else if( dauer < tierheim.getMinGassi()) {
            throw new IllegalArgumentException("Die Dauer dieses Spaziergangs ist zu kurz");
        }
    }

    // Prüfung des Erfahrungslevels
    private void pruefeErfahrung(Hund hund, Mitglied mitglied) {
        if(hund.getErfahrung() != mitglied.getErfahrung()) {
            throw new IllegalArgumentException("Dieser Hund erfordert den Besuch eines Lehrgangs für schwierige Hunde");
        }
    }

    public boolean istVerfuegbar(Hund hund, LocalDate datum, LocalTime zeitAb, LocalTime zeitBis) {
        List<Reservierung> reservierungenAmTag = reservierungRepository
                .findByHundIdAndDatumAndStatus(
                        hund.getId(),
                        datum,
                        Reservierungsstatus.AKTIV
                );

        int ruhezeitMinuten = hund.getTierheim().getRuhezeit();

        for(Reservierung bestehende : reservierungenAmTag) {
            boolean ueberschneidet = bestehende.getZeitAb().isBefore(zeitBis) && bestehende.getZeitBis().isAfter(zeitAb);

            if(ueberschneidet) { return false; }

            // Bestehende Reservierung liegt vor dem neuen Termin
            if(!bestehende.getZeitBis().isAfter(zeitAb)) {
                if(ChronoUnit.MINUTES.between(bestehende.getZeitBis(), zeitAb) < ruhezeitMinuten) { return false; }
            }

            // Bestehende Reservierung liegt nach dem neuen Termin
            if(!bestehende.getZeitAb().isBefore(zeitBis)) {
                if(ChronoUnit.MINUTES.between(zeitBis, bestehende.getZeitAb()) < ruhezeitMinuten) { return false; }
            }
        }
        return true;
    }

    public boolean erfuelltDauer(LocalTime zeitAb, LocalTime zeitBis, Hund hund) {
        Tierheim tierheim = hund.getTierheim();
        long dauer = ChronoUnit.MINUTES.between(zeitAb, zeitBis);

        return dauer >= tierheim.getMinGassi() && dauer <= tierheim.getMaxGassi();
    }

    public boolean erfuelltErfahrung(Hund hund, Mitglied mitglied) {
        return !hund.getErfahrung() || mitglied.getErfahrung();
    }
}