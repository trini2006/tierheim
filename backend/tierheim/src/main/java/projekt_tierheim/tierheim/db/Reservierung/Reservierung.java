package projekt_tierheim.tierheim.db.Reservierung;

import jakarta.persistence.*;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Reservierung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservierung;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mitglied mitglied;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hund hund;

    private LocalDate datum;
    private LocalTime zeitAb;
    private LocalTime zeitBis;

    public Reservierung() {}

    public Reservierung(int idReservierung, Mitglied mitglied, Hund hund, LocalDate datum, LocalTime zeitAb, LocalTime zeitBis) {
        this.idReservierung = idReservierung;
        this.mitglied = mitglied;
        this.hund = hund;
        this.datum = datum;
        this.zeitAb = zeitAb;
        this.zeitBis = zeitBis;
    }

    public int getIdReservierung() {
        return idReservierung;
    }

    public void setIdReservierung(int idReservierung) {
        this.idReservierung = idReservierung;
    }

    public Mitglied getMitglied() {
        return mitglied;
    }

    public void setMitglied(Mitglied mitglied) {
        this.mitglied = mitglied;
    }

    public Hund getHund() {
        return hund;
    }

    public void setHund(Hund hund) {
        this.hund = hund;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public LocalTime getZeitAb() {
        return zeitAb;
    }

    public void setZeitAb(LocalTime zeitAb) {
        this.zeitAb = zeitAb;
    }

    public LocalTime getZeitBis() {
        return zeitBis;
    }

    public void setZeitBis(LocalTime zeitBis) {
        this.zeitBis = zeitBis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservierung that = (Reservierung) o;
        return idReservierung == that.idReservierung && Objects.equals(mitglied, that.mitglied) && Objects.equals(hund, that.hund) && Objects.equals(datum, that.datum) && Objects.equals(zeitAb, that.zeitAb) && Objects.equals(zeitBis, that.zeitBis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservierung, mitglied, hund, datum, zeitAb, zeitBis);
    }
}
