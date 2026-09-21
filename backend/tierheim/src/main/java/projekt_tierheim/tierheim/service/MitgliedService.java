package projekt_tierheim.tierheim.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Mitglied.*;
import projekt_tierheim.tierheim.exception.NotFoundException;

@Service
public class MitgliedService {
    private MitgliedRepository mitgliedRepository;

    private final PasswordEncoder passwordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public MitgliedService(MitgliedRepository mitgliedRepository){
        this.mitgliedRepository = mitgliedRepository;
    }

    public MitgliedResponseDTO createMitglied(MitgliedCreateDTO mitgliedDTO) {
        Mitglied mitglied = new Mitglied();
        mitglied.setMitgliedsnummer(mitgliedDTO.mitgliedsnummer());

        // Passwort hashen
        String hash = passwordEncoder.encode(mitgliedDTO.passwort());
        mitglied.setPasswort(hash);

        Mitglied gespeichert = mitgliedRepository.saveAndFlush(mitglied);
        return new MitgliedResponseDTO(
                gespeichert.getId(),
                gespeichert.getMitgliedsnummer(),
                gespeichert.getErfahrung()
        );
    }

    public MitgliedResponseDTO updateMitglied(int mitgliedsnummer, MitgliedUpdateDTO mitgliedDTO) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);

        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer " +  mitgliedsnummer + " nicht gefunden");
        }

        // Neues Passwort hashen
        String hash = passwordEncoder.encode(mitgliedDTO.passwort());
        mitglied.setPasswort(hash);

        mitglied.setErfahrung(mitgliedDTO.erfahrung());

        Mitglied gespeichert = mitgliedRepository.saveAndFlush(mitglied);

        return new MitgliedResponseDTO(
                gespeichert.getId(),
                gespeichert.getMitgliedsnummer(),
                gespeichert.getErfahrung()
        );
    }

    public Mitglied getMitgliedByMitgliedsnummer(int mitgliedsnummer) {
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(mitgliedsnummer);
        if (mitglied == null) {
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer \" +  mitgliedsnummer + \" nicht gefunden");
        }
        return mitglied;
    }

    public boolean login(MitgliedLoginDTO dto){
        Mitglied mitglied = mitgliedRepository.findMitgliedByMitgliedsnummer(
                dto.mitgliedsnummer()
        );
        if(mitglied == null){
            throw new NotFoundException("Mitglied mit der Mitgliedsnummer \" +  mitgliedsnummer + \" nicht gefunden");
        }

        return passwordEncoder.matches(
                dto.passwort(), // eingegebenes Passwort
                mitglied.getPasswort() // gespeicherter Hash
        );
    }
}
