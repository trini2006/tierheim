package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalTime;


@Entity
public class GassiGehZeiten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String tag; // ToDo: Enum erstellen
    private LocalTime von;
    private LocalTime bis;
    private int idTierheim;


}
