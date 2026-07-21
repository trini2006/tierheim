package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.persistence.*;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.time.LocalTime;

@Entity
public class GassiGehZeiten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalTime von;
    private LocalTime bis;
    @Enumerated(EnumType.STRING)
    private Tage tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tierheim tierheim;

    public GassiGehZeiten(){}

    public GassiGehZeiten(int id, Tage tag, LocalTime von, LocalTime bis, Tierheim tierheim) {
        this.id = id;
        this.tag = tag;
        this.von = von;
        this.bis = bis;
        this.tierheim = tierheim;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tage getTag() {
        return tag;
    }

    public void setTag(Tage tag) {
        this.tag = tag;
    }

    public LocalTime getVon() {
        return von;
    }

    public void setVon(LocalTime von) {
        this.von = von;
    }

    public LocalTime getBis() {
        return bis;
    }

    public void setBis(LocalTime bis) {
        this.bis = bis;
    }

    public Tierheim getTierheim() {
        return tierheim;
    }

    public void setTierheim(Tierheim tierheim) {
        this.tierheim = tierheim;
    }
}
