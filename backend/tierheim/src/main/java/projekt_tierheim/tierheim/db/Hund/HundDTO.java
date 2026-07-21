package projekt_tierheim.tierheim.db.Hund;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

// Zum Bearbeiten eines vorhandenen Hundes ohne Sperrgrund
public record HundDTO(
        @NotBlank String name,
        @NotBlank boolean geschlecht,
        @NotBlank @Positive int alter,
        @NotBlank String rasse,
        //@NotEmpty Groesse groesse, ToDO Enum?!
        @NotBlank @Positive int gewicht,
        @NotBlank boolean erfahrung)
{ }
