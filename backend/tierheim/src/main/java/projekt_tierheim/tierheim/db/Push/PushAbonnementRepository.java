package projekt_tierheim.tierheim.db.Push;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PushAbonnementRepository extends JpaRepository<PushAbonnement, Integer> {
    List<PushAbonnement> findByMitgliedId(Integer integer);

    List<PushAbonnement> findByTierheimId(Integer integer);

    Optional<PushAbonnement> findByEndpoint(@NotBlank String endpoint);
}
