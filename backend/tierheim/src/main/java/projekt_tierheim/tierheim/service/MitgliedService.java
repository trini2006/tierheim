package projekt_tierheim.tierheim.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminDTO;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedDTO;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.exception.NotFoundException;

@Service
public class MitgliedService {
    private MitgliedRepository mitgliedRepository;

    private final PasswordEncoder passwordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public MitgliedService(MitgliedRepository mitgliedRepository){
        this.mitgliedRepository = mitgliedRepository;
    }

    public Mitglied createMitglied(MitgliedDTO mitgliedDTO) {
        Mitglied mitglied = new Mitglied();
        mitglied.setMitgliedsnummer(mitgliedDTO.mitgliedsnummer());

        // Passwort hashen
        String hash = passwordEncoder.encode(mitgliedDTO.passwort());
        mitglied.setPasswort(hash);

        return mitgliedRepository.saveAndFlush(mitglied);
    }

    public Mitglied updateMitglied(int mitgliedsnummer, MitgliedDTO mitgliedDTO) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);

        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer " +  mitgliedsnummer + " nicht gefunden");
        }
        mitglied.setMitgliedsnummer(mitgliedDTO.mitgliedsnummer());

        // Neues Passwort hashen
        String hash = passwordEncoder.encode(mitgliedDTO.passwort());
        mitglied.setPasswort(hash);

        return mitgliedRepository.saveAndFlush(mitglied);
    }

    public Mitglied getMitgliedByMitgliedsnummer(int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer \" +  mitgliedsnummer + \" nicht gefunden");
        }
        return mitglied;
    }
}
