package projekt_tierheim.tierheim.db.Reservierung;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservierungDTO(
        @NotBlank LocalDate datum,
        @NotBlank LocalTime zeitAb,
        @NotBlank LocalTime zeitBis
        )
{ }
