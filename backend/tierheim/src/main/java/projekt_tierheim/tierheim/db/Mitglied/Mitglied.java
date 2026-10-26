package projekt_tierheim.tierheim.db.Mitglied;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mitglied")
public class Mitglied {

    // ToDO Benötigt Tierheim, dem ein Mitglied zugeordnet ist
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mitgliedsnummer;
    @JsonIgnore
    private String passwort;
    private boolean erfahrung; // false = Gruen, true = Gelb/Orange

    public Mitglied() {
    }

    public Mitglied(int mitgliedsnummer, String passwort, boolean erfahrung) {
        this.mitgliedsnummer = mitgliedsnummer;
        this.passwort = passwort;
        this.erfahrung = erfahrung;
    }

    public int getId() {
        return id;
    }

    public int getMitgliedsnummer() {
        return mitgliedsnummer;
    }
    public void setMitgliedsnummer(int mitgliedsnummer) {
        this.mitgliedsnummer = mitgliedsnummer;
    }

    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public boolean getErfahrung() {
        return erfahrung;
    }
    public void setErfahrung(boolean erfahrung) {
        this.erfahrung = erfahrung;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mitglied mitglied = (Mitglied) o;
        return id == mitglied.id && mitgliedsnummer == mitglied.mitgliedsnummer && erfahrung == mitglied.erfahrung && Objects.equals(passwort, mitglied.passwort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mitgliedsnummer, passwort, erfahrung);
    }
}
