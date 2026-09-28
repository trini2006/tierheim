package projekt_tierheim.tierheim.db.Label;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LabelDTO(
  @NotBlank String bezeichnung,
  @NotNull boolean hinweis
)
{ }
