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
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Test
    void getLabelById() throws Exception{
        Mockito.when(labelRepository.findLabelById(1)).thenReturn(getNormalLabel());
        mockMvc.perform(MockMvcRequestBuilders.get("/label/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("bezeichnung").value(TEST_BEZEICHNUNG1),
                        jsonPath("hinweis").value(TEST_HINWEIS1)
                );
    }


    // ToDO GET alle Label
    @Test
    void getAllLabels() throws Exception{
        Mockito.when(labelRepository.findAll()).thenReturn(getAlleLabel());
        mockMvc.perform(MockMvcRequestBuilders.get("/label/all")
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$[0].bezeichnung").value(TEST_BEZEICHNUNG1),
                        jsonPath("$[1].hinweis").value(TEST_HINWEIS2)
                );
    }

    // ToDO POST neues Label
    @Test
    void newLabel() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bezeichnung", TEST_BEZEICHNUNG1);
        jsonObject.put("hinweis", TEST_HINWEIS1);

        Mockito.when(labelRepository.save(Mockito.any(Label.class)))
                .thenReturn(getNormalLabel());
        mockMvc.perform(MockMvcRequestBuilders.post("/label")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("bezeichnung").value(TEST_BEZEICHNUNG1),
                        jsonPath("hinweis").value(TEST_HINWEIS1)
                );
    }

    // ToDO PUT vorhandenes Label
    @Test
    void updateLabel() throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bezeichnung", "Verspielt");

        Label updatedLabel = new Label();
        updatedLabel.setId(TEST_ID1);
        updatedLabel.setBezeichnung("Verspielt");
        updatedLabel.setHinweis(TEST_HINWEIS1);

        Mockito.when(labelRepository.findLabelById(TEST_ID1)).thenReturn(getNormalLabel());
        Mockito.when(labelRepository.saveAndFlush(Mockito.any(Label.class))).thenReturn(updatedLabel);

        mockMvc.perform(MockMvcRequestBuilders.put("/label/" + TEST_ID1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("id").value(TEST_ID1),
                        jsonPath("bezeichnung").value("Verspielt"),
                        jsonPath("hinweis").value(TEST_HINWEIS1)
                );
    }

    // ToDO DELETE vorhandenes Label
    @Test
    void delete() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/label/" + TEST_ID1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Mockito.verify(labelRepository, Mockito.times(1)).deleteById(TEST_ID1);
    }
}