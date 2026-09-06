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
    // ToDO Müsste eigentlich eine Liste sein
    @GetMapping("/{tag}")
    public GassiGehZeiten getZeitByTag(@PathVariable("tag") String tag) {
        Enum<Tage> EnumTag = Enum.valueOf(Tage.class, tag.toUpperCase());
        return gassiGehZeitenRepository.findGassiGehZeitenByTag(EnumTag);
    }

    @PostMapping
    public GassiGehZeiten newZeit(@Valid @RequestBody GassiGehZeitenDTO zeitDTO)
    {
        GassiGehZeiten gassiZeiten = GassiGehZeiten.convertToGassiGehZeiten(zeitDTO);
        return gassiGehZeitenRepository.saveAndFlush(gassiZeiten);
    }

    // ToDO Zeiten müssen irgendwie eindeutig identifizierbar sein
    // z.B. an einem Mo können Vor- und Nachmittags Spaziergänge stattfinden
    @PutMapping("/{tag}")
    public GassiGehZeiten updateZeit(@PathVariable("tag") String tag, @Valid @RequestBody GassiGehZeitenDTO zeitDTO) {
        Enum<Tage> EnumTag = Enum.valueOf(Tage.class, tag.toUpperCase());
        GassiGehZeiten zeit = gassiGehZeitenRepository.findGassiGehZeitenByTag(EnumTag);
        if(zeit == null) {
            return null;
        }
        zeit.setTag(zeitDTO.tag());
        zeit.setVon(zeitDTO.von());
        zeit.setBis(zeitDTO.bis());
        return gassiGehZeitenRepository.saveAndFlush(zeit);
    }

    // ToDO Zeiten müssen eindeutig identifizierbar sein
    @DeleteMapping("/{tag}")
    public void deleteZeit(@PathVariable("tag") String tag) {

    }
}
