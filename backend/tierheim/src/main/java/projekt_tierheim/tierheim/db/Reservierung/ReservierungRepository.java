package projekt_tierheim.tierheim.db.Reservierung;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservierungRepository extends JpaRepository<Reservierung, Integer> {
    // """ > Java Text Blocks (String-Verkettung)
    // r FROM Reservierung > nicht die Tabelle reservierung ist gemeint, sondern die Entität Reservierung (Java Objekt)
    // r ist ein Platzhaltername > repräsentiert im Query eine einzelne Reservierung
    @Query("""
        SELECT r FROM Reservierung r
            WHERE r.datum BETWEEN :von AND :bis
                AND (:mitgliedId IS NULL OR r.mitglied.id = :mitgliedId)
                AND (:hundId IS NULL OR r.hund.id = :hundId)
                AND (:status IS NULL OR r.status = :status)
    """)
    List<Reservierung> findGefiltert(
      @Param("von") LocalDate von,
      @Param("bis") LocalDate bis,
      @Param("mitgliedId") Integer mitgliedId,
      @Param("hundId") Integer hundId,
      @Param("status") Reservierungsstatus status
    );

    Reservierung findReservierungById(int id);
    List<Reservierung> findReservierungByHundAndStatus(int hundId, Reservierungsstatus reservierungsstatus);
}
