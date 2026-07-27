package projekt_tierheim.tierheim.rest;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
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

    public static Admin getAdmin1(){
        return new Admin(TEST_ID1, TEST_PERSONALNUMMER1, TEST_PASSWORT1);
    }

    public static Admin getAdmin2(){
        return new Admin(TEST_ID2, TEST_PERSONALNUMMER2, TEST_PASSWORT2);
    }

    public static List<Admin> getAlleAdmins(){
        return List.of(getAdmin1(), getAdmin2());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AdminRepository adminRepository;

    @Test
    void getAdminById() throws Exception{
        Mockito.when(adminRepository.findAdminById(TEST_ID1)).thenReturn(getAdmin1());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("personalnummer").value(TEST_PERSONALNUMMER1),
                        jsonPath("passwort").value(TEST_PASSWORT1)
                );
    }

    @Test
    void getAllAdmins() throws Exception{
        Mockito.when(adminRepository.findAll()).thenReturn(getAlleAdmins());
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].id").value(TEST_ID1),
                        jsonPath("$[1].personalnummer").value(TEST_PERSONALNUMMER2),
                        jsonPath("$[1].passwort").value(TEST_PASSWORT2)
                );
    }

    @Test
    void newAdmin() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("personalnummer", TEST_PERSONALNUMMER1);
        jsonObject.put("passwort", TEST_PASSWORT1);

        Mockito.when(adminRepository.saveAndFlush(Mockito.any(Admin.class))).thenReturn(getAdmin1());
        mockMvc.perform(MockMvcRequestBuilders.post("/admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("personalnummer").value(TEST_PERSONALNUMMER1),
                        jsonPath("passwort").value(TEST_PASSWORT1)
                );
    }

    @Test
    void updateAdmin() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("passwort", "NeuesPasswort1234");

        Admin updatedAdmin = new Admin();
        updatedAdmin.setId(TEST_ID1);
        updatedAdmin.setPersonalnummer(TEST_PERSONALNUMMER1);
        updatedAdmin.setPasswort("NeuesPasswort1234");

        Mockito.when(adminRepository.findAdminById(TEST_ID1)).thenReturn(getAdmin1());
    }
}