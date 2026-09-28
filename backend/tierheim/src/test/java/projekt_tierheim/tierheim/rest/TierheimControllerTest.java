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
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TierheimControllerTest {
    public static int TEST_ID1 = 1;
    public static String TEST_NAME1 = "Tierschutzverein Weiden u. U. e.V.";
    public static String TEST_STRASSE1 = "Schustermooslohe";
    public static String TEST_HAUSNUMMER1 = "96";
    public static String TEST_PLZ1 = "92637";
    public static String TEST_ORT1 = "Weiden i. d. Oberpfalz";
    public static String TEST_TELEFONNR1 = "0961 25780";
    public static String TEST_EMAIL1 = "info@tierheim-weiden.de";
    public static String TEST_LINK1 = "https://tierheim-weiden.de";

    public static String TEST_BILD1 = "profil.png";
    public static String TEST_FARBE1 = "B60028";

    public static int TEST_ZEITBLOCK1 = 30;
    public static int TEST_RUHEZEIT1 = 60;
    public static int TEST_MAXGASSI1 = 120;
    public static int TEST_MINGASSI1 = 30;

    public static Tierheim getTestTierheim() {
        return new Tierheim(
                TEST_ID1,
                TEST_NAME1,
                TEST_STRASSE1,
                TEST_HAUSNUMMER1,
                TEST_PLZ1,
                TEST_ORT1,
                TEST_BILD1,
                TEST_TELEFONNR1,
                "",
                TEST_LINK1,
                TEST_BILD1,
                TEST_FARBE1,
                TEST_ZEITBLOCK1,
                TEST_RUHEZEIT1,
                TEST_MAXGASSI1,
                TEST_MINGASSI1);
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private TierheimRepository tierheimRepository;

    @Test
    public void updateTierheimStammdaten() throws Exception {
        Tierheim updatedTierheim = getTestTierheim();
        updatedTierheim.setOrt("Regensburg");
        updatedTierheim.setPlz("93055");

        Mockito.when(tierheimRepository.findTierheimById(TEST_ID1)).thenReturn(getTestTierheim());
        Mockito.when(tierheimRepository.saveAndFlush(Mockito.any(Tierheim.class))).thenReturn(updatedTierheim);

        JSONObject tierheim = new JSONObject();
        tierheim.put("name", updatedTierheim.getName());
        tierheim.put("strasse",  updatedTierheim.getStrasse());
        tierheim.put("hausnummer", updatedTierheim.getHausnummer());
        tierheim.put("plz", updatedTierheim.getPlz());
        tierheim.put("ort", updatedTierheim.getOrt());
        tierheim.put("telefonnummer", updatedTierheim.getTelefonnummer());
        tierheim.put("notfallnummer", null);
        tierheim.put("email", updatedTierheim.getEmail());
        tierheim.put("link", updatedTierheim.getLink());

        mockMvc.perform(MockMvcRequestBuilders.put("/tierheim/" + TEST_ID1 + "/stammdaten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tierheim.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("ort").value("Regensburg"),
                        jsonPath("plz").value("93055")
                );

    }

    @Test
    public void updateTierheimAussehen() throws Exception {
        Tierheim updatedTierheim = getTestTierheim();
        updatedTierheim.setBannerfarbe("Blau");
        updatedTierheim.setBild("neuesBild.png");

        Mockito.when(tierheimRepository.findTierheimById(TEST_ID1)).thenReturn(getTestTierheim());
        Mockito.when(tierheimRepository.saveAndFlush(Mockito.any(Tierheim.class))).thenReturn(updatedTierheim);

        JSONObject tierheim = new JSONObject();
        tierheim.put("bild", updatedTierheim.getBild());
        tierheim.put("bannerfarbe", updatedTierheim.getBannerfarbe());

        mockMvc.perform(MockMvcRequestBuilders.put("/tierheim/" + TEST_ID1 + "/aussehen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tierheim.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("bild").value(updatedTierheim.getBild()),
                        jsonPath("bannerfarbe").value(updatedTierheim.getBannerfarbe())
                );
    }

    @Test
    public void updateGassiEinstellungen() throws Exception {
        Tierheim updatedTierheim = getTestTierheim();
        updatedTierheim.setZeitblock(45);
        updatedTierheim.setRuhezeit(30);

        Mockito.when(tierheimRepository.findTierheimById(TEST_ID1)).thenReturn(getTestTierheim());
        Mockito.when(tierheimRepository.saveAndFlush(Mockito.any(Tierheim.class))).thenReturn(updatedTierheim);

        JSONObject tierheim = new JSONObject();
        tierheim.put("zeitblock",  updatedTierheim.getZeitblock());
        tierheim.put("ruhezeit", updatedTierheim.getRuhezeit());
        tierheim.put("maxGassi", updatedTierheim.getMaxGassi());
        tierheim.put("minGassi", updatedTierheim.getMinGassi());

        mockMvc.perform(MockMvcRequestBuilders.put("/tierheim/" + TEST_ID1 + "/gassi-einstellungen")
                .contentType(MediaType.APPLICATION_JSON)
                .content(tierheim.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("zeitblock").value(updatedTierheim.getZeitblock()),
                        jsonPath("ruhezeit").value(updatedTierheim.getRuhezeit()),
                        jsonPath("name").value(TEST_NAME1),
                        jsonPath("maxGassi").value(TEST_MAXGASSI1)
                );
    }
}