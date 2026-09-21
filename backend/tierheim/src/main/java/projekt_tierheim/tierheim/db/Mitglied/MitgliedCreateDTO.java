package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MitgliedCreateDTO(
        @NotNull @Positive int mitgliedsnummer,
        @NotNull String passwort,
        @NotNull boolean erfahrung
) {
}
