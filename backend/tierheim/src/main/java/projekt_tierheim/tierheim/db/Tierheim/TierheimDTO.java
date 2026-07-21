package projekt_tierheim.tierheim.db.Tierheim;

import jakarta.validation.constraints.NotBlank;
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
  @NotBlank @Positive int zeitblock,
  @NotBlank @Positive int ruhezeit,
  @NotBlank @Positive int maxGassi,
  @NotBlank @Positive int inGassi
)
{ }
