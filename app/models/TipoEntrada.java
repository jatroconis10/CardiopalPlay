package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
public enum TipoEntrada {
    Examen,
    Tratamiento,
    Diagnostico;

    private static Map<String, TipoEntrada> namesMap = new HashMap<>(3);

    static {
        namesMap.put("examen", Examen);
        namesMap.put("tratamiento", Tratamiento);
        namesMap.put("diagnostico", Diagnostico);
    }

    @JsonCreator
    public static TipoEntrada forValue(String value) {
        return namesMap.get(StringUtils.lowerCase(value));
    }

    @JsonValue
    public String toValue() {
        for (Map.Entry<String, TipoEntrada> entry : namesMap.entrySet()) {
            if (entry.getValue() == this)
                return entry.getKey();
        }

        return null;
    }
}
