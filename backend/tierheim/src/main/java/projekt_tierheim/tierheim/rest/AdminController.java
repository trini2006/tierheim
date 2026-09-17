package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Admin.*;
import projekt_tierheim.tierheim.exception.NotFoundException;
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
        Admin admin = adminService.getAdminByPersonalnummer(personalnummer);
        if(admin == null){
            throw new NotFoundException("Admin mit der Personalnummer " +  personalnummer + " nicht gefunden");
        }
        return admin;
    }

    @GetMapping("/all")
    public List<Admin> getAllAdmins(){
        return adminRepository.findAll();
    }

    @PostMapping()
    public Admin newAdmin(@Valid @RequestBody AdminCreateDTO adminDTO){
        return adminService.createAdmin(adminDTO);
    }

    @PutMapping("/{personalnummer}")
    public AdminResponseDTO updateAdmin(
            @PathVariable("personalnummer") int personalnummer,
            @Valid @RequestBody AdminPasswortDTO adminDTO) {

        return adminService.updateAdmin(personalnummer, adminDTO);
    }

    @DeleteMapping("/{personalnummer}")
    public void deleteAdmin(@PathVariable("personalnummer") int personalnummer){
        if(!adminRepository.existsByPersonalnummer(personalnummer)) {
            throw new NotFoundException("Admin mit der Personalnummer " +  personalnummer + " nicht gefunden");
        }
        adminRepository.deleteByPersonalnummer(personalnummer);
    }
}
