package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminCreateDTO(
        @NotNull @Positive int personalnummer,
        @NotNull String passwort
)
{ }
