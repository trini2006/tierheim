package projekt_tierheim.tierheim.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class HundService {
    private final HundRepository hundRepository;
    private final ReservierungRepository reservierungRepository;
    private final ReservierungService reservierungService;

    public HundService(HundRepository hundRepository, ReservierungRepository reservierungRepository, ReservierungService reservierungService) {
        this.hundRepository = hundRepository;
        this.reservierungRepository = reservierungRepository;
        this.reservierungService = reservierungService;
    }

    public List<Hund> ermittleVeruegbareHunde(Mitglied mitglied, LocalDate datum, LocalTime von, LocalTime bis) {
        List<Hund> alleHunde = hundRepository.findAll();
        List<Hund> verfuegbareHunde = new ArrayList<>();
        for (Hund h : alleHunde) {
            if (h.isIstGesperrt()) continue;
            if (reservierungService.istVerfuegbar(h, datum, von, bis)) continue;
            if (reservierungService.erfuelltDauer(von, bis, h)) continue;
            if (reservierungService.erfuelltErfahrung(h, mitglied)) continue;
            verfuegbareHunde.add(h);
        }
        return verfuegbareHunde;
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
}
