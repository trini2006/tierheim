package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminDTO;
import projekt_tierheim.tierheim.db.Admin.AdminRepository;
import projekt_tierheim.tierheim.service.AdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminRepository adminRepository;
    private final AdminService adminService;

    @Autowired
    public AdminController(
            AdminRepository adminRepository,
            AdminService adminService) {

        this.adminRepository = adminRepository;
        this.adminService = adminService;
    }

    // Mitarbeiter nach Personalnummer zu suchen, macht mehr Sinn, als nach einer zufällig vergebenen id
    @GetMapping("/{personalnummer}")
    public Admin getAdminByPersonalnummer(@PathVariable("personalnummer") int personalnummer){
        return adminService.getAdminByPersonalnummer(personalnummer);
    }

    @GetMapping("/all")
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    @PostMapping()
    public Admin newAdmin(@Valid @RequestBody AdminDTO adminDTO){
        return adminService.createAdmin(adminDTO);
    }

    @PutMapping("/{personalnummer}")
    public Admin updateAdmin(
            @PathVariable("personalnummer") int personalnummer,
            @Valid @RequestBody AdminDTO adminDTO) {

        return adminService.updateAdmin(personalnummer, adminDTO);
    }

    @DeleteMapping("/{personalnummer}")
    public void deleteAdmin(@PathVariable("personalnummer") int personalnummer){
        adminRepository.deleteByPersonalnummer(personalnummer);
    }
}
