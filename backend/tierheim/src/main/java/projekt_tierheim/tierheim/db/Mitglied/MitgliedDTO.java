package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MitgliedDTO(
    @NotNull @Positive int mitgliedsnummer,
    @NotBlank String passwort, // ToDO Passwörter iwie verschlüsseln
    @NotNull boolean erfahrung
)
{ }
