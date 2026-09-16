package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedDTO;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.exception.NotFoundException;
import projekt_tierheim.tierheim.service.MitgliedService;

import java.util.List;

@RestController
@RequestMapping("/mitglied")
public class MitgliedController {
    private final MitgliedRepository mitgliedRepository;
    private final MitgliedService mitgliedService;

    @Autowired
    public MitgliedController(MitgliedRepository mitgliedRepository, MitgliedService mitgliedService) {
        this.mitgliedRepository = mitgliedRepository;
        this.mitgliedService = mitgliedService;
    }

    @GetMapping("/all")
    public List<Mitglied> findAll() {
        return mitgliedRepository.findAll();
    }

    @GetMapping("/{mitgliedsnummer}")
    public Mitglied getMitgliedByMitgliedsnummer(@PathVariable("mitgliedsnummer") int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if(mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer " +  mitgliedsnummer + " nicht gefunden");
        }
        return mitglied;
    }

    @PostMapping("/new")
    public Mitglied newMitglied(@Valid @RequestBody MitgliedDTO mitgliedDTO) {
        return mitgliedService.createMitglied(mitgliedDTO);
    }

    @PutMapping("/{mitgliedsnummer}")
    public Mitglied updateMitglied(@PathVariable("mitgliedsnummer") int mitgliedsnummer, @Valid @RequestBody MitgliedDTO mitgliedDTO) {
        return mitgliedService.updateMitglied(mitgliedsnummer, mitgliedDTO);
    }

    @DeleteMapping("/{mitgliedsnummer}")
    public void deleteMitglied(@PathVariable("mitgliedsnummer") int mitgliedsnummer) {
        if(!mitgliedRepository.existsByMitgliedsnummer(mitgliedsnummer)) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer " +  mitgliedsnummer + " nicht gefunden");
        }
        mitgliedRepository.deleteByMitgliedsnummer(mitgliedsnummer);
    }
}
