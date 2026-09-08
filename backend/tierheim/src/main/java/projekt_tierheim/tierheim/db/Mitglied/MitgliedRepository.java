package projekt_tierheim.tierheim.db.Mitglied;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MitgliedRepository extends JpaRepository<Mitglied, Integer> {
    Mitglied findMitgliedByMitgliedsnummer(int mitgliedsnummer);

    Mitglied findMitgliedById(int testId1);
}
