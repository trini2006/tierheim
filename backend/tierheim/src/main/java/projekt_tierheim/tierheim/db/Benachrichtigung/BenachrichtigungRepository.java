package projekt_tierheim.tierheim.db.Benachrichtigung;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BenachrichtigungRepository extends JpaRepository<Benachrichtigung, Integer> {

    Collection<Object> findByMitgliedIdOrderByErstelltAmDesc(int mitgliedId);

    Collection<Object> findByTierheimIdOrderByErstelltAmDesc(int tierheimId);

    long countByMitgliedIdAndGelesenFalse(int mitgliedId);

    long countByTierheimIdAndGelesenFalse(int tierheimId);
}
