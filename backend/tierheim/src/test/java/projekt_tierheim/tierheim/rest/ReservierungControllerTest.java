package projekt_tierheim.tierheim.rest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Hund.Groesse;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.Strecke;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReservierungControllerTest {

    // MITGLIED
    public static int TEST_MITGLIEDSNUMMER1 = 12345678;
    public static String TEST_PASSWORT1 = "password1";
    public static boolean TEST_ERFAHRUNG1 = false;

    // HUND
    public static String TEST_NAME1 = "Wambo";
    public static boolean TEST_GESCHLECHT1 = false;
    public static int TEST_ALTER1 = 2;
    public static String TEST_RASSE1 = "Husky-Chow Chow Mix";
    public static int TEST_GEWICHT1 = 25;
    public static Groesse TEST_GROESSE1 = Groesse.MITTEL;
    public static Strecke TEST_STRECKE1 = Strecke.LANG;
    public static Admin TEST_ERSTELLTVON = new Admin(1, 1234567890, "geheim123");

    // RESERVIERUNG
    public static int TEST_ID1 = 1;
    public static Mitglied TEST_MITGLIED1 = new Mitglied(TEST_ID1, TEST_MITGLIEDSNUMMER1, TEST_PASSWORT1, TEST_ERFAHRUNG1);
    public static Hund TEST_HUND1 = new Hund(TEST_ID1, TEST_NAME1, TEST_GESCHLECHT1, TEST_ALTER1, TEST_RASSE1, TEST_GROESSE1, TEST_GEWICHT1, TEST_ERFAHRUNG1, TEST_STRECKE1, TEST_ERSTELLTVON);
    public static LocalDate TEST_DATUM1 = LocalDate.of(2026, 9, 7);
    public static LocalTime TEST_ZEITAB1 = LocalTime.of(12, 0);
    public static LocalTime TEST_ZEITBIS1 = LocalTime.of(14, 0);

    public static Reservierung getTestReservierung() {
        return new Reservierung(TEST_ID1, TEST_MITGLIED1, TEST_HUND1, TEST_DATUM1, TEST_ZEITAB1, TEST_ZEITBIS1);
    }

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ReservierungRepository reservierungRepository;

    @Test
    public void getAlleReservierungen() throws Exception {
        // Alle Übergabeparameter müssen Matcher (any) sein
        Mockito.when(reservierungRepository.findGefiltert(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(TEST_ID1),
                        Mockito.eq(TEST_ID1)))
                .thenReturn(List.of(getTestReservierung(), getTestReservierung()));
        mockMvc.perform(MockMvcRequestBuilders.get("/reservierung/all?mitgliedId=" + TEST_ID1 + "&hundId=" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].idReservierung").value(TEST_ID1),
                        jsonPath("$[0].mitglied").value(TEST_MITGLIED1),
                        jsonPath("$[0].datum").value(TEST_DATUM1.toString()),
                        jsonPath("$[1].idReservierung").value(TEST_ID1)
                );
    }

    // ToDO Get alle Reservierungen von Mitglied (in einem bestimmten Zeitraum)
    @Test
    public void getAlleReservierungenMitglied() throws Exception {

    }

    // ToDO Get alle Reservierungen von Hund (in einem bestimmten Zeitraum)
    @Test
    public void getAlleReservierungenHund() throws Exception {

    }

    // ToDO Get alle Reservierungen in einem bestimmten Zeitraum
    @Test
    public void getAlleReservierungenZeitraum() throws Exception {

    }

    // ToDO Post Erstelle eine neue Reservierung von einem bestimmten Hund und Mitglied
    @Test
    public void newReservierung() throws Exception {

    }

    // ToDO Delete Storniere eine/ alle Reservierung(en)
    @Test
    public void deleteReservierung() throws Exception {

    }

}