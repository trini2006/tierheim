package projekt_tierheim.tierheim.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeiten;
import projekt_tierheim.tierheim.db.GassiGehZeiten.GassiGehZeitenRepository;
import projekt_tierheim.tierheim.db.Hund.HundRepository;
import projekt_tierheim.tierheim.db.Mitglied.MitgliedRepository;
import projekt_tierheim.tierheim.db.Reservierung.ReservierungRepository;
import projekt_tierheim.tierheim.db.Tierheim.TierheimRepository;
import projekt_tierheim.tierheim.rest.AdminController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReservierungServiceTest {
    @MockitoBean
    ReservierungRepository reservierungRepository;
    @MockitoBean
    HundRepository hundRepository;
    @MockitoBean
    MitgliedRepository mitgliedRepository;
    @MockitoBean
    GassiGehZeitenRepository gassiGehZeitenRepository;
    @MockitoBean
    TierheimRepository tierheimRepository;

    ReservierungService reservierungService;

    @BeforeEach
    void setUp() {
        reservierungService = new ReservierungService(
                reservierungRepository,
                hundRepository,
                mitgliedRepository,
                gassiGehZeitenRepository,
                tierheimRepository
        );
    }

    void pruefeDeadline(LocalDate date, String expectedMessage) {
        Exception exception = assertThrows(Exception.class, () -> {
            reservierungService.pruefeDeadline(date);
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void pruefeReservierungVorHeute() {
        pruefeDeadline(LocalDate.now().plusDays(1), "");
    }

    @Test
    void pruefeReservierungNachHeute() {
        pruefeDeadline(LocalDate.now().minusDays(1), "Reservierung darf nicht in der Vergangenheit liegen");
    }

    @Test
    void pruefeReservierungHeute() {
        // Für Tage ohne Zeitraum
        pruefeDeadline(LocalDate.now(), "Es gibt keine Zeiträume für den gewählten Tag");
        //pruefeDeadline(LocalDate.now(), "Die Deadline für heute ist abgelaufen. Die Reservierung muss 3h vor dem ersten Zeitraum erfolgen!");
    }

    @Test
    void pruefeVorlauf() {
        LocalDate datHeute = LocalDate.now();
        LocalDate anfangWoche = datHeute.minusDays(datHeute.getDayOfWeek().get(ChronoField.DAY_OF_WEEK) - 1);
        LocalDate bisVorlauf = anfangWoche.plusDays(20);


    }
}