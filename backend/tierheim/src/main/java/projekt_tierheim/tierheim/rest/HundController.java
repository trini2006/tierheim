package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundDTO;
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

    @GetMapping("/search")
    public Hund getHundByName(@RequestParam String name) {
        if(name == null) {
            return null;
        }
        return hundRepository.findHundByName(name);
    }

    @PostMapping()
    public Hund newHund(@RequestBody HundDTO hundDTO) {
        Hund hund = Hund.convertToHund(hundDTO);
        return hundRepository.saveAndFlush(hund);
    }

    @DeleteMapping("/{id}")
    public void deleteHund(@PathVariable int id) {
        Hund hund = hundRepository.findHundById(id);
        hundRepository.delete(hund);
    }

}
