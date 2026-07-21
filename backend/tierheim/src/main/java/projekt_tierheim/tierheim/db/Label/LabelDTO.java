package projekt_tierheim.tierheim.db.Label;

import jakarta.validation.constraints.NotBlank;

public record LabelDTO(
  @NotBlank String bezeichnung,
  @NotBlank boolean hinweis
)
{ }
