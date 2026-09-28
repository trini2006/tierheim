package projekt_tierheim.tierheim.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Admin.*;
import projekt_tierheim.tierheim.exception.NotFoundException;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public AdminResponseDTO createAdmin(AdminCreateDTO adminDTO) {
        Admin admin = new Admin();
        admin.setPersonalnummer(adminDTO.personalnummer());

        // Passwort hashen
        String hash = passwordEncoder.encode(adminDTO.passwort());
        admin.setPasswort(hash);

        Admin gespeichert = adminRepository.saveAndFlush(admin);
        return new AdminResponseDTO(
                gespeichert.getId(),
                gespeichert.getPersonalnummer()
        );
    }

    public AdminResponseDTO updateAdmin(int personalnummer, AdminPasswortDTO adminDTO) {
        Admin admin = adminRepository.findAdminByPersonalnummer(personalnummer);

        if (admin == null) {
            throw new NotFoundException("Admin mit der Personalnummer " +  personalnummer + " nicht gefunden");
        }
        // Neues Passwort hashen
        admin.setPasswort(
                passwordEncoder.encode(adminDTO.passwort())
        );
        Admin gespeichert = adminRepository.saveAndFlush(admin);

        return new AdminResponseDTO(
                gespeichert.getId(),
                gespeichert.getPersonalnummer()
        );
    }

    public Admin getAdminByPersonalnummer(int personalnummer) {
        Admin admin = adminRepository.findAdminByPersonalnummer(personalnummer);
        if (admin == null) {
            throw new NotFoundException("Admin mit der Personalnummer \" +  personalnummer + \" nicht gefunden");
        }
        return admin;
    }

    public boolean login(AdminLoginDTO dto){
        Admin admin = adminRepository.findAdminByPersonalnummer(
                dto.personalnummer()
        );
        if(admin == null){
            throw new NotFoundException("Admin mit der Personalnummer \" +  personalnummer + \" nicht gefunden");
        }

        return passwordEncoder.matches(
                dto.passwort(), // eingegebenes Passwort
                admin.getPasswort() // gespeicherter Hash
        );
    }
}