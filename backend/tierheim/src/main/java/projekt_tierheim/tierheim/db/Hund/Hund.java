package projekt_tierheim.tierheim.db.Hund;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Hund {
    // Enum
    enum Strecke {
        KURZ,
        MITTEL,
        LANG
    }
    enum Groesse {
        KLEIN,
        MITTEL,
        GROSS
    }

    // Attribute für alle Hunde
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private boolean geschlecht; // 0 = Rüde, 1 = Hündin
    private int alter;
    private String rasse;
    private Groesse groesse;
    private int gewicht; // z.B. 20kg
    private boolean erfahrung; // 0 = Anfängerhund, 1 = Problemhund
    private Strecke strecke;

    // Attribute zum Sperren von Hunden
    private LocalDate gesperrtVon;
    private LocalDate gesperrtBis;
    private boolean istGesperrt; // 0 = ja, 1 = nein
    private String sperrGrund;

    // Log-Daten für z.B. Debugging
    private LocalDateTime erstelltAm;
    private int erstelltVon; // Id eines Mitarbeiters

    // DTO
    public Hund() {}

    // Hund ohne Sperrgrund
    public Hund(int id, String name, boolean geschlecht, int alter, String rasse, Groesse groesse, int gewicht, boolean erfahrung, Strecke strecke) {
        this.id = id;
        this.name = name;
        this.geschlecht = geschlecht;
        this.alter = alter;
        this.rasse = rasse;
        this.groesse = groesse;
        this.gewicht = gewicht;
        this.erfahrung = erfahrung;
        this.strecke = strecke;
    }

    // Hund mit Sperrgrund
    public Hund(int id, String name, boolean geschlecht, int alter, String rasse, Groesse groesse, int gewicht, boolean erfahrung, Strecke strecke, LocalDate gesperrtVon, LocalDate gesperrtBis, boolean istGesperrt, String sperrGrund, LocalDateTime erstelltAm, int erstelltVon) {
        this.id = id;
        this.name = name;
        this.geschlecht = geschlecht;
        this.alter = alter;
        this.rasse = rasse;
        this.groesse = groesse;
        this.gewicht = gewicht;
        this.erfahrung = erfahrung;
        this.strecke = strecke;
        this.gesperrtVon = gesperrtVon;
        this.gesperrtBis = gesperrtBis;
        this.istGesperrt = istGesperrt;
        this.sperrGrund = sperrGrund;
        this.erstelltAm = erstelltAm;
        this.erstelltVon = erstelltVon;
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

    public boolean isGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(boolean geschlecht) {
        this.geschlecht = geschlecht;
    }

    public int getAlter() {
        return alter;
    }

    public void setAlter(int alter) {
        this.alter = alter;
    }

    public String getRasse() {
        return rasse;
    }

    public void setRasse(String rasse) {
        this.rasse = rasse;
    }

    public Groesse getGroesse() {
        return groesse;
    }

    public void setGroesse(Groesse groesse) {
        this.groesse = groesse;
    }

    public int getGewicht() {
        return gewicht;
    }

    public void setGewicht(int gewicht) {
        this.gewicht = gewicht;
    }

    public boolean isErfahrung() {
        return erfahrung;
    }

    public void setErfahrung(boolean erfahrung) {
        this.erfahrung = erfahrung;
    }

    public Strecke getStrecke() {
        return strecke;
    }

    public void setStrecke(Strecke strecke) {
        this.strecke = strecke;
    }

    public LocalDate getGesperrtVon() {
        return gesperrtVon;
    }

    public void setGesperrtVon(LocalDate gesperrtVon) {
        this.gesperrtVon = gesperrtVon;
    }

    public LocalDate getGesperrtBis() {
        return gesperrtBis;
    }

    public void setGesperrtBis(LocalDate gesperrtBis) {
        this.gesperrtBis = gesperrtBis;
    }

    public boolean isIstGesperrt() {
        return istGesperrt;
    }

    public void setIstGesperrt(boolean istGesperrt) {
        this.istGesperrt = istGesperrt;
    }

    public String getSperrGrund() {
        return sperrGrund;
    }

    public void setSperrGrund(String sperrGrund) {
        this.sperrGrund = sperrGrund;
    }

    public LocalDateTime getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(LocalDateTime erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public int getErstelltVon() {
        return erstelltVon;
    }

    public void setErstelltVon(int erstelltVon) {
        this.erstelltVon = erstelltVon;
    }
}