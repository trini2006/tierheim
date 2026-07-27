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

    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable int id){
        return adminRepository.findAdminById(id);
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
}
