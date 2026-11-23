package projekt_tierheim.tierheim.db.Push;

import jakarta.persistence.*;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;

import java.time.LocalDateTime;

@Entity
@Table(name = "push_abonnement")
public class PushAbonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Mitglied mitglied;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tierheim tierheim;

    @Column(nullable = false, unique = true, length = 1000)
    private String endpoint;

    @Column(nullable = false, length = 200)
    private String p256dh;

    @Column(nullable = false, length = 100)
    private String auth;

    private LocalDateTime erstelltAm = LocalDateTime.now();

    protected PushAbonnement() {}
    public PushAbonnement(String endpoint) { this.endpoint = endpoint; }

    public void setzeEmpfaenger(Mitglied m, Tierheim t) { this.mitglied = m; this.tierheim = t; }
    public void setzeSchluessel(String p256dh, String auth) { this.p256dh = p256dh; this.auth = auth; }

    public String getEndpoint() { return endpoint; }
    public String getP256dh() { return p256dh; }
    public String getAuth() { return auth; }
}
