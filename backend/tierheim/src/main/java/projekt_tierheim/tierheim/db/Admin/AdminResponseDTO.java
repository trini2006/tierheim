package projekt_tierheim.tierheim.db.Admin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AdminResponseDTO(
        @NotNull @Positive int id,
        @NotNull @Positive int personalnummer
) {
}
