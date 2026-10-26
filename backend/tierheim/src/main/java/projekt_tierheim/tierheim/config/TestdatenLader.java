package projekt_tierheim.tierheim.config;

import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import projekt_tierheim.tierheim.db.Admin.Admin;
import projekt_tierheim.tierheim.db.Admin.AdminRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;
import projekt_tierheim.tierheim.db.Hund.Groesse;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Hund.Strecke;
import projekt_tierheim.tierheim.db.Label.Label;
import projekt_tierheim.tierheim.db.Label.LabelRepository;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Tierheim.Tierheim;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("dev")
public class TestdatenLader implements CommandLineRunner {
    private final TierheimRepository tierheimRepository;
    private final HundRepository hundRepository;
    private final AdminRepository adminRepository;
    private final GassiGehZeitenRepository gassiGehZeitenRepository;
    private final LabelRepository labelRepository;
    private final MitgliedRepository mitgliedRepository;
    private final PasswordEncoder passwordEncoder =
            Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    public TestdatenLader(TierheimRepository tierheimRepository, HundRepository hundRepository, AdminRepository adminRepository, GassiGehZeitenRepository gassiGehZeitenRepository, LabelRepository labelRepository, MitgliedRepository mitgliedRepository) {
        this.tierheimRepository = tierheimRepository;
        this.hundRepository = hundRepository;
        this.adminRepository = adminRepository;
        this.gassiGehZeitenRepository = gassiGehZeitenRepository;
        this.labelRepository = labelRepository;
        this.mitgliedRepository = mitgliedRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if(tierheimRepository.count() > 0){
            return; // wenn schon Daten in der Datenbank stehen, wird die Initialisierung übergangen
        }

        Admin admin = legeAdminAn();
        Tierheim tierheim = legeTierheimAn();
        List<GassiGehZeiten> gassiGehZeiten = legeGassiGehZeitenAn(tierheim);
        List<Hund> hunde = legeHundeAn(tierheim);
        List<Label> labels = legeLabelAn();
        List<Mitglied> mitglieder = legeMitgliedAn();
    }

    private Admin legeAdminAn() {

        Admin admin = new Admin(1234567890, passwordEncoder.encode("geheimTest"));
        return adminRepository.save(admin);
    }

    private Tierheim legeTierheimAn() {
        Tierheim t = new  Tierheim (
                "Tierschutzverein Weiden u.U. e.V.",
                "Schustermooslohe",
                "96",
                "92637",
                "Weiden i. d. Oberpfalz",
                "0961 25780"
        );
        return tierheimRepository.save(t);
    }

    // Mo bis Fr 8:00 - 14:30Uhr und Sa 8:00 - 12:00Uhr
    private List<GassiGehZeiten> legeGassiGehZeitenAn(Tierheim tierheim) {
        List<GassiGehZeiten> gassiGehZeiten = new ArrayList<>();
        gassiGehZeiten.add(new GassiGehZeiten(Tage.MO, LocalTime.of(8,0), LocalTime.of(14, 30), tierheim));
        gassiGehZeiten.add(new GassiGehZeiten(Tage.DI, LocalTime.of(8,0), LocalTime.of(14, 30), tierheim));
        gassiGehZeiten.add(new GassiGehZeiten(Tage.MI, LocalTime.of(8,0), LocalTime.of(14, 30), tierheim));
        gassiGehZeiten.add(new GassiGehZeiten(Tage.DO, LocalTime.of(8,0), LocalTime.of(14, 30), tierheim));
        gassiGehZeiten.add(new GassiGehZeiten(Tage.FR, LocalTime.of(8,0), LocalTime.of(14, 30), tierheim));
        gassiGehZeiten.add(new GassiGehZeiten(Tage.SA, LocalTime.of(8,0), LocalTime.of(12, 0), tierheim));
        return gassiGehZeitenRepository.saveAll(gassiGehZeiten);
    }

    private List<Hund> legeHundeAn(Tierheim tierheim) {
        List<Hund> hunde = new ArrayList<Hund>();
        hunde.add(new Hund("Askan", false, 1, "Malinois-Großspitz Mix", Groesse.GROSS, 42, true, Strecke.LANG, tierheim));
        hunde.add(new Hund("Aris", false, 3, "Husky", Groesse.GROSS, 32, false, Strecke.LANG, tierheim));
        hunde.add(new Hund("Richy", false, 4, "Labrador", Groesse.MITTEL, 25, false, Strecke.MITTEL, tierheim));
        return hundRepository.saveAll(hunde);
    }

    // ToDO Überlegung über Hinzufügen von weiteren Parametern wie Abruf, Leinenführigkeit, etc...
    private List<Label> legeLabelAn() {
        List<Label> labels = new ArrayList<>();
        // Anfängergeeignet bzw. ohne "Warnung"
        labels.add(new Label("Freundlich", false));
        labels.add(new Label("Kontaktfreudig", false));
        labels.add(new Label("Ruhig", false));
        labels.add(new Label("Ausgeglichen", false));
        labels.add(new Label("mag Kinder", false));
        labels.add(new Label("Vorsichtig", false));
        labels.add(new Label("Zurückhaltend", false));

        // benötigen Erfahrungslevel bzw. mit "Warnung"
        labels.add(new Label("Ängstlich", true));
        labels.add(new Label("Unsicher", true));
        labels.add(new Label("Aggressionen", true));
        labels.add(new Label("Beißvorfälle", true));
        labels.add(new Label("Geräuschempfindlich", true));
        labels.add(new Label("Fluchtneigung", true));
        labels.add(new Label("Leinenaggression", true));
        labels.add(new Label("Jagdtrieb", true));

        labels.add(new Label("Fahrzeug-Reaktiv", true));
        labels.add(new Label("Jogger-Reaktiv", true));
        labels.add(new Label("Kinder-Reaktiv", true));
        labels.add(new Label("Radfahrer-Reaktiv", true));
        labels.add(new Label("Hunde-Reaktiv", true));

        labels.add(new Label("Männerunsicher", true));
        labels.add(new Label("Frauenunsicher", true));

        return labelRepository.saveAll(labels);
    }

    private List<Mitglied> legeMitgliedAn() {
        List<Mitglied> mitglieder = new ArrayList<>();
        mitglieder.add(new Mitglied(12345, passwordEncoder.encode("geheimMitglied1"), false));
        mitglieder.add(new Mitglied(67890, passwordEncoder.encode("geheimMitglied2"), true));
        return mitgliedRepository.saveAll(mitglieder);
    }

    private void legeReservierungAn() {

    }
}
