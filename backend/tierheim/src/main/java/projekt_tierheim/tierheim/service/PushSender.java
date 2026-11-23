package projekt_tierheim.tierheim.service;


import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import projekt_tierheim.tierheim.db.Push.PushAbonnement;
import projekt_tierheim.tierheim.db.Push.PushAbonnementRepository;
import tools.jackson.databind.ObjectMapper;

import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.List;
import java.util.Map;

@Service
public class PushSender {
    private static final Logger log = LoggerFactory.getLogger(PushSender.class);

    private final PushAbonnementRepository repository;
    private final ObjectMapper objectMapper;
    private final String publicKey;
    private PushService pushService;   // null = Push nicht konfiguriert

    public PushSender(PushAbonnementRepository repository, ObjectMapper objectMapper,
                      @Value("${push.vapid.public-key:}") String publicKey,
                      @Value("${push.vapid.private-key:}") String privateKey,
                      @Value("${push.vapid.subject:mailto:admin@localhost}") String subject) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.publicKey = publicKey;
        if (!publicKey.isBlank() && !privateKey.isBlank()) {
            try {
                if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                    Security.addProvider(new BouncyCastleProvider());
                }
                this.pushService = new PushService(publicKey, privateKey, subject);
            } catch (GeneralSecurityException e) {
                log.error("Push konnte nicht initialisiert werden", e);
            }
        } else {
            log.warn("Push ist deaktiviert: push.vapid.* ist nicht konfiguriert");
        }
    }

    public String getPublicKey() { return pushService != null ? publicKey : ""; }

    public void sende(List<PushAbonnement> abos, String titel, String text, String url) {
        if (pushService == null || abos.isEmpty()) return;
        String payload;
        try {
            payload = objectMapper.writeValueAsString(Map.of("title", titel, "body", text, "url", url));
        } catch (Exception e) {
            log.error("Push-Nachricht konnte nicht erstellt werden", e);
            return;
        }
        for (PushAbonnement a : abos) {
            try {
                HttpResponse response = pushService.send(
                        new Notification(a.getEndpoint(), a.getP256dh(), a.getAuth(), payload));
                int status = (response).getStatusLine().getStatusCode();
                if (status == 404 || status == 410) {
                    repository.delete(a);       // Abo ist abgelaufen oder wurde im Browser widerrufen
                    log.info("Abgelaufenes Push-Abo entfernt");
                } else if (status >= 300) {
                    String antwort = "";
                    try {
                        antwort = org.apache.http.util.EntityUtils.toString(response.getEntity());
                    } catch (Exception ignored) {
                    }
                    log.warn("Push von {} abgelehnt (Status {}): {}",
                            java.net.URI.create(a.getEndpoint()).getHost(), status, antwort);
                }
            } catch (Exception ex) {
                log.warn("Push-Versand fehlgeschlagen", ex);
            }
        }
    }
}
