package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Label.LabelRepository;

@RestController
@RequestMapping("/label")
public class LabelController {
    private final LabelRepository labelRepository;

    @Autowired
    public LabelController(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }
}
