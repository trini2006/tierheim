package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Mitglied {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int mitgliedsnummer;
    private String passwort;
    private boolean erfahrung; // 0 = Gruen, 1 = Gelb/Orange

    public Mitglied() {
    }

    public Mitglied(int id, int mitgliedsnummer, String passwort, boolean erfahrung) {
        this.id = id;
        this.mitgliedsnummer = mitgliedsnummer;
        this.passwort = passwort;
        this.erfahrung = erfahrung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isErfahrung() {
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
