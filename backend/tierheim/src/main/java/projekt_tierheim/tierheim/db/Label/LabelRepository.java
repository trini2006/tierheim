package projekt_tierheim.tierheim.db.Label;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Integer> {
    Label findLabelById(int id);
}
