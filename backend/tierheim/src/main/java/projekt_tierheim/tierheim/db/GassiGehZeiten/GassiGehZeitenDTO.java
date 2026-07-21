package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record GassiGehZeitenDTO(
        @NotBlank Tage tag,
        @NotNull LocalTime von,
        @NotNull LocalTime bis
        )
{ }