package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record AdminDTO(
        @NotBlank @Positive int personalnummer,
        @NotBlank String passwort // ToDO Passwörter iwie verschlüsseln
)
{ }
