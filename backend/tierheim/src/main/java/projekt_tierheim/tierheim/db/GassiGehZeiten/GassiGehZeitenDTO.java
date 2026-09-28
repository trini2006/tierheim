package projekt_tierheim.tierheim.db.GassiGehZeiten;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record GassiGehZeitenDTO(
        @NotNull Tage tag,
        @NotNull LocalTime von,
        @NotNull LocalTime bis
        )
{ }