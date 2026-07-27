package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundDTO;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;

import java.util.List;

@RestController
@RequestMapping("/hund")
public class HundController {
    private final HundRepository hundRepository;
    private final LabelRepository labelRepository;

    @Autowired
    public HundController(HundRepository hundRepository, LabelRepository labelRepository) {
        this.hundRepository = hundRepository;
        this.labelRepository = labelRepository;
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
    public List<Hund> getHundByName(@RequestParam String name) {
        if(name == null) {
            return null;
        }
        return hundRepository.findHundByNameIgnoreCase(name);
    }

    @PostMapping()
    public Hund newHund(@Valid @RequestBody HundDTO hundDTO) {
        Hund hund = Hund.convertToHund(hundDTO);
        return hundRepository.saveAndFlush(hund);
    }

    @PutMapping("/{id}")
    public Hund updateHund(@PathVariable int id, @Valid @RequestBody HundDTO neuerHund)
    {
        Hund hundAlt = hundRepository.findHundById(id);
        if(hundAlt == null) {
            return null;
        }
        hundAlt.setName(neuerHund.name());
        hundAlt.setGeschlecht(neuerHund.geschlecht());
        hundAlt.setJahre(neuerHund.jahre());
        hundAlt.setRasse(neuerHund.rasse());
        hundAlt.setGroesse(neuerHund.groesse());
        hundAlt.setGewicht(neuerHund.gewicht());
        hundAlt.setErfahrung(neuerHund.erfahrung());
        hundAlt.setStrecke(neuerHund.strecke());

        return hundRepository.saveAndFlush(hundAlt);
    }

    @DeleteMapping("/{id}")
    public void deleteHund(@PathVariable int id) {
        Hund hund = hundRepository.findHundById(id);
        hundRepository.delete(hund);
    }

    @PutMapping("/{id}")
    public Hund addLabel(@PathVariable int hundId, @RequestParam int labelId) {
        Hund hund = hundRepository.findHundById(hundId);
        Label label = labelRepository.findLabelById(labelId);

        if(!hund.getLabels().contains(label)) {
            hund.addLabel(label);
            return hundRepository.saveAndFlush(hund);
        }
        return hund;
    }

}
