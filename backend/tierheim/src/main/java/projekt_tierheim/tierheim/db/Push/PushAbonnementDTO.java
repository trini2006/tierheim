package projekt_tierheim.tierheim.db.Push;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.martijndwars.webpush.Subscription;

public record PushAbonnementDTO(@NotBlank String endpoint, @NotNull @Valid Subscription.Keys keys) {
    public record Keys(@NotBlank String p256dh, @NotBlank String auth) {}
}
