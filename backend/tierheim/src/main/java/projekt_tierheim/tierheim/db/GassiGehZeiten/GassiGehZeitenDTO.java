package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record GassiGehZeitenDTO(
        // @NotBlank Tage tag, ToDO Enum
        @NotBlank LocalTime von,
        @NotBlank LocalTime bis
        )
{ }
