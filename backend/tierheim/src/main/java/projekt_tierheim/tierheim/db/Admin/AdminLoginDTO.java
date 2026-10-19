package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminLoginDTO (
        @NotNull @Positive Integer personalnummer,
        @NotBlank String passwort)
{ }
