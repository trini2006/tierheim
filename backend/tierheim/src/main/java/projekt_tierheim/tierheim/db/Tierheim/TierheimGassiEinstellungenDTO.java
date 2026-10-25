package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;

public record TierheimGassiEinstellungenDTO(
        @Positive int zeitblock,
        @Positive int ruhezeit,
        @Positive int maxGassi,
        @Positive int minGassi
)
{
    @AssertTrue(message = "Der Zeitblock muss 30, 45 oder 60 Minuten betragen")
    public boolean isZeitblockGueltig() {
        return zeitblock == 30 || zeitblock == 45 || zeitblock == 60;
    }
}
