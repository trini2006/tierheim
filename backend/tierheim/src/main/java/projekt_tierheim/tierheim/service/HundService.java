package projekt_tierheim.tierheim.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.SperrHundDTO;
import projekt_tierheim.tierheim.db.Hund.TeilFenster;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;
import projekt_tierheim.tierheim.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HundService {
    private final HundRepository hundRepository;
    private final ReservierungRepository reservierungRepository;
    private final ReservierungService reservierungService;
    private final GassiGehZeitenRepository gassiGehZeitenRepository;

    public HundService(HundRepository hundRepository, ReservierungRepository reservierungRepository, ReservierungService reservierungService, GassiGehZeitenRepository gassiGehZeitenRepository) {
        this.hundRepository = hundRepository;
        this.reservierungRepository = reservierungRepository;
        this.reservierungService = reservierungService;
        this.gassiGehZeitenRepository = gassiGehZeitenRepository;
    }

    public List<Hund> findeFreieHunde(Mitglied mitglied, LocalDate datum, LocalTime von, LocalTime bis) {
        if (!reservierungService.liegtInGassiGehZeit(datum, von, bis)) return List.of();

        return hundRepository.findAllByTierheimId(1).stream()
                .filter(h -> !h.istGesperrtAm(datum))
                .filter(h -> reservierungService.istVerfuegbar(h, datum, von, bis))
                .filter(h -> reservierungService.erfuelltDauer(von, bis, h))
                .filter(h -> reservierungService.erfuelltErfahrung(h, mitglied))
                .toList();
    }

    public Hund waehleVorschlag(List<Hund> verfuegbareHunde) {
        if(verfuegbareHunde.isEmpty()) {
            return null;
        }

        Map<Hund, Integer> anzahlProHund = new HashMap<>();
        for(Hund h : verfuegbareHunde) {
            anzahlProHund.put(h, reservierungRepository.countByHundIdAndStatusNot(h.getId(), Reservierungsstatus.STORNIERT));
        }

        int minimum = Collections.min(anzahlProHund.values());
        List<Hund> kandidaten = verfuegbareHunde.stream()
                .filter(h -> anzahlProHund.get(h) == minimum)
                .toList();

        return kandidaten.get(new Random().nextInt(kandidaten.size()));
    }

    @Transactional
    public Hund sperreHund(int hundId, SperrHundDTO sperrHundDTO) {
        Hund hund = hundRepository.findHundById(hundId);
        if(hund == null) {
            throw new NotFoundException("Hund mit der Id " + hundId + " nicht gefunden");
        }
        if(sperrHundDTO.gesperrtVon().isAfter(sperrHundDTO.gesperrtBis())) {
            throw new IllegalArgumentException("Inkorrekter Zeitraum");
        }

        hund.setIstGesperrt(true);
        hund.setGesperrtVon(sperrHundDTO.gesperrtVon());
        hund.setGesperrtBis(sperrHundDTO.gesperrtBis());
        hund.setSperrGrund(sperrHundDTO.sperrGrund());

        List<Reservierung> betroffene = reservierungRepository.findByHundIdAndStatusAndDatumBetween(
                hundId, Reservierungsstatus.AKTIV, sperrHundDTO.gesperrtVon(), sperrHundDTO.gesperrtBis())
                .stream()
                .filter(r -> LocalDateTime.of(r.getDatum(), r.getZeitAb()).isAfter(LocalDateTime.now()))
                .toList();

        String grund = "Hund gesperrt";
        if(sperrHundDTO.sperrGrund() != null && !sperrHundDTO.sperrGrund().isBlank()) {
            grund += ": " + sperrHundDTO.sperrGrund();
        }
        for(Reservierung r : betroffene) {
            r.setStatus(Reservierungsstatus.STORNIERT);
            r.setStornierungsgrund(grund);
            r.setStorniertAm(LocalDateTime.now());
        }
        reservierungRepository.saveAll(betroffene);
        return hundRepository.saveAndFlush(hund);
    }

    public record SlotBelegung(LocalTime von, LocalTime bis, List<Integer> freieHunde) {}
    private static final int SLOT_MINUTEN = 30;
    public List<SlotBelegung> berechneSlotBelegung(LocalDate datum, Mitglied mitglied) {
        List<GassiGehZeiten> heuteFenster = gassiGehZeitenRepository.findGassiGehZeitenByTag(Tage.von(datum.getDayOfWeek()));
        heuteFenster = heuteFenster.stream()
                .sorted(Comparator.comparing(GassiGehZeiten::getVon))
                .toList();

        List<SlotBelegung> alleSlots = new ArrayList<SlotBelegung>();
        for (GassiGehZeiten fenster : heuteFenster) {
            for(LocalTime von = fenster.getVon();
                !von.plusMinutes(SLOT_MINUTEN).isAfter(fenster.getBis());
                von = von.plusMinutes(SLOT_MINUTEN)) {
                LocalTime bis = von.plusMinutes(SLOT_MINUTEN);

                List<Integer> freieHunde = findeFreieHunde(mitglied, datum, von, bis).stream()
                        .map(Hund::getId).toList();
                alleSlots.add(new SlotBelegung(von, bis, freieHunde));
            }
        }
        return alleSlots;
    }


    // Logik für das Anzeigen der Hunde, die nur teilweise im gewählten Zeitraum Zeit haben
    public record Zeitraum(LocalTime von, LocalTime bis) {}

    public List<TeilFenster> findeTeilweiseFreieHunde(Mitglied mitglied, LocalDate datum, LocalTime von, LocalTime bis) {
        // vorhandene Slot-Berechnung wiederverwenden, nur den gewählten Bereich behalten
        List<SlotBelegung> slots = berechneSlotBelegung(datum, mitglied).stream()
                .filter(s -> !s.von().isBefore(von) && !s.bis().isAfter(bis))
                .sorted(Comparator.comparing(SlotBelegung::von))
                .toList();

        Map<Integer, Hund> hunde = hundRepository.findAllByTierheimId(1).stream()   // ToDO: Tierheim des Mitglieds
                .collect(Collectors.toMap(Hund::getId, h -> h));

        // alle Hunde, die in mindestens einem Slot frei sind
        Set<Integer> kandidaten = slots.stream()
                .flatMap(s -> s.freieHunde().stream())
                .collect(Collectors.toSet());

        Map<Zeitraum, List<Hund>> gruppen = new HashMap<>();
        for (Integer id : kandidaten) {
            Hund h = hunde.get(id);
            LocalTime start = null;
            LocalTime ende = null;

            for (SlotBelegung s : slots) {
                boolean frei = s.freieHunde().contains(id);
                boolean schliesstAn = ende != null && ende.equals(s.von());

                if (frei && start != null && schliesstAn) {
                    ende = s.bis();                                         // Abschnitt verlängern
                } else {
                    if (start != null) sammleAbschnitt(gruppen, h, start, ende, von, bis);   // Abschnitt abschließen
                    start = frei ? s.von() : null;
                    ende = frei ? s.bis() : null;
                }
            }
            if (start != null) sammleAbschnitt(gruppen, h, start, ende, von, bis);
        }

        return gruppen.entrySet().stream()
                .map(e -> new TeilFenster(e.getKey().von(), e.getKey().bis(), e.getValue()))
                .sorted(Comparator.comparing(TeilFenster::von).thenComparing(TeilFenster::bis))
                .toList();
    }

    private void sammleAbschnitt(Map<Zeitraum, List<Hund>> gruppen, Hund h,
                                 LocalTime start, LocalTime ende, LocalTime von, LocalTime bis) {
        if (start.equals(von) && ende.equals(bis)) return;                     // ganz frei -> steht in der Hauptliste
        if (!reservierungService.erfuelltDauer(start, ende, h)) return;        // kürzer als die Mindestdauer
        gruppen.computeIfAbsent(new Zeitraum(start, ende), k -> new ArrayList<>()).add(h);
    }
}