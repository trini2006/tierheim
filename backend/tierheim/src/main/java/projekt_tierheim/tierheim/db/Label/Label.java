package projekt_tierheim.tierheim.db.Label;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bezeichnung;
    // Farbe fürs Label
    // 0 = normal, 1 = wichtig
    private boolean hinweis;


}
