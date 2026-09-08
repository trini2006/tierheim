package projekt_tierheim.tierheim.db.GassiGehZeiten;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GassiGehZeitenRepository extends JpaRepository<GassiGehZeiten, Integer> {

    GassiGehZeiten findGassiGehZeitenById(int id);
    List<GassiGehZeiten> findGassiGehZeitenByTag(Tage tag);
}
