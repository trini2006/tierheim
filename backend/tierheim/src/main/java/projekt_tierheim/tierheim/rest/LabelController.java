package projekt_tierheim.tierheim.rest;

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
    public Label getLabel(@PathVariable int id){return labelRepository.findLabelById(id);}

    @GetMapping("all")
    public List<Label> getAllLabel(){return labelRepository.findAll();}

    @PostMapping()
    public Label saveLabel(@RequestBody LabelDTO labelDTO){
        Label label = Label.convertToLabel(labelDTO);
        return labelRepository.saveAndFlush(label);
    }
}
