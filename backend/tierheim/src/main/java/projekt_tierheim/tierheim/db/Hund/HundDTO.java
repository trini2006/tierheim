package projekt_tierheim.tierheim.db.Hund;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Zum Bearbeiten eines vorhandenen Hundes ohne Sperrgrund
public record HundDTO(
        @NotBlank String name,
        @NotNull boolean geschlecht,
        @NotNull @Positive int alter,
        @NotBlank String rasse,
        @NotBlank Groesse groesse,
        @NotNull @Positive int gewicht,
        @NotNull boolean erfahrung,
        @NotBlank Strecke strecke)
{ }
