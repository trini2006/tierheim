package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.validation.constraints.NotBlank;

public record TierheimStammdatenDTO(
        @NotBlank String name,
        @NotBlank String strasse,
        @NotBlank String hausnummer,
        @NotBlank String plz,
        @NotBlank String ort,
        @NotBlank String telefonnummer,
        String notfallnummer,
        String email,
        String link
)
{ }
