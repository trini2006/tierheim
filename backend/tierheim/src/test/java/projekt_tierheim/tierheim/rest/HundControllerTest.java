package projekt_tierheim.tierheim.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Hund.Groesse;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.Strecke;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class HundControllerTest {
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

    // Aris Test-Hund 2 - mit Sperrgrund
    public static int TEST_ID2 = 2;
    public static String TEST_NAME2 = "Aris";
    public static boolean TEST_GESCHLECHT2 = false;
    public static int TEST_ALTER2 = 2;
    public static String TEST_RASSE2 = "Husky";
    public static int TEST_GEWICHT2 = 23;
    public static boolean TEST_ERFAHRUNG2 = false;
    public static Groesse TEST_GROESSE2 = Groesse.MITTEL;
    public static Strecke TEST_STRECKE2 = Strecke.LANG;
    public static LocalDate TEST_GESPERRTVON = LocalDate.of(2026, 7, 23);
    public static LocalDate TEST_GESPERRTBIS = LocalDate.of(2026, 7, 30);
    public static boolean TEST_ISTGESPERRT = true;
    public static String TEST_SPERRGRUND = "Probewohnen";

    public static Admin TEST_ERSTELLTVON = new Admin(1, 1234567890, "geheim123");

    public static Hund getTestHund1() {
        return new Hund(TEST_ID1, TEST_NAME1, TEST_GESCHLECHT1, TEST_ALTER1, TEST_RASSE1, TEST_GROESSE1, TEST_GEWICHT1, TEST_ERFAHRUNG1, TEST_STRECKE1, TEST_ERSTELLTVON);
    }

    public static Hund getTestHund2() {
        return new Hund(TEST_ID2, TEST_NAME2, TEST_GESCHLECHT2, TEST_ALTER2, TEST_RASSE2, TEST_GROESSE2, TEST_GEWICHT2, TEST_ERFAHRUNG2, TEST_STRECKE2, TEST_ERSTELLTVON);
    }

    public static List<Hund> getAlleTestHunde() {
        return List.of(getTestHund1(), getTestHund2());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private HundRepository hundRepository;

    // ToDO GET einzelnen Hund nach Id
    @Test
    void getHundById() throws Exception {

    }

    // ToDO GET alle Hunde
    @Test
    void getAlleHunde() throws Exception{

    }

    // ToDO GET (search) nach Hundenamen
    @Test
    void getHundByName() throws Exception {

    }

    // ToDo POST neuer Hund
    @Test
    void neuerHund() throws Exception {

    }

    // ToDO DELETE vorhandener Hund
    @Test
    void deleteHund() throws Exception {

    }

    // ToDO DELETE Label von Hund
    @Test
    void deleteLabelVonHund() throws Exception {

    }

    // ToDO PUT aktualisiere vorhandener Hund
    @Test
    void updateHund() throws Exception {

    }

    // ToDO PUT Label hinzufügen an Hund
    @Test
    void upadteLabelHund() throws Exception {

    }

    // ToDO PUT Sperrgrund hinzufügen
    @Test
    void updateSperrgrund() throws Exception {

    }
}