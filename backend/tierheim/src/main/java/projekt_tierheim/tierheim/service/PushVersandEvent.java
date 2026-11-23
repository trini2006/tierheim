package projekt_tierheim.tierheim.service;

public record PushVersandEvent(
        Integer mitgliedId,
        Integer tierheimId,
        String titel,
        String text,
        String url)
{ }
