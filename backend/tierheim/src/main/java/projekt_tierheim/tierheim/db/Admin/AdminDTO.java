package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminDTO(
        @NotNull @Positive int personalnummer,
        @NotBlank String passwort // ToDO Passwörter iwie verschlüsseln
)
{ }
