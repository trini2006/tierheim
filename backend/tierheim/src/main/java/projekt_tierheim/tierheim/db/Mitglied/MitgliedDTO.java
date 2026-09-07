package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// ToDO evtl. Neu und Update DTO eigenständig
public record MitgliedDTO(
        @Positive @NotBlank int mitgliedsnummer,
        @NotBlank String passwort,
        @NotNull boolean erfahrung
)
{ }
