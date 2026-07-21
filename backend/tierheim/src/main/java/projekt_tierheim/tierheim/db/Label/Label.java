package projekt_tierheim.tierheim.db.Label;

import jakarta.persistence.*;
import projekt_tierheim.tierheim.db.Hund.Hund;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String bezeichnung;
    // Farbe fürs Label
    // 0 = normal, 1 = wichtig
    private boolean hinweis;

    @ManyToMany(mappedBy = "labels")
    private Set<Hund> hunde = new HashSet<>();

    public Label(){}

    public Label(int id, String bezeichnung, boolean hinweis) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.hinweis = hinweis;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public boolean isHinweis() {
        return hinweis;
    }

    public void setHinweis(boolean hinweis) {
        this.hinweis = hinweis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return getId() == label.getId() && isHinweis() == label.isHinweis() && Objects.equals(getBezeichnung(), label.getBezeichnung());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBezeichnung(), isHinweis());
    }
}
