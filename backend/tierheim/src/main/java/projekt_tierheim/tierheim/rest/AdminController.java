package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminDTO;
import projekt_tierheim.tierheim.db.Admin.AdminRepository;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminController(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    // Mitarbeiter nach Personalnummer zu suchen, macht mehr Sinn, als nach einer zufällig vergebenen id
    @GetMapping("/{personalnummer}")
    public Admin getAdminByPersonalnummer(@PathVariable int personalnummer){
        return adminRepository.findAdminByPersonalnummer(personalnummer);
    }

    @GetMapping("/all")
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    @PostMapping()
    public Admin newAdmin(@RequestBody AdminDTO adminDTO){
        Admin admin = Admin.convertToAdmin(adminDTO);
        return adminRepository.saveAndFlush(admin);
    }

    @PutMapping("/{personalnummer}")
    public Admin updateAdmin(@PathVariable int personalnummer, @RequestBody AdminDTO adminDTO){
        Admin admin = adminRepository.findAdminByPersonalnummer(personalnummer);
        if(admin == null){
            return null;
        }
        admin.setPersonalnummer(adminDTO.personalnummer());
        admin.setPasswort(adminDTO.passwort());
        return adminRepository.saveAndFlush(admin);
    }

    @DeleteMapping("/{personalnummer}")
    public void deleteAdmin(@PathVariable int personalnummer){
        adminRepository.deleteByPersonalnummer(personalnummer);
    }
}
