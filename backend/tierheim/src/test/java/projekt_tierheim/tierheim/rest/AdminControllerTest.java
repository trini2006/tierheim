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
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    public static final int TEST_ID1 = 1;
    public static final int TEST_PERSONALNUMMER1 = 12345;
    public static final String TEST_PASSWORT1 = "ajshash";

    public static final int TEST_ID2 = 2;
    public static final int TEST_PERSONALNUMMER2 = 23451;
    public static final String TEST_PASSWORT2 = "HallomeineLieben";

    public static Admin getAdmin1() {
        return new Admin(TEST_ID1, TEST_PERSONALNUMMER1, TEST_PASSWORT1);
    }

    public static Admin getAdmin2() {
        return new Admin(TEST_ID2, TEST_PERSONALNUMMER2, TEST_PASSWORT2);
    }

    public static List<Admin> getAlleAdmins() {
        return List.of(getAdmin1(), getAdmin2());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AdminRepository adminRepository;

    @Test
    void getAdminByPersonalnummer() throws Exception {

        Mockito.when(adminRepository.findAdminByPersonalnummer(TEST_PERSONALNUMMER1))
                .thenReturn(getAdmin1());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/" + TEST_PERSONALNUMMER1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("id").value(TEST_ID1),
                    jsonPath("personalnummer").value(TEST_PERSONALNUMMER1),
                    // Passwort darf nicht zurückgegeben werden
                    jsonPath("passwort").doesNotExist()
        );
    }

    @Test
    void getAllAdmins() throws Exception {

        Mockito.when(adminRepository.findAll()).thenReturn(getAlleAdmins());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("$[0].id").value(TEST_ID1),
                    jsonPath("$[0].personalnummer").value(TEST_PERSONALNUMMER1),
                    jsonPath("$[1].id").value(TEST_ID2),
                    jsonPath("$[1].personalnummer").value(TEST_PERSONALNUMMER2),
                    // Passwörter dürfen nicht zurückgegeben werden
                    jsonPath("$[0].passwort").doesNotExist(),
                    jsonPath("$[1].passwort").doesNotExist()
        );
    }


    @Test
    void newAdmin() throws Exception {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalnummer", TEST_PERSONALNUMMER1);
        jsonObject.put("passwort", TEST_PASSWORT1);

        Mockito.when(adminRepository.saveAndFlush(Mockito.any(Admin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("personalnummer").value(TEST_PERSONALNUMMER1),
                        // Passwort darf nicht zurückgegeben werden
                        jsonPath("passwort").doesNotExist()
                );

        // Prüfen, was tatsächlich an das Repository übergeben wurde.
        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        Mockito.verify(adminRepository).saveAndFlush(captor.capture());
        Admin gespeicherterAdmin = captor.getValue();
        assertEquals(TEST_PERSONALNUMMER1, gespeicherterAdmin.getPersonalnummer());

        // Das Passwort darf NICHT mehr im Klartext sein.
        assertNotEquals(TEST_PASSWORT1, gespeicherterAdmin.getPasswort());

        // Argon2-Hashes beginnen mit $argon2
        assertTrue(gespeicherterAdmin.getPasswort().startsWith("$argon2"));
    }

    @Test
    void updateAdmin() throws Exception {

        String neuesPasswort = "NeuesPasswort1234";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalnummer", TEST_PERSONALNUMMER1);
        jsonObject.put("passwort", neuesPasswort);

        Admin admin = getAdmin1();

        Mockito.when(adminRepository.findAdminByPersonalnummer(TEST_PERSONALNUMMER1))
                .thenReturn(admin);

        Mockito.when(adminRepository.saveAndFlush(Mockito.any(Admin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/admin/" + TEST_PERSONALNUMMER1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                    status().isOk(),
                    jsonPath("id").value(TEST_ID1),
                    jsonPath("personalnummer").value(TEST_PERSONALNUMMER1),
                    // Passwort darf nicht zurückgegeben werden
                    jsonPath("passwort").doesNotExist()
        );

        ArgumentCaptor<Admin> captor = ArgumentCaptor.forClass(Admin.class);
        Mockito.verify(adminRepository).saveAndFlush(captor.capture());
        Admin gespeicherterAdmin = captor.getValue();

        //Das neue Passwort darf nicht im Klartext gespeichert sein
        assertNotEquals(neuesPasswort, gespeicherterAdmin.getPasswort());

        assertTrue(gespeicherterAdmin.getPasswort().startsWith("$argon2"));
    }


    @Test
    void deleteAdmin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/" + TEST_PERSONALNUMMER1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(
                    status().isOk()
        );

        Mockito.verify(adminRepository, Mockito.times(1))
                .deleteByPersonalnummer(TEST_PERSONALNUMMER1);
    }
}