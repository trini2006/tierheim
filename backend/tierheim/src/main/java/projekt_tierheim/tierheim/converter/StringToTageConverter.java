package projekt_tierheim.tierheim.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import projekt_tierheim.tierheim.db.GassiGehZeiten.Tage;

@Component
public class StringToTageConverter implements Converter <String, Tage> {
    @Override
    public Tage convert(String source) {
        return Tage.valueOf(source.trim().toUpperCase());
    }
}
