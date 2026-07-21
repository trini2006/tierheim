package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;

@RestController
@RequestMapping("/mitglied")
public class MitgliedController {
    private final MitgliedRepository mitgliedRepository;

    @Autowired
    public MitgliedController(MitgliedRepository mitgliedRepository) {
        this.mitgliedRepository = mitgliedRepository;
    }
}
