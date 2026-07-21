package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record MitgliedDTO(
    @NotBlank @Positive int mitgliedsnummer,
    @NotBlank String passwort, // ToDO Passwörter iwie verschlüsseln
    @NotBlank boolean erfahrung
)
{ }
