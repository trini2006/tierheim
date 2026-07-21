package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Hund.HundRepository;

@RestController
@RequestMapping("/hund")
public class HundController {
    private final HundRepository hundRepository;

    @Autowired
    public HundController(HundRepository hundRepository) {
        this.hundRepository = hundRepository;
    }
}
