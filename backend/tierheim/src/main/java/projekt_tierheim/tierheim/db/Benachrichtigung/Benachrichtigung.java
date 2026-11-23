package projekt_tierheim.tierheim.db.Benachrichtigung;

import jakarta.persistence.*;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.time.LocalDateTime;

@Entity
@Table(name = "benachrichtigung")
public class Benachrichtigung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mitglied mitglied;      // gesetzt, wenn ein Mitglied benachrichtigt wird

    @ManyToOne(fetch = FetchType.LAZY)
    private Tierheim tierheim;      // gesetzt, wenn das Tierheim benachrichtigt wird

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BenachrichtigungsTyp typ;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime erstelltAm = LocalDateTime.now();

    @Column(nullable = false)
    private boolean gelesen = false;

    protected Benachrichtigung() {}

    public int getId() { return id; }
    public BenachrichtigungsTyp getTyp() { return typ; }
    public String getText() { return text; }
    public LocalDateTime getErstelltAm() { return erstelltAm; }

    public boolean isGelesen() { return gelesen; }
    public void setGelesen(boolean gelesen) { this.gelesen = gelesen; }

    public static Benachrichtigung fuerMitglied(Mitglied mitglied, BenachrichtigungsTyp typ, String text) {
        Benachrichtigung benachrichtigung = new Benachrichtigung();
        benachrichtigung.mitglied = mitglied;
        benachrichtigung.typ = typ;
        benachrichtigung.text = text;
        return benachrichtigung;
    }

    public static Benachrichtigung fuerTierheim(Tierheim tierheim, BenachrichtigungsTyp typ, String text) {
        Benachrichtigung benachrichtigung = new Benachrichtigung();
        benachrichtigung.tierheim = tierheim;
        benachrichtigung.typ = typ;
        benachrichtigung.text = text;
        return benachrichtigung;
    }
}
