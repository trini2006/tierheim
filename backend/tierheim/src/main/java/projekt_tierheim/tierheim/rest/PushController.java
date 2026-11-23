package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Push.PushAbonnement;
import projekt_tierheim.tierheim.db.Push.PushAbonnementDTO;
import projekt_tierheim.tierheim.db.Push.PushAbonnementRepository;
import projekt_tierheim.tierheim.db.Push.PushEntfernenDTO;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;
import projekt_tierheim.tierheim.exception.NotFoundException;
import projekt_tierheim.tierheim.service.PushSender;

import java.util.Map;

@RestController
@RequestMapping("/push")
public class PushController {
    private final PushAbonnementRepository repository;
    private final MitgliedRepository mitgliedRepository;
    private final TierheimRepository tierheimRepository;
    private final PushSender sender;

    public PushController(PushAbonnementRepository repository, MitgliedRepository mitgliedRepository, TierheimRepository tierheimRepository, PushSender sender) {
        this.repository = repository;
        this.mitgliedRepository = mitgliedRepository;
        this.tierheimRepository = tierheimRepository;
        this.sender = sender;
    }

    @GetMapping("/public-key")
    public Map<String, String> publicKey() {
        return Map.of("publicKey", sender.getPublicKey());      // leer = Push nicht verfügbar
    }

    @PostMapping("/abonnement/mitglied/{mitgliedId}")
    public void abonniereMitglied(@PathVariable int mitgliedId, @Valid @RequestBody PushAbonnementDTO dto) {
        Mitglied m = mitgliedRepository.findMitgliedById(mitgliedId);
        if (m == null) throw new NotFoundException("Mitglied mit der Id " + mitgliedId + " nicht gefunden");
        speichere(dto, m, null);
    }

    @PostMapping("/abonnement/tierheim/{tierheimId}")
    public void abonniereTierheim(@PathVariable int tierheimId, @Valid @RequestBody PushAbonnementDTO dto) {
        Tierheim t = tierheimRepository.findTierheimById(tierheimId);
        if (t == null) throw new NotFoundException("Tierheim mit der Id " + tierheimId + " nicht gefunden");
        speichere(dto, null, t);
    }

    @PostMapping("/abonnement/entfernen")
    public void entferne(@Valid @RequestBody PushEntfernenDTO dto) {
        repository.findByEndpoint(dto.endpoint()).ifPresent(repository::delete);
    }

    private void speichere(PushAbonnementDTO dto, Mitglied m, Tierheim t) {
        PushAbonnement abo = repository.findByEndpoint(dto.endpoint())
                .orElseGet(() -> new PushAbonnement(dto.endpoint()));
        abo.setzeEmpfaenger(m, t);                              // z. B. anderes Konto im selben Browser
        abo.setzeSchluessel(dto.keys().p256dh, dto.keys().auth);
        repository.save(abo);
    }
}