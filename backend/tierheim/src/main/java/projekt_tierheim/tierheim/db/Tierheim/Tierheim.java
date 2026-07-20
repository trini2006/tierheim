package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Tierheim {
    // Allgemeine Informationen
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;
    private String telefonnummer;
    private String notfallnummer;
    private String link; // z.B. Webseite des Tierheims, Mitgliedsformular

    // Aussehen
    private String bild; // Bildadresse
    private String bannerfarbe;

    // Gassi/ Hunde Einstellungen
    private int zeitblock; // 30min, 45min, 60min > Auswahl der gewünschten Zeit
    private int ruhezeit; // Pause, die ein Hund zwingend zwischen Spaziergängen haben muss
    private int maxGassi; // maximale Zeit, die ein Hund am Stück spazieren gehen darf
    private int minGassi; // Zeit, die ein Hund mindestens spazieren gehen muss

    public Tierheim() {}

    public Tierheim(int id, String name, String strasse, String hausnummer, String plz, String ort, String telefonnummer, String notfallnummer, String link, String bild, String bannerfarbe, int zeitblock, int ruhezeit, int maxGassi, int minGassi) {
        this.id = id;
        this.name = name;
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.plz = plz;
        this.ort = ort;
        this.telefonnummer = telefonnummer;
        this.notfallnummer = notfallnummer;
        this.link = link;
        this.bild = bild;
        this.bannerfarbe = bannerfarbe;
        this.zeitblock = zeitblock;
        this.ruhezeit = ruhezeit;
        this.maxGassi = maxGassi;
        this.minGassi = minGassi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public String getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(String hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getOrt() {
        return ort;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getNotfallnummer() {
        return notfallnummer;
    }

    public void setNotfallnummer(String notfallnummer) {
        this.notfallnummer = notfallnummer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBild() {
        return bild;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }

    public String getBannerfarbe() {
        return bannerfarbe;
    }

    public void setBannerfarbe(String bannerfarbe) {
        this.bannerfarbe = bannerfarbe;
    }

    public int getZeitblock() {
        return zeitblock;
    }

    public void setZeitblock(int zeitblock) {
        this.zeitblock = zeitblock;
    }

    public int getRuhezeit() {
        return ruhezeit;
    }

    public void setRuhezeit(int ruhezeit) {
        this.ruhezeit = ruhezeit;
    }

    public int getMaxGassi() {
        return maxGassi;
    }

    public void setMaxGassi(int maxGassi) {
        this.maxGassi = maxGassi;
    }

    public int getMinGassi() {
        return minGassi;
    }

    public void setMinGassi(int minGassi) {
        this.minGassi = minGassi;
    }
}
