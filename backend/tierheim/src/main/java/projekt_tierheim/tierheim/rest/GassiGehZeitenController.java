package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;

@RestController
@RequestMapping("/gassiGehZeiten")
public class GassiGehZeitenController {
    private final GassiGehZeitenRepository gassiGehZeitenRepository;

    @Autowired
    public GassiGehZeitenController(GassiGehZeitenRepository gassiGehZeitenRepository) {
        this.gassiGehZeitenRepository = gassiGehZeitenRepository;
    }
}
