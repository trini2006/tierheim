package projekt_tierheim.tierheim.rest;

import org.json.JSONObject;
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
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.Strecke;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

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

    // TIERHEIM
    public static String TEST_NAME = "Tierschutzverein Weiden u. U. e.V.";
    public static String TEST_STRASSE1 = "Schustermooslohe";
    public static String TEST_HAUSNUMMER1 = "96";
    public static String TEST_PLZ1 = "92637";
    public static String TEST_ORT1 = "Weiden i. d. Oberpfalz";
    public static String TEST_TELEFONNR1 = "0961 25780";

    // RESERVIERUNG
    public static int TEST_ID1 = 1;
    public static Tierheim TEST_TIERHEIM1 = new Tierheim(TEST_ID1, TEST_NAME, TEST_STRASSE1, TEST_HAUSNUMMER1, TEST_PLZ1, TEST_ORT1, TEST_TELEFONNR1);
    public static Mitglied TEST_MITGLIED1 = new Mitglied(TEST_ID1, TEST_MITGLIEDSNUMMER1, TEST_PASSWORT1, TEST_ERFAHRUNG1);
    public static Hund TEST_HUND1 = new Hund(TEST_ID1, TEST_NAME1, TEST_GESCHLECHT1, TEST_ALTER1, TEST_RASSE1, TEST_GROESSE1, TEST_GEWICHT1, TEST_ERFAHRUNG1, TEST_STRECKE1, TEST_ERSTELLTVON, TEST_TIERHEIM1);
    public static LocalDate TEST_DATUM1 = LocalDate.of(2026, 9, 7);
    public static LocalTime TEST_ZEITAB1 = LocalTime.of(12, 0);
    public static LocalTime TEST_ZEITBIS1 = LocalTime.of(14, 0);

    public static Reservierungsstatus TEST_STATUS1 = Reservierungsstatus.AKTIV;

    // STORNIERUNG
    // noch nicht gebraucht

    public static Reservierung getTestReservierung() {
        return new Reservierung(TEST_ID1, TEST_MITGLIED1, TEST_HUND1, TEST_DATUM1, TEST_ZEITAB1, TEST_ZEITBIS1);
    }

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    ReservierungRepository reservierungRepository;
    @MockitoBean
    HundRepository hundRepository;
    @MockitoBean
    MitgliedRepository mitgliedRepository;

    @Test
    public void getAlleReservierungen() throws Exception {
        // Alle Übergabeparameter müssen Matcher (any) sein
        Mockito.when(reservierungRepository.findGefiltert(
                        Mockito.any(LocalDate.class),
                        Mockito.any(LocalDate.class),
                        Mockito.eq(TEST_ID1),
                        Mockito.eq(TEST_ID1),
                        Mockito.nullable(Reservierungsstatus.class)))
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

    @Test
    public void newReservierung() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(TEST_HUND1);
        Mockito.when(mitgliedRepository.findMitgliedById(TEST_ID1)).thenReturn(TEST_MITGLIED1);
        Mockito.when(reservierungRepository.saveAndFlush(Mockito.any(Reservierung.class))).thenReturn(getTestReservierung());

        JSONObject reservierung = new JSONObject();
        reservierung.put("mitgliedId",  TEST_ID1);
        reservierung.put("hundId", TEST_ID1);
        reservierung.put("datum", TEST_DATUM1);
        reservierung.put("zeitAb", TEST_ZEITAB1);
        reservierung.put("zeitBis", TEST_ZEITBIS1);

        mockMvc.perform(MockMvcRequestBuilders.post("/reservierung/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(reservierung.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("mitglied.mitgliedsnummer").value(TEST_MITGLIED1.getMitgliedsnummer()),
                        jsonPath("idReservierung").value(TEST_ID1),                        jsonPath("status").value(TEST_STATUS1.toString())
                );
    }

    @Test
    public void storniereReservierung() throws Exception {
        Reservierung storniertReservierung = getTestReservierung();
        storniertReservierung.setStatus(Reservierungsstatus.STORNIERT);

        Mockito.when(reservierungRepository.findReservierungById(TEST_ID1)).thenReturn(getTestReservierung());
        Mockito.when(reservierungRepository.saveAndFlush(Mockito.any(Reservierung.class))).thenReturn(storniertReservierung);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reservierung/" + TEST_ID1)
                        .param("grund", "Testgrund")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("status").value(Reservierungsstatus.STORNIERT.toString())
                );
    }

    @Test
    public void storniereAlleReservierungen()  throws Exception {
        List<Reservierung> stornierteReservierungen = List.of(getTestReservierung(), getTestReservierung());
        for(Reservierung r : stornierteReservierungen) {
            r.setStatus(Reservierungsstatus.STORNIERT);
        }

        Mockito.when(reservierungRepository.findReservierungByHundAndStatus(TEST_ID1, Reservierungsstatus.AKTIV)).thenReturn(List.of(getTestReservierung(), getTestReservierung()));
        Mockito.when(reservierungRepository.saveAllAndFlush(Mockito.any(List.class))).thenReturn(stornierteReservierungen);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reservierung/hund/" + TEST_ID1 + "/alle")
                        .param("grund", "Testgrund")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].status").value(Reservierungsstatus.STORNIERT.toString()),
                        jsonPath("$[1].status").value(Reservierungsstatus.STORNIERT.toString())
                );
    }
}