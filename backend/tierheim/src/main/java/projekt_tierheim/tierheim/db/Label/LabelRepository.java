package projekt_tierheim.tierheim.db.Label;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    Label findLabelById(int id);

    List<Label> findLabelByBezeichnungIgnoreCase(String bezeichnung);
}
