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
import projekt_tierheim.tierheim.service.ReservierungService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/reservierung")
public class ReservierungController {
    private final ReservierungRepository reservierungRepository;
    private final HundRepository hundRepository;
    private final MitgliedRepository mitgliedRepository;

    private final ReservierungService reservierungService;

    @Autowired
    public ReservierungController(ReservierungRepository reservierungRepository, HundRepository hundRepository, MitgliedRepository mitgliedRepository, ReservierungService reservierungService) {
        this.reservierungRepository = reservierungRepository;
        this.hundRepository = hundRepository;
        this.mitgliedRepository = mitgliedRepository;
        this.reservierungService = reservierungService;
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
        return reservierungService.erstelleReservierung(reservierungDTO);
    }

    @DeleteMapping("/{reservierungId}")
    public Reservierung storniereReservierung(@PathVariable("reservierungId") int reservierungsId, @RequestParam(required = true) String grund) {
        Reservierung reservierung = reservierungRepository.findReservierungById(reservierungsId);
        if(reservierung == null) {
            return null;
        }
        reservierung.setStatus(Reservierungsstatus.STORNIERT);
        reservierung.setStornierungsgrund(grund);
        reservierung.setStorniertAm(LocalDateTime.now());

        return reservierungRepository.saveAndFlush(reservierung);
    }

    @DeleteMapping("/hund/{hundId}/alle")
    public List<Reservierung> storniereAlleReservierung(@PathVariable("hundId") int hundId, @RequestParam(required = true) String grund) {
        List<Reservierung> reservierungen = reservierungRepository.findReservierungByHundAndStatus(hundId, Reservierungsstatus.AKTIV);
        if(reservierungen == null || reservierungen.isEmpty()) {
            return null;
        }
        for(Reservierung r : reservierungen) {
            r.setStatus(Reservierungsstatus.STORNIERT);
            r.setStornierungsgrund(grund);
            r.setStorniertAm(LocalDateTime.now());
        }
        return reservierungRepository.saveAllAndFlush(reservierungen);
    }
}
