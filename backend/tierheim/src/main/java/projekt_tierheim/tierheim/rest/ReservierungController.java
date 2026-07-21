package projekt_tierheim.tierheim.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;

@RestController
@RequestMapping("/reservierung")
public class ReservierungController {
    private final ReservierungRepository reservierungRepository;

    @Autowired
    public ReservierungController(ReservierungRepository reservierungRepository) {
        this.reservierungRepository = reservierungRepository;
    }
}
