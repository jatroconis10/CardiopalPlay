package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
public enum TipoEspecializacion {
    General,
    Cardiologo;

    private static Map<String, TipoEspecializacion> namesMap = new HashMap<>(2);

    static {
        namesMap.put("general", General);
        namesMap.put("cardiologo", Cardiologo);
    }

    @JsonCreator
    public static TipoEspecializacion forValue(String value) {
        return namesMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, TipoEspecializacion> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }
}
