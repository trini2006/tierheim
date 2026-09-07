package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.validation.constraints.Positive;

public record TierheimGassiEinstellungenDTO(
        @Positive int zeitblock,
        @Positive int ruhezeit,
        @Positive int maxGassi,
        @Positive int minGassi
)
{ }
