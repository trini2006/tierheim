package projekt_tierheim.tierheim.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import projekt_tierheim.tierheim.db.Hund.Hund;
import projekt_tierheim.tierheim.db.Hund.HundRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class TaskHundEntsperren {

    private final HundRepository hundRepository;
    @Autowired
    public TaskHundEntsperren(HundRepository hundRepository) {
        this.hundRepository = hundRepository;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Europe/Berlin")
    public void pruefeAbgelaufeneSperren() {
        List<Hund> gesperrteHunde = hundRepository.findAll().stream()
                .filter(h -> h.isIstGesperrt())
                .toList();

        List<Hund> entsperrteHunde = new ArrayList<>();
        for(Hund h :  gesperrteHunde) {
            if(!h.istGesperrtAm(LocalDate.now())) {
                h.setIstGesperrt(false);
                h.setSperrGrund(null);
                h.setGesperrtVon(null);
                h.setGesperrtBis(null);

                entsperrteHunde.add(h);
            }
        }

        if(!entsperrteHunde.isEmpty()) {
            hundRepository.saveAllAndFlush(entsperrteHunde);
        }
    }
}
