package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;

@RestController
@RequestMapping("/tierheim")
public class TierheimController {
    private final TierheimRepository tierheimRepository;

    @Autowired
    public TierheimController(TierheimRepository tierheimRepository) {
        this.tierheimRepository = tierheimRepository;
    }
}
