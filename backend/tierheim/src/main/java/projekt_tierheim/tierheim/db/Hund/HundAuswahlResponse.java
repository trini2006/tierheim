package projekt_tierheim.tierheim.db.Hund;
import java.util.List;

public record HundAuswahlResponse(
    List<Hund> verfuegbareHunde,
    Hund vorschlag)
{ }
