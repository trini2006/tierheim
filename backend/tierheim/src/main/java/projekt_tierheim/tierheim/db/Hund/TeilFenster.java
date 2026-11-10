package projekt_tierheim.tierheim.db.Hund;

import java.time.LocalTime;
import java.util.List;

public record TeilFenster(LocalTime von, LocalTime bis, List<Hund> hunde) {}
