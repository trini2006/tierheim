package projekt_tierheim.tierheim.db.Admin;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminByPersonalnummer(int personalnummer);

    // Selbst definierte Query-Methoden benötigen so eine Annotation,
    // um eine explizit geöffnete Schreib-/ Löschtransaktionen auszuführen
    @Transactional
    void deleteByPersonalnummer(int personalnummer);
}
