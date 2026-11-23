package projekt_tierheim.tierheim.rest;

import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Benachrichtigung.Benachrichtigung;
import projekt_tierheim.tierheim.db.Benachrichtigung.BenachrichtigungRepository;
import projekt_tierheim.tierheim.db.Benachrichtigung.BenachrichtigungsTyp;
import projekt_tierheim.tierheim.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/benachrichtigung")
public class BenachrichtigungController {
    private final BenachrichtigungRepository benachrichtigungRepository;

    public BenachrichtigungController(BenachrichtigungRepository benachrichtigungRepository) {
        this.benachrichtigungRepository = benachrichtigungRepository;
    }

    public record BenachrichtigungDTO(int id, BenachrichtigungsTyp typ, String text, LocalDateTime erstelltAm, boolean gelesen) {
        static BenachrichtigungDTO von(Benachrichtigung b) {
            return new BenachrichtigungDTO(b.getId(), b.getTyp(), b.getText(), b.getErstelltAm(), b.isGelesen());
        }
    }

    @GetMapping("/mitglied/{mitgliedId}")
    public List<BenachrichtigungDTO> fuerMitglied(@PathVariable int mitgliedId) {
        return benachrichtigungRepository.findByMitgliedIdOrderByErstelltAmDesc(mitgliedId).stream()
                .map(b -> BenachrichtigungDTO.von((Benachrichtigung) b)).toList();
    }

    @GetMapping("/tierheim/{tierheimId}")
    public List<BenachrichtigungDTO> fuerTierheim(@PathVariable int tierheimId) {
        return benachrichtigungRepository.findByTierheimIdOrderByErstelltAmDesc(tierheimId).stream()
                .map(b -> BenachrichtigungDTO.von((Benachrichtigung) b)).toList();
    }

    @GetMapping("/mitglied/{mitgliedId}/ungelesen")
    public long ungelesenMitglied(@PathVariable int mitgliedId) {
        return benachrichtigungRepository.countByMitgliedIdAndGelesenFalse(mitgliedId);
    }

    @GetMapping("/tierheim/{tierheimId}/ungelesen")
    public long ungelesenTierheim(@PathVariable int tierheimId) {
        return benachrichtigungRepository.countByTierheimIdAndGelesenFalse(tierheimId);
    }

    @PutMapping("/{id}/gelesen")
    public BenachrichtigungDTO markiereGelesen(@PathVariable int id) {
        Benachrichtigung b = benachrichtigungRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Benachrichtigung mit der Id " + id + " nicht gefunden"));
        b.setGelesen(true);
        return BenachrichtigungDTO.von(benachrichtigungRepository.saveAndFlush(b));
    }


}
