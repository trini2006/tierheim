package projekt_tierheim.tierheim.db.Mitglied;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MitgliedUpdateDTO(
        @NotNull boolean erfahrung
)
{ }
