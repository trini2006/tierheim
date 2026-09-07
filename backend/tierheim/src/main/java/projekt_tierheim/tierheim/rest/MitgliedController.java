package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedDTO;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;

import java.util.List;

@RestController
@RequestMapping("/mitglied")
public class MitgliedController {
    private final MitgliedRepository mitgliedRepository;

    @Autowired
    public MitgliedController(MitgliedRepository mitgliedRepository) {
        this.mitgliedRepository = mitgliedRepository;
    }

    @GetMapping("/all")
    public List<Mitglied> findAll() {
        return mitgliedRepository.findAll();
    }

    @GetMapping("/{mitgliedsnummer}")
    public Mitglied findByMitgliedsnummer(@PathVariable("mitgliedsnummer") int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if(mitglied == null) {
            return null;
        }
        return mitglied;
    }

    @PostMapping("/new")
    public Mitglied newMitglied(@Valid @RequestBody MitgliedDTO mitgliedDTO) {
        Mitglied mitglied = Mitglied.convertToMitglied(mitgliedDTO);
        return mitgliedRepository.saveAndFlush(mitglied);
    }

    @PutMapping("/{mitgliedsnummer}")
    public Mitglied updateMitglied(@PathVariable("mitgliedsnummer") int mitgliedsnummer, @Valid @RequestBody MitgliedDTO mitgliedDTO) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if(mitglied == null) {
            return null;
        }

        mitglied.setErfahrung(mitgliedDTO.erfahrung());
        mitglied.setPasswort(mitgliedDTO.passwort());
        return mitgliedRepository.saveAndFlush(mitglied);
    }

    @DeleteMapping("/{mitgliedsnummer}")
    public void deleteMitglied(@PathVariable("mitgliedsnummer") int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        mitgliedRepository.delete(mitglied);
    }
}
