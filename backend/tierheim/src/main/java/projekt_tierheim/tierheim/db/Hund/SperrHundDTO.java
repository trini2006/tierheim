package projekt_tierheim.tierheim.db.Hund;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SperrHundDTO(
        @NotNull LocalDate gesperrtVon,
        @NotNull LocalDate gesperrtBis,
        String sperrGrund)
{ }
