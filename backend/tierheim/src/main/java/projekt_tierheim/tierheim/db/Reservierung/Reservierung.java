package projekt_tierheim.tierheim.db.Reservierung;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class Reservierung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReservierung;
    private int idMitglied;
    private int idHund;
    private LocalDate datum;
    private LocalTime zeitAb;
    private LocalTime zeitBis;

    public Reservierung(){

    }

    public Reservierung(int idReservierung, int idMitglied, int idHund, LocalDate datum, LocalTime zeitAb, LocalTime zeitBis) {
        this.idReservierung = idReservierung;
        this.idMitglied = idMitglied;
        this.idHund = idHund;
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

    public int getIdMitglied() {
        return idMitglied;
    }

    public void setIdMitglied(int idMitglied) {
        this.idMitglied = idMitglied;
    }

    public int getIdHund() {
        return idHund;
    }

    public void setIdHund(int idHund) {
        this.idHund = idHund;
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
        if (o == null || getClass() != o.getClass()) return false;
        Reservierung that = (Reservierung) o;
        return idReservierung == that.idReservierung && idMitglied == that.idMitglied && idHund == that.idHund && Objects.equals(datum, that.datum) && Objects.equals(zeitAb, that.zeitAb) && Objects.equals(zeitBis, that.zeitBis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReservierung, idMitglied, idHund, datum, zeitAb, zeitBis);
    }
}
