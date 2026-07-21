package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class LabelControllerTest {
    // Attribute für Testobjekt anlegen
    public static int TEST_ID1 = 1;
    public static String TEST_BEZEICHNUNG1 = "Freundlich";
    public static boolean TEST_HINWEIS1 = false;

    public static int TEST_ID2 = 2;
    public static String TEST_BEZEICHNUNG2 = "Fahrräder";
    public static boolean TEST_HINWEIS2 = true;

    // Methoden, die Testobjekte liefern
    public static Label getNormalLabel() {
        return new Label(TEST_ID1, TEST_BEZEICHNUNG1, TEST_HINWEIS1);
    }

    public static Label getHinweisLabel() {
        return new Label(TEST_ID2, TEST_BEZEICHNUNG2, TEST_HINWEIS2);
    }

    public static List<Label> getAlleLabel() {
        return List.of(getNormalLabel(), getHinweisLabel());
    }

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private LabelRepository labelRepository;

    // ToDO GET einzelnes Label

    // ToDO GET alle Label

    // ToDO POST neues Label

    // ToDO PUT vorhandenes Label

    // ToDO DELETE vorhandenes Label
}