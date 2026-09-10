package projekt_tierheim.tierheim.service;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminDTO;
import projekt_tierheim.tierheim.db.Admin.AdminRepository;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin createAdmin(AdminDTO adminDTO) {

        Admin admin = new Admin();

        admin.setPersonalnummer(adminDTO.personalnummer());

        // Passwort hashen
        String hash = passwordEncoder.encode(adminDTO.passwort());

        admin.setPasswort(hash);

        return adminRepository.saveAndFlush(admin);
    }

    public Admin updateAdmin(int personalnummer, AdminDTO adminDTO) {

        Admin admin = adminRepository.findAdminByPersonalnummer(personalnummer);

        if (admin == null) {
            return null;
        }

        admin.setPersonalnummer(adminDTO.personalnummer());

        // Neues Passwort hashen
        String hash = passwordEncoder.encode(adminDTO.passwort());

        admin.setPasswort(hash);

        return adminRepository.saveAndFlush(admin);
    }

    public Admin getAdminByPersonalnummer(int personalnummer) {
        return adminRepository.findAdminByPersonalnummer(personalnummer);
    }
}