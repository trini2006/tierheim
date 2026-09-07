package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import projekt_tierheim.tierheim.db.Reservierung.Reservierung;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/reservierung")
public class ReservierungController {
    private final ReservierungRepository reservierungRepository;

    @Autowired
    public ReservierungController(ReservierungRepository reservierungRepository) {
        this.reservierungRepository = reservierungRepository;
    }

    @GetMapping("/all")
    public List<Reservierung> getAlleReservierung(
            @RequestParam(required = false) LocalDate von,
            @RequestParam(required = false) LocalDate bis,
            @RequestParam(required = false) Integer mitgliedId,
            @RequestParam(required = false) Integer hundId) {
        LocalDate effektivVon = (von != null) ? von : LocalDate.now().minusMonths(1);
        LocalDate effektivBis = (bis != null) ? bis : LocalDate.now().plusMonths(1);

        if(ChronoUnit.MONTHS.between(effektivVon, effektivBis) > 6) {
            throw new IllegalArgumentException("Zeitraum darf maximal 6 Monate umfassen");
        }

        return reservierungRepository.findGefiltert(effektivVon, effektivBis, mitgliedId, hundId);
    }

    @PostMapping("/new")
    public Reservierung newReservierung() {
        return null;
    }

    @DeleteMapping("/{reservierungId}")
    public void deleteReservierung() {

    }

}
