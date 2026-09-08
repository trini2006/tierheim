package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundDTO;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.SperrHundDTO;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;

import java.util.List;

@RestController
@RequestMapping("/hund")
public class HundController {
    private final HundRepository hundRepository;
    private final LabelRepository labelRepository;
    private final TierheimRepository tierheimRepository;

    @Autowired
    public HundController(HundRepository hundRepository, LabelRepository labelRepository, TierheimRepository tierheimRepository) {
        this.hundRepository = hundRepository;
        this.labelRepository = labelRepository;
        this.tierheimRepository = tierheimRepository;
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
    public List<Hund> getHundByName(@RequestParam(required = false) String name) {
        if(name == null || name.isBlank()) {
            return hundRepository.findAll();
        }
        return hundRepository.findHundByNameIgnoreCase(name);
    }

    @PostMapping()
    public Hund newHund(@Valid @RequestBody HundDTO hundDTO) {
        Tierheim tierheim = tierheimRepository.findTierheimById(hundDTO.tierheimId());
        if(tierheim == null) {
            return null;
        }
        Hund hund = Hund.convertToHund(hundDTO, tierheim);
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

    // HUND SPERREN
    @PutMapping("/sperren/{id}")
    public Hund sperrgrundHinzufuegen(@PathVariable("id") int hundId, @Valid @RequestBody SperrHundDTO sperrHundDTO) {
        Hund hund = hundRepository.findHundById(hundId);
        if(hund == null) {
            return null;
        }

        hund.setIstGesperrt(true);
        hund.setGesperrtVon(sperrHundDTO.gesperrtVon());
        hund.setGesperrtBis(sperrHundDTO.gesperrtBis());
        hund.setSperrGrund(sperrHundDTO.sperrGrund());

        return hundRepository.saveAndFlush(hund);
    }

    // HUND ENTSPERREN
    @PutMapping("/entsperren/{id}")
    public Hund sperrgrundEntfernen(@PathVariable("id") int hundId) {
        Hund hund =  hundRepository.findHundById(hundId);
        if(hund == null) {
            return null;
        }

        hund.setIstGesperrt(false);
        hund.setGesperrtVon(null);
        hund.setGesperrtBis(null);
        hund.setSperrGrund(null);

        return hundRepository.saveAndFlush(hund);
    }

    // LABEL
    // ToDO Frontend und Backend müssen mit möglichen 404 ungehen können
    @PostMapping("/{id}/label/{labelId}")
    public Hund addLabel(@PathVariable("id") int hundId, @PathVariable("labelId") int labelId) {
        Hund hund = hundRepository.findHundById(hundId);
        Label label = labelRepository.findLabelById(labelId);

        // ResponseEntity.notFound().build()
        if(hund == null || label == null) {
            return null;
        }

        if(!hund.getLabels().contains(label)) {
            hund.addLabel(label);
            return hundRepository.saveAndFlush(hund);
        }
        return hund;
    }

    @DeleteMapping("/{id}/label/{labelId}")
    public Hund deleteLabel(@PathVariable("id") int hundId,  @PathVariable("labelId") int labelId) {
        Hund hund = hundRepository.findHundById(hundId);
        Label label = labelRepository.findLabelById(labelId);

        if(hund == null || label == null) {
            return null;
        }
        // theroretisch eigentlich unmöglich
        if(!hund.getLabels().contains(label)) {
            return null;
        }
        hund.removeLabel(label);
        return hundRepository.saveAndFlush(hund);
    }
}
