package projekt_tierheim.tierheim.db.Push;

import jakarta.validation.constraints.NotBlank;

public record PushEntfernenDTO(@NotBlank String endpoint) {
}
