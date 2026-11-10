package projekt_tierheim.tierheim.service;

import jakarta.transaction.Transactional;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;
import projekt_tierheim.tierheim.db.Hund.Groesse;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.Strecke;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HundServiceTest {

    @Autowired
    HundRepository hundRepository;
    @Autowired
    ReservierungRepository reservierungRepository;
    @Autowired
    ReservierungService reservierungService;
    @Autowired
    MitgliedRepository mitgliedRepository;
    @Autowired
    GassiGehZeitenRepository gassiGehZeitenRepository;

    HundService hundService;

    @BeforeEach
    void setUp() {

        hundService = new HundService(
                hundRepository,
                reservierungRepository,
                reservierungService,
                gassiGehZeitenRepository
        );
    }

    @Test
    void ermittleAlleHunde() {
        List<Hund> alle = hundRepository.findAll();
        System.out.println(alle.size());
    }

    LocalDate datum = LocalDate.of(2026, 11, 2);
    LocalTime von = LocalTime.of(10, 30);
    LocalTime bis = LocalTime.of(12, 30);

    @Test
    void liegtInGassiGehZeit() {
        boolean res = reservierungService.liegtInGassiGehZeit(datum, von , bis);
        System.out.println(res);
    }

    @Test
    void istGesperrtAm() {
        Hund hund = hundRepository.findHundById(2);
        boolean res = hund.istGesperrtAm(datum);
        System.out.println(res);
    }

    @Transactional
    @Test
    void istVerfuegbar() {
        Hund hund = hundRepository.findHundById(2);
        boolean res = reservierungService.istVerfuegbar(hund, datum, von , bis);
        System.out.println(res);
    }

    @Transactional
    @Test
    void erfuelltDauer() {
        Hund hund = hundRepository.findHundById(2);
        boolean res = reservierungService.erfuelltDauer(von, bis, hund);
        System.out.println(res);
    }

    @Transactional
    @Test
    void erfuelltErfahrung() {
        Hund hund = hundRepository.findHundById(2);
        Mitglied mitglied = mitgliedRepository.findMitgliedById(1);
        boolean res = reservierungService.erfuelltErfahrung(hund, mitglied);
        System.out.println(res);
    }

    @Test
    void ermittleVeruegbareHunde() {
        Mitglied mitglied = mitgliedRepository.findMitgliedById(1);

        LocalDate datum = LocalDate.of(2026, 11, 2);
        LocalTime von = LocalTime.of(10, 30);
        LocalTime bis = LocalTime.of(12, 30);

        List<Hund> verfuegbareHunde =
                hundService.findeFreieHunde(
                        mitglied,
                        datum,
                        von,
                        bis
                );

        assertNotNull(verfuegbareHunde);

        System.out.println("Verfügbare Hunde: "
                + verfuegbareHunde.size());

        verfuegbareHunde.forEach(h ->
                System.out.println(h.getName()));
    }

    record SlotBelegung(LocalTime von, LocalTime bis, List<Integer> freieHunde) {}
    @Test
    public void berechneSlotBelegung() {
        Mitglied mitglied = mitgliedRepository.findMitgliedById(1);
        List<GassiGehZeiten> heuteFenster = gassiGehZeitenRepository.findGassiGehZeitenByTag(Tage.von(datum.getDayOfWeek()));
        heuteFenster = heuteFenster.stream()
                .sorted(Comparator.comparing(GassiGehZeiten::getVon))
                .toList();

        List<SlotBelegung> alleSlots = new ArrayList<SlotBelegung>();
        for (GassiGehZeiten fenster : heuteFenster) {
            System.out.println("Von :" + fenster.getVon() + " " + "Bis: " + fenster.getBis());

            for(LocalTime von = fenster.getVon();
                !von.plusMinutes(30).isAfter(fenster.getBis());
                von = von.plusMinutes(30)) {
                LocalTime bis = von.plusMinutes(30);

                List<Integer> freieHunde = hundService.findeFreieHunde(mitglied, datum, von, bis).stream()
                        .map(Hund::getId).toList();
                alleSlots.add(new SlotBelegung(von, bis, freieHunde));
            }

            for(SlotBelegung s : alleSlots) {
                System.out.println("Von :" + s.von + "Bis: " + s.bis + "Anzahl Hunde: " + s.freieHunde.size());
            }
        }
    }
}