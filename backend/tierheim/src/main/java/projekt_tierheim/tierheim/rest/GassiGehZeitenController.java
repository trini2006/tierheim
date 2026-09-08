package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenDTO;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;

import java.util.List;

@RestController
@RequestMapping("/gassiGehZeiten")
public class GassiGehZeitenController {
    private final GassiGehZeitenRepository gassiGehZeitenRepository;

    @Autowired
    public GassiGehZeitenController(GassiGehZeitenRepository gassiGehZeitenRepository) {
        this.gassiGehZeitenRepository = gassiGehZeitenRepository;
    }

    @GetMapping("/all")
    public List<GassiGehZeiten> findAllZeiten() {
        return gassiGehZeitenRepository.findAll();
    }

    // Alle Zeiten für einen Tag
    @GetMapping("/tag/{tag}")
    public List<GassiGehZeiten> getZeitByTag(@PathVariable("tag") Tage tag) {
        return gassiGehZeitenRepository.findGassiGehZeitenByTag(tag);
    }

    @GetMapping("/{id}")
    public GassiGehZeiten getGassiGehZeit(@PathVariable("id") int id) {
        return gassiGehZeitenRepository.findGassiGehZeitenById(id);
    }

    @PostMapping("/new")
    public GassiGehZeiten newZeit(@Valid @RequestBody GassiGehZeitenDTO zeitDTO)
    {
        GassiGehZeiten gassiZeiten = GassiGehZeiten.convertToGassiGehZeiten(zeitDTO);
        return gassiGehZeitenRepository.saveAndFlush(gassiZeiten);
    }

    @PutMapping("/{id}")
    public GassiGehZeiten updateZeit(@PathVariable("id") int id, @Valid @RequestBody GassiGehZeitenDTO zeitDTO) {
        GassiGehZeiten zeit = gassiGehZeitenRepository.findGassiGehZeitenById(id);
        if(zeit == null) {
            return null;
        }
        zeit.setTag(zeitDTO.tag());
        zeit.setVon(zeitDTO.von());
        zeit.setBis(zeitDTO.bis());
        return gassiGehZeitenRepository.saveAndFlush(zeit);
    }

    @DeleteMapping("/{id}")
    public void deleteZeit(@PathVariable("id") int id) {
        gassiGehZeitenRepository.deleteById(id);
    }
}
