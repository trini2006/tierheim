package projekt_tierheim.tierheim.rest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Mitglied.Mitglied;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungDTO;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Reservierung.Reservierungsstatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/reservierung")
public class ReservierungController {
    private final ReservierungRepository reservierungRepository;
    private final HundRepository hundRepository;
    private final MitgliedRepository mitgliedRepository;

    @Autowired
    public ReservierungController(ReservierungRepository reservierungRepository, HundRepository hundRepository, MitgliedRepository mitgliedRepository) {
        this.reservierungRepository = reservierungRepository;
        this.hundRepository = hundRepository;
        this.mitgliedRepository = mitgliedRepository;
    }

    // Es können nur Aktive, nur Stornierte oder Alle Reservierungen für
    // einen bestimmten Hund
    // ein bestimmtes Mitglied
    // einen bestimmten Zeitraum eingesehen werden
    @GetMapping("/all")
    public List<Reservierung> getAlleReservierung(
            @RequestParam(required = false) LocalDate von,
            @RequestParam(required = false) LocalDate bis,
            @RequestParam(required = false) Integer mitgliedId,
            @RequestParam(required = false) Integer hundId,
            @RequestParam(required = false) Reservierungsstatus status) {
        LocalDate effektivVon = (von != null) ? von : LocalDate.now().minusMonths(1);
        LocalDate effektivBis = (bis != null) ? bis : LocalDate.now().plusMonths(1);

        if(ChronoUnit.MONTHS.between(effektivVon, effektivBis) > 6) {
            throw new IllegalArgumentException("Zeitraum darf maximal 6 Monate umfassen");
        }
        return reservierungRepository.findGefiltert(effektivVon, effektivBis, mitgliedId, hundId, status);
    }

    @PostMapping("/new")
    public Reservierung newReservierung(@Valid @RequestBody ReservierungDTO reservierungDTO) {
        Mitglied mitglied = mitgliedRepository.findMitgliedById(reservierungDTO.mitgliedId());
        Hund hund = hundRepository.findHundById(reservierungDTO.hundId());

        if(mitglied == null || hund == null) {
            return null;
        }

        Reservierung reservierung = new Reservierung();
        reservierung.setMitglied(mitglied);
        reservierung.setHund(hund);
        reservierung.setDatum(reservierungDTO.datum());
        reservierung.setZeitAb(reservierungDTO.zeitAb());
        reservierung.setZeitBis(reservierungDTO.zeitBis());

        return reservierungRepository.saveAndFlush(reservierung);
    }

    @DeleteMapping("/{reservierungId}")
    public void storniereReservierung(@PathVariable("reservierungsId") int reservierungsId) {

    }

    @DeleteMapping("/hund/{hundId}/alle")
    public void storniereAlleReservierung(@PathVariable("hundId") int hundId) {

    }
}
