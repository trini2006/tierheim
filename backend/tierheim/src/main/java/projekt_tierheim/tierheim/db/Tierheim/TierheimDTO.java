package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TierheimDTO(
  @NotBlank String name,
  @NotBlank String strasse,
  @NotBlank String hausnummer,
  @NotBlank String plz,
  @NotBlank String ort,
  @NotBlank String telefonnummer,
  String notfallnummer,
  String link,
  String bild,
  String bannerfarbe,
  @NotNull @Positive int zeitblock,
  @NotNull @Positive int ruhezeit,
  @NotNull @Positive int maxGassi,
  @NotNull @Positive int inGassi
)
{ }
