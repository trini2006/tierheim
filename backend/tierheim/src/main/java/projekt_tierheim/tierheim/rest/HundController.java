package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Hund.*;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;
import projekt_tierheim.tierheim.exception.NotFoundException;
import projekt_tierheim.tierheim.service.HundService;
import projekt_tierheim.tierheim.service.ReservierungService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/hund")
public class HundController {
    private final HundRepository hundRepository;
    private final LabelRepository labelRepository;
    private final TierheimRepository tierheimRepository;
    private final MitgliedRepository mitgliedRepository;
    private final ReservierungService reservierungService;
    private final HundService hundService;

    @Autowired
    public HundController(HundRepository hundRepository, LabelRepository labelRepository, TierheimRepository tierheimRepository, MitgliedRepository mitgliedRepository, ReservierungService reservierungService, HundService hundService) {
        this.hundRepository = hundRepository;
        this.labelRepository = labelRepository;
        this.tierheimRepository = tierheimRepository;
        this.mitgliedRepository = mitgliedRepository;
        this.reservierungService = reservierungService;
        this.hundService = hundService;
    }

    @GetMapping("/{id}")
    public Hund getHund(@PathVariable int id) {
        Hund hund = hundRepository.findHundById(id);
        if(hund == null) {
            throw new NotFoundException("Hund nicht gefunden");
        }
        return hund;
    }

    @GetMapping("/all")
    public List<Hund> getAlleHunde() {
        return hundRepository.findAll();
    }

    @GetMapping("/all/available/{mitgliedId}")
    public HundAuswahlResponse getAlleHundeAvailable(
            @PathVariable("mitgliedId") int mitgliedId,
            @RequestParam LocalDate datum,
            @RequestParam LocalTime von,
            @RequestParam LocalTime bis) {
        Mitglied mitglied = mitgliedRepository.findMitgliedById(mitgliedId);
        if(mitglied == null) {
            throw new NotFoundException("Mitglied mit der Id " +  mitgliedId + " nicht gefunden");
        }
        List<Hund> verfuegbareHunde = hundService.ermittleVeruegbareHunde(mitglied, datum, von, bis);
        Hund vorschlag = hundService.waehleVorschlag(verfuegbareHunde);

        return new HundAuswahlResponse(verfuegbareHunde, vorschlag);
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
            throw new NotFoundException("Tierheim mit der Id" + hundDTO.tierheimId() + " nicht gefunden");
        }
        Hund hund = Hund.convertToHund(hundDTO, tierheim);
        return hundRepository.saveAndFlush(hund);
    }

    @PutMapping("/{id}")
    public Hund updateHund(@PathVariable("id") int id, @Valid @RequestBody HundDTO neuerHund)
    {
        Hund hundAlt = hundRepository.findHundById(id);
        if(hundAlt == null) {
            throw new NotFoundException("Hund mit der Id " + id + " nicht gefunden");
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
        if(!hundRepository.existsById(id)) {
            throw new NotFoundException("Hund mit der Id " + id + " nicht gefunden");
        }
        hundRepository.deleteById(id);
    }

    // HUND SPERREN
    @PutMapping("/sperren/{id}")
    public Hund sperrgrundHinzufuegen(@PathVariable("id") int hundId, @Valid @RequestBody SperrHundDTO sperrHundDTO) {
        Hund hund = hundRepository.findHundById(hundId);
        if(hund == null) {
            throw new NotFoundException("Hund mit der Id " + hundId + " nicht gefunden");
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
            throw new NotFoundException("Hund mit der Id " + hundId + " nicht gefunden");
        }

        hund.setIstGesperrt(false);
        hund.setGesperrtVon(null);
        hund.setGesperrtBis(null);
        hund.setSperrGrund(null);

        return hundRepository.saveAndFlush(hund);
    }

    // LABEL
    @PostMapping("/{id}/label/{labelId}")
    public Hund addLabel(@PathVariable("id") int hundId, @PathVariable("labelId") int labelId) {
        Hund hund = hundRepository.findHundById(hundId);
        Label label = labelRepository.findLabelById(labelId);

        if(hund == null) {
            throw new NotFoundException("Hund mit der Id " + hundId + " nicht gefunden");
        }
        if(label == null) {
            throw new NotFoundException("Label mit der Id " + labelId + " nicht gefunden");
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

        if(hund == null) {
            throw new NotFoundException("Hund mit der Id " + hundId + " nicht gefunden");
        }
        if(label == null) {
            throw new NotFoundException("Label mit der Id " + labelId + " nicht gefunden");
        }

        if(!hund.getLabels().contains(label)) {
            throw new IllegalStateException("Der Hund hat dieses Label nicht zugewiesen");
        }
        hund.removeLabel(label);
        return hundRepository.saveAndFlush(hund);
    }
}
