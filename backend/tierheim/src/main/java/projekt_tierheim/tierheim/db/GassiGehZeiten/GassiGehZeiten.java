package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class GassiGehZeiten {
    enum Tage {
        MO, DI, MI, DO, FR, SA, SO
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Tage tag;
    private LocalTime von;
    private LocalTime bis;
    private int idTierheim;

    public GassiGehZeiten(){}
    public GassiGehZeiten(int idTierheim, LocalTime bis, LocalTime von, Tage tag, int id) {
        this.idTierheim = idTierheim;
        this.bis = bis;
        this.von = von;
        this.tag = tag;
        this.id = id;
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

    public int getIdTierheim() {
        return idTierheim;
    }

    public void setIdTierheim(int idTierheim) {
        this.idTierheim = idTierheim;
    }


}
