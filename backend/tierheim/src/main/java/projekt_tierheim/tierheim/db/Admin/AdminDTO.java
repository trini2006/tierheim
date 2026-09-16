package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminDTO(
        @NotNull @Positive int personalnummer,
        @NotNull String passwort    // Erstmal nur für die Tests noch im normalen DTO
)
{ }
