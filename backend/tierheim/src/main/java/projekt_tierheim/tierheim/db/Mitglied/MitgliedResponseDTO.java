package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MitgliedResponseDTO(
        @NotNull @Positive int id,
        @NotNull @Positive int mitgliedsnummer,
        @NotNull boolean erfahrung
) {
}
