package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;

import java.util.List;

@RestController
@RequestMapping("/hund")
public class HundController {
    private final HundRepository hundRepository;

    @Autowired
    public HundController(HundRepository hundRepository) {
        this.hundRepository = hundRepository;
    }

    @GetMapping("/{id}")
    public Hund getHund(@PathVariable int id) {
        return hundRepository.findHundById(id);
    }

    @GetMapping("/all")
    public List<Hund> getAlleHunde() {
        return hundRepository.findAll();
    }
}
