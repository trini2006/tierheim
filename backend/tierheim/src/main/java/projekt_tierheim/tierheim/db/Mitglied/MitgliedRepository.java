package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MitgliedRepository extends JpaRepository<Mitglied, Integer> {
    Mitglied findMitgliedByMitgliedsnummer(int mitgliedsnummer);

    Mitglied findMitgliedById(int testId1);

    boolean existsByMitgliedsnummer(int mitgliedsnummer);

    @Transactional
    void deleteByMitgliedsnummer(int mitgliedsnummer);
}
