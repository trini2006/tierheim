package projekt_tierheim.tierheim.db.Admin;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int personalnummer;
    private String passwort;

    public Admin(){}

    public Admin(int id, int personalnummer, String passwort) {
        this.id = id;
        this.personalnummer = personalnummer;
        this.passwort = passwort;
    }

    public static Admin convertToAdmin(AdminDTO adminDTO) {
        Admin admin = new Admin();
        admin.setPersonalnummer(adminDTO.personalnummer());
        admin.setPasswort(adminDTO.passwort());
        return admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
