package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotBlank;

public record AdminPasswortDTO(
        @NotBlank String passwort
)
{ }
