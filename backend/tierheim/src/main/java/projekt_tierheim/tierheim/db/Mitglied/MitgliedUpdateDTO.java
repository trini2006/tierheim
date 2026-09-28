package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MitgliedUpdateDTO(
        @NotBlank String passwort,
        @NotNull boolean erfahrung
) {
}
