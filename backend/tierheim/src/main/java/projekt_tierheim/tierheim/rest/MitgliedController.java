package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Mitglied.*;
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
    public MitgliedResponseDTO getMitgliedByMitgliedsnummer(@PathVariable("mitgliedsnummer") int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if(mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer " +  mitgliedsnummer + " nicht gefunden");
        }
        return new MitgliedResponseDTO(
                mitglied.getId(),
                mitglied.getMitgliedsnummer(),
                mitglied.getErfahrung()
        );
    }

    @PostMapping("/new")
    public MitgliedResponseDTO newMitglied(@Valid @RequestBody MitgliedCreateDTO mitgliedDTO) {
        return mitgliedService.createMitglied(mitgliedDTO);
    }

    @PostMapping("/login")
    public boolean login(@Valid @RequestBody MitgliedLoginDTO dto){
        return mitgliedService.login(dto);
    }

    @PutMapping("/{mitgliedsnummer}")
    public MitgliedResponseDTO updateMitglied(@PathVariable("mitgliedsnummer") int mitgliedsnummer, @Valid @RequestBody MitgliedUpdateDTO mitgliedDTO) {
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
