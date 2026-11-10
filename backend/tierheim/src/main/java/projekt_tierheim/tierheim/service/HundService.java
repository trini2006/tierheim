package projekt_tierheim.tierheim.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.SperrHundDTO;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
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

    public List<Hund> ermittleVeruegbareHunde(Mitglied mitglied, LocalDate datum, LocalTime von, LocalTime bis) {
        reservierungService.pruefeDeadline(datum);
        reservierungService.pruefeZeitraum(von, bis);

        List<Hund> alleHunde = hundRepository.findAll();
        List<Hund> verfuegbareHunde = new ArrayList<>();

        for (Hund h : alleHunde) {
            if (!reservierungService.liegtInGassiGehZeit(datum, von, bis)) continue;
            if (h.istGesperrtAm(datum)) continue;
            if (!reservierungService.istVerfuegbar(h, datum, von, bis)) continue;
            if (!reservierungService.erfuelltDauer(von, bis, h)) continue;
            if (!reservierungService.erfuelltErfahrung(h, mitglied)) continue;
            verfuegbareHunde.add(h);
        }
        return verfuegbareHunde;
    }

    // reine Berechnung, wirft nichts
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
        if(sperrHundDTO.gesperrtVon().isBefore(sperrHundDTO.gesperrtBis())) {
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
}