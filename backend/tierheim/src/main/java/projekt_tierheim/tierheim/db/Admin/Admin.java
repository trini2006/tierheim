package projekt_tierheim.tierheim.db.Admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "admin")
public class Admin {

    // ToDO Benötigt Tierheim, dem ein Admin zugeordnet ist
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private int personalnummer;
    @JsonIgnore
    private String passwort;

    public Admin(){}

    public Admin(int personalnummer, String passwort) {
        this.personalnummer = personalnummer;
        this.passwort = passwort;
    }

    public Admin(int i, int i1, String geheim123) {
    }

    public int getId() {
        return id;
    }

    public int getPersonalnummer() {
        return personalnummer;
    }
    public void setPersonalnummer(int personalnummer) {
        this.personalnummer = personalnummer;
    }

    public String getPasswort() {
        return passwort;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return id == admin.id && personalnummer == admin.personalnummer && Objects.equals(passwort, admin.passwort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personalnummer, passwort);
    }
}
