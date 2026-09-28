package projekt_tierheim.tierheim.rest;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MitgliedControllerTest {
    public static int TEST_ID1 = 1;
    public static int TEST_MITGLIEDSNUMMER1 = 12345678;
    public static String TEST_PASSWORT1 = "password1";
    public static boolean TEST_ERFAHRUNG1 = false;

    public static int TEST_ID2 = 2;
    public static int TEST_MITGLIEDSNUMMER2 = 90123456;
    public static String TEST_PASSWORT2 = "password2";
    public static boolean TEST_ERFAHRUNG2 = true;

    public static Mitglied getTestMitglied1() {
        return new Mitglied(TEST_ID1, TEST_MITGLIEDSNUMMER1, TEST_PASSWORT1, TEST_ERFAHRUNG1);
    }

    public static Mitglied getUpdateMitglied1() {
        return new Mitglied(TEST_ID1, TEST_MITGLIEDSNUMMER1, "geheim", TEST_ERFAHRUNG2);
    }

    public static Mitglied getTestMitglied2() {
        return new Mitglied(TEST_ID2, TEST_MITGLIEDSNUMMER2, TEST_PASSWORT2, TEST_ERFAHRUNG2);
    }

    public static List<Mitglied> getAllMitglieder() {
        return List.of(getTestMitglied1(), getTestMitglied2());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private MitgliedRepository mitgliedRepository;

    @Test
    void findAll() throws Exception {
        Mockito.when(mitgliedRepository.findAll()).thenReturn(getAllMitglieder());
        mockMvc.perform(MockMvcRequestBuilders.get("/mitglied/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(TEST_ID1),
                        jsonPath("$[0].erfahrung").value(TEST_ERFAHRUNG1),
                        jsonPath("$[1].erfahrung").value(TEST_ERFAHRUNG2),
                        jsonPath("$[0].passwort").doesNotExist(),
                        jsonPath("$[1].passwort").doesNotExist()
                );
    }

    @Test
    void findByMitgliedsnummer() throws Exception {
        Mockito.when(mitgliedRepository.findMitgliedByMitgliedsnummer(TEST_MITGLIEDSNUMMER1)).thenReturn(getTestMitglied1());
        mockMvc.perform(MockMvcRequestBuilders.get("/mitglied/" +  TEST_MITGLIEDSNUMMER1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("erfahrung").value(TEST_ERFAHRUNG1),
                        jsonPath("passwort").doesNotExist()
                );
    }

    @Test
    void newMitglied() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mitgliedsnummer", TEST_MITGLIEDSNUMMER1);
        jsonObject.put("passwort", TEST_PASSWORT1);

        Mockito.when(mitgliedRepository.saveAndFlush(Mockito.any(Mitglied.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/mitglied")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("mitgliedsnummer").value(TEST_MITGLIEDSNUMMER1),
                        jsonPath("passwort").doesNotExist()
                );
    }

    @Test
    void updateMitglied() throws Exception {
        String neuesPasswort = "NeuesPasswort1234";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mitgliedsnummer", TEST_MITGLIEDSNUMMER1);
        jsonObject.put("passwort", neuesPasswort);

        Mitglied mitglied = getTestMitglied1();

        Mockito.when(mitgliedRepository.findMitgliedByMitgliedsnummer(TEST_MITGLIEDSNUMMER1))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/mitglied/" + TEST_MITGLIEDSNUMMER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("passwort").doesNotExist()
                );

        ArgumentCaptor<Mitglied> captor = ArgumentCaptor.forClass(Mitglied.class);
        Mockito.verify(mitgliedRepository).saveAndFlush(captor.capture());
        Mitglied gespeichertesMitglied = captor.getValue();

        assertNotEquals(neuesPasswort, gespeichertesMitglied.getPasswort());
        assertTrue(gespeichertesMitglied.getPasswort().startsWith("$argon2"));
    }

    @Test
    void deleteMitglied() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/mitglied/" + TEST_MITGLIEDSNUMMER1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(
                        status().isOk()
                );
        Mockito.verify(mitgliedRepository, Mockito.times(1))
                .deleteByMitgliedsnummer(TEST_MITGLIEDSNUMMER1);
    }
}