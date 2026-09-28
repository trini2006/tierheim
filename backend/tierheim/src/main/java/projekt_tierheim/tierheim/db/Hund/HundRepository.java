package projekt_tierheim.tierheim.db.Hund;

import org.springframework.data.jpa.repository.JpaRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.util.List;

public interface HundRepository extends JpaRepository<Hund, Integer> {
    Hund findHundById(int testId1);

    List<Hund> findHundByNameIgnoreCase(String testName1);
    List<Hund> findAllByTierheimId(int tierheimId);
}
