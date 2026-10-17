package projekt_tierheim.tierheim.db.GassiGehZeiten;

import java.time.DayOfWeek;

public enum Tage {
    MO, DI, MI, DO, FR, SA, SO;

    public static Tage von(DayOfWeek tag) {
        return switch (tag) {
            case MONDAY -> MO;
            case TUESDAY -> DI;
            case WEDNESDAY -> MI;
            case THURSDAY -> DO;
            case FRIDAY -> FR;
            case SATURDAY -> SA;
            case SUNDAY -> SO;
        };
    }
}
