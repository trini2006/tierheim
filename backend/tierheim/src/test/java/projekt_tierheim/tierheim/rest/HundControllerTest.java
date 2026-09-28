package projekt_tierheim.tierheim.rest;

import jakarta.persistence.GenerationType;
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
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HundControllerTest {
    // TIERHEIM
    public static String TEST_NAME = "Tierschutzverein Weiden u. U. e.V.";
    public static String TEST_STRASSE1 = "Schustermooslohe";
    public static String TEST_HAUSNUMMER1 = "96";
    public static String TEST_PLZ1 = "92637";
    public static String TEST_ORT1 = "Weiden i. d. Oberpfalz";
    public static String TEST_TELEFONNR1 = "0961 25780";

    // Wambo Test-Hund 1
    public static int TEST_ID1 = 1;
    public static String TEST_NAME1 = "Wambo";
    public static boolean TEST_GESCHLECHT1 = false;
    public static int TEST_ALTER1 = 2;
    public static String TEST_RASSE1 = "Husky-Chow Chow Mix";
    public static int TEST_GEWICHT1 = 25;
    public static boolean TEST_ERFAHRUNG1 = false;
    public static Groesse TEST_GROESSE1 = Groesse.MITTEL;
    public static Strecke TEST_STRECKE1 = Strecke.LANG;

    public static String TEST_RASSE2 = "Husky";
    public static Groesse TEST_GROESSE2 = Groesse.GROSS;
    public static int TEST_GEWICHT2 = 40;
    public static Strecke TEST_STRECKE2 = Strecke.KURZ;

    // mit Sperrgrund
    public static LocalDate TEST_GESPERRTVON = LocalDate.of(2026, 7, 23);
    public static LocalDate TEST_GESPERRTBIS = LocalDate.of(2026, 7, 30);
    public static boolean TEST_ISTGESPERRT = true;
    public static String TEST_SPERRGRUND = "Probewohnen";

    public static Admin TEST_ERSTELLTVON = new Admin(1, 1234567890, "geheim123");
    public static Tierheim TEST_TIERHEIM1 = new Tierheim(TEST_ID1, TEST_NAME, TEST_STRASSE1, TEST_HAUSNUMMER1, TEST_PLZ1, TEST_ORT1, TEST_TELEFONNR1);

    public static Hund getTestHund() {
        Hund hund = new Hund(TEST_ID1, TEST_NAME1, TEST_GESCHLECHT1, TEST_ALTER1, TEST_RASSE1, TEST_GROESSE1, TEST_GEWICHT1, TEST_ERFAHRUNG1, TEST_STRECKE1, TEST_ERSTELLTVON, TEST_TIERHEIM1);
        hund.setIstGesperrt(false);
        return hund;
    }

    public static Hund updateTestHund() {
        return new Hund(TEST_ID1, TEST_NAME1, TEST_GESCHLECHT1, TEST_ALTER1, TEST_RASSE2, TEST_GROESSE2, TEST_GEWICHT2, TEST_ERFAHRUNG1, TEST_STRECKE2, TEST_ERSTELLTVON, TEST_TIERHEIM1);
    }

    public static Hund getTestSperrHund() {
        Hund hund = getTestHund();
        hund.setIstGesperrt(TEST_ISTGESPERRT); // true
        hund.setGesperrtVon(TEST_GESPERRTVON);
        hund.setGesperrtBis(TEST_GESPERRTBIS);
        hund.setSperrGrund(TEST_SPERRGRUND);
        return hund;
    }

    public static List<Hund> getAlleTestHunde() {
        return List.of(getTestHund(), getTestSperrHund());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private HundRepository hundRepository;
    @MockitoBean
    private LabelRepository labelRepository;

    @Test
    void getHundById() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(getTestHund());
        mockMvc.perform(MockMvcRequestBuilders.get("/hund/" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("geschlecht").value(TEST_GESCHLECHT1),
                        jsonPath("erfahrung").value(TEST_ERFAHRUNG1),
                        jsonPath("erstelltVon").value(TEST_ERSTELLTVON)
                );
    }

    @Test
    void getAlleHunde() throws Exception{
        Mockito.when(hundRepository.findAll()).thenReturn(getAlleTestHunde());
        mockMvc.perform(MockMvcRequestBuilders.get("/hund/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].name").value(TEST_NAME1),
                        jsonPath("$[0].rasse").value(TEST_RASSE1),
                        jsonPath("$[0].gewicht").value(TEST_GEWICHT1),
                        jsonPath("$[1].name").value(TEST_NAME1),
                        jsonPath("$[1].rasse").value(TEST_RASSE1),
                        jsonPath("$[1].gewicht").value(TEST_GEWICHT1)
                );
    }

    @Test
    void getHundeImZeitfenster() throws Exception {

    }

    @Test
    void getHundByName() throws Exception {
        Mockito.when(hundRepository.findHundByNameIgnoreCase(TEST_NAME1)).thenReturn(List.of(getTestHund()));
        mockMvc.perform(MockMvcRequestBuilders.get("/hund/search?name=" + TEST_NAME1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].name").value(TEST_NAME1),
                        jsonPath("$[0].gewicht").value(TEST_GEWICHT1)
                );
    }

    @Test
    void newHund() throws Exception {
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(getTestHund());
        JSONObject hund = new JSONObject();
        hund.put("name", TEST_NAME1);
        hund.put("geschlecht", TEST_GESCHLECHT1);
        hund.put("jahre", TEST_ALTER1);
        hund.put("rasse", TEST_RASSE1);
        hund.put("gewicht", TEST_GEWICHT1);
        hund.put("erfahrung", TEST_ERFAHRUNG1);
        hund.put("groesse", TEST_GROESSE1);
        hund.put("strecke", TEST_STRECKE1);

        mockMvc.perform(MockMvcRequestBuilders.post("/hund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(hund.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("groesse").value(TEST_GROESSE1.name())
                );
    }

    @Test
    void deleteHund() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(getTestHund());
        mockMvc.perform(MockMvcRequestBuilders.delete("/hund/" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(hundRepository, Mockito.times(1))
                .delete(Mockito.any(Hund.class));
    }

    @Test
    void updateHund() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(getTestHund());
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(updateTestHund());

        JSONObject hund = new JSONObject();
        hund.put("name", TEST_NAME1);
        hund.put("geschlecht", TEST_GESCHLECHT1);
        hund.put("jahre", TEST_ALTER1);
        hund.put("rasse", TEST_RASSE2);
        hund.put("gewicht", TEST_GEWICHT2);
        hund.put("erfahrung", TEST_ERFAHRUNG1);
        hund.put("groesse", TEST_GROESSE2);
        hund.put("strecke", TEST_STRECKE2);

        mockMvc.perform(MockMvcRequestBuilders.put("/hund/" + TEST_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(hund.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("gewicht").value(TEST_GEWICHT2),
                        jsonPath("rasse").value(TEST_RASSE2)
                );
    }

    // HUND SPERREN
    @Test
    void sperrgrundHinzufuegen() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(getTestHund());
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(getTestSperrHund());

        JSONObject hund = new JSONObject();
        hund.put("gesperrtVon", TEST_GESPERRTVON);
        hund.put("gesperrtBis", TEST_GESPERRTBIS);
        hund.put("istGesperrt", TEST_ISTGESPERRT);
        hund.put("sperrGrund", TEST_SPERRGRUND);

        mockMvc.perform(MockMvcRequestBuilders.put("/hund/sperren/" + TEST_ID1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(hund.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("gesperrtVon").value(TEST_GESPERRTVON.toString()),
                        jsonPath("sperrGrund").value(TEST_SPERRGRUND)
                );
    }

    @Test
    void sperrgrundEntfernen() throws Exception {
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(getTestSperrHund());
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(getTestHund());

        mockMvc.perform(MockMvcRequestBuilders.put("/hund/entsperren/" + TEST_ID1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("istGesperrt").value(false)
                );
    }

    // LABELS
    @Test
    void addLabel() throws Exception {
        Hund hund = getTestHund();
        Hund updateHund = getTestHund();
        Label label = new Label(1, "Freundlich", false);
        updateHund.addLabel(label);

        Mockito.when(labelRepository.findLabelById(TEST_ID1)).thenReturn(label);
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(hund);
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(updateHund);

        mockMvc.perform(MockMvcRequestBuilders.post("/hund/" + TEST_ID1 + "/label/" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(TEST_ID1),
                        jsonPath("$.labels.length()").value(1),
                        jsonPath("$.labels[0].bezeichnung").value("Freundlich")
                );
    }

    @Test
    void deleteLabelVonHund() throws Exception {
        Hund hund = getTestHund();
        Label label = new Label(1, "Freundlich", false);
        hund.addLabel(label);
        Mockito.when(labelRepository.findLabelById(TEST_ID1)).thenReturn(label);
        Mockito.when(hundRepository.findHundById(TEST_ID1)).thenReturn(hund);
        Mockito.when(hundRepository.saveAndFlush(Mockito.any(Hund.class))).thenReturn(hund);

        mockMvc.perform(MockMvcRequestBuilders.delete("/hund/" + TEST_ID1 + "/label/" + TEST_ID1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(TEST_ID1),
                        jsonPath("$.labels.length()").value(0)
                );
    }
}