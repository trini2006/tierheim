package projekt_tierheim.tierheim.db.Reservierung;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservierungDTO(
        @NotNull LocalDate datum,
        @NotNull LocalTime zeitAb,
        @NotNull LocalTime zeitBis
        )
{ }
