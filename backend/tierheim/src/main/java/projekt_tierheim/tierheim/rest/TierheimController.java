package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Tierheim.*;

import java.util.List;

@RestController
@RequestMapping("/tierheim")
public class TierheimController {
    private final TierheimRepository tierheimRepository;

    @Autowired
    public TierheimController(TierheimRepository tierheimRepository) {
        this.tierheimRepository = tierheimRepository;
    }

    @GetMapping("/all")
    public List<Tierheim> getAll() {
        return tierheimRepository.findAll();
    }

    @GetMapping("/{id}")
    public Tierheim getById(@PathVariable("id") int id) {
        Tierheim tierheim = tierheimRepository.findById(id).orElse(null);
        if(tierheim == null) {
            return null;
        }
        return tierheim;
    }

    @PostMapping("/new")
    public Tierheim newTierheim(@Valid @RequestBody TierheimDTO tierheimDTO) {
        Tierheim tierheim = Tierheim.convertToTierheim(tierheimDTO);
        return tierheimRepository.saveAndFlush(tierheim);
    }

    @DeleteMapping("/{id}")
    public void deleteTierheim(@PathVariable("id") int id) {
        tierheimRepository.deleteById(id);
    }

    @PutMapping("/{id}/stammdaten")
    public Tierheim updateTierheimStammdaten(@PathVariable("id") int id, @Valid @RequestBody TierheimStammdatenDTO tierheimStammdatenDTO) {
        Tierheim tierheim =  tierheimRepository.findTierheimById(id);
        if(tierheim == null) {
            return null;
        }
        tierheim.setName(tierheimStammdatenDTO.name());
        tierheim.setStrasse(tierheimStammdatenDTO.strasse());
        tierheim.setHausnummer(tierheimStammdatenDTO.hausnummer());
        tierheim.setPlz(tierheimStammdatenDTO.plz());
        tierheim.setOrt(tierheimStammdatenDTO.ort());
        tierheim.setTelefonnummer(tierheimStammdatenDTO.telefonnummer());
        tierheim.setNotfallnummer(tierheimStammdatenDTO.notfallnummer());
        tierheim.setEmail(tierheimStammdatenDTO.email());
        tierheim.setLink(tierheimStammdatenDTO.link());

        return tierheimRepository.saveAndFlush(tierheim);
    }

    @PutMapping("/{id}/aussehen")
    public Tierheim updateTierheimAussehen(@PathVariable("id") int id, @Valid @RequestBody TierheimAussehenDTO tierheimAussehenDTO) {
        Tierheim tierheim =  tierheimRepository.findTierheimById(id);
        if(tierheim == null) {
            return null;
        }
        tierheim.setBild(tierheimAussehenDTO.bild());
        tierheim.setBannerfarbe(tierheimAussehenDTO.bannerfarbe());
        return tierheimRepository.saveAndFlush(tierheim);
    }

    @PutMapping("/{id}/gassi-einstellungen")
    public Tierheim updateTierheimGassiEinstellungen(@PathVariable("id") int id, @Valid @RequestBody TierheimGassiEinstellungenDTO tierheimGassiEinstellungenDTO) {
        Tierheim tierheim =  tierheimRepository.findTierheimById(id);
        if(tierheim == null) {
            return null;
        }
        tierheim.setZeitblock(tierheimGassiEinstellungenDTO.zeitblock());
        tierheim.setRuhezeit(tierheimGassiEinstellungenDTO.ruhezeit());
        tierheim.setMaxGassi(tierheimGassiEinstellungenDTO.maxGassi());
        tierheim.setMinGassi(tierheimGassiEinstellungenDTO.minGassi());
        return tierheimRepository.saveAndFlush(tierheim);
    }
}
