package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelDTO;
import projekt_tierheim.tierheim.db.Label.LabelRepository;

import java.util.List;

@RestController
@RequestMapping("/label")
public class LabelController {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @GetMapping("/{id}")
    public Label getLabelById(@PathVariable("id") int id) {
        return labelRepository.findLabelById(id);
    }

    @GetMapping("/all")
    public List<Label> getAllLabel() {
        return labelRepository.findAll();
    }

    @GetMapping("/search")
    public List<Label> getLabelByBezeichnung(@RequestParam(required = false) String bezeichnung) {
        if(bezeichnung == null || bezeichnung.isBlank()) {
            return labelRepository.findAll();
        }
        return labelRepository.findLabelByBezeichnungIgnoreCase(bezeichnung);
    }

    @PostMapping()
    public Label newLabel(@Valid @RequestBody LabelDTO labelDTO) {
        Label label = Label.convertToLabel(labelDTO);
        return labelRepository.saveAndFlush(label);
    }

    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable("id") int id, @Valid @RequestBody LabelDTO labelDTO) {
        Label label = labelRepository.findLabelById(id);
        if(label == null){
            return null;
        }
        label.setBezeichnung(labelDTO.bezeichnung());
        label.setHinweis(labelDTO.hinweis());
        return labelRepository.saveAndFlush(label);
    }

    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable("id") int id) {
        labelRepository.deleteById(id);
    }
}
