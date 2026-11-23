package projekt_tierheim.tierheim.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import projekt_tierheim.tierheim.db.Push.PushAbonnement;
import projekt_tierheim.tierheim.db.Push.PushAbonnementRepository;

import java.util.List;

@Component
public class PushListener {
    private final PushAbonnementRepository repository;
    private final PushSender sender;

    public PushListener(PushAbonnementRepository repository, PushSender sender) {
        this.repository = repository;
        this.sender = sender;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onVersand(PushVersandEvent e) {
        List<PushAbonnement> abos = e.mitgliedId() != null
                ? repository.findByMitgliedId(e.mitgliedId())
                : repository.findByTierheimId(e.tierheimId());
        sender.sende(abos, e.titel(), e.text(), e.url());
    }
}
