package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MitgliedLoginDTO(
        @NotNull @Positive Integer username,
        @NotNull String passwort
) {
}
