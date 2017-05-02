package utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import models.Medico;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ja.troconis10 on 01/05/2017.
 */
public class MedicoBuilder {

    public enum TipoEspecializacion {
        GENERAL,
        CARDIOLOGO;

        private static Map<String, TipoEspecializacion> namesMap = new HashMap<>(2);

        static {
            namesMap.put("general", GENERAL);
            namesMap.put("cardiologo", CARDIOLOGO);
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

    public static Medico buildMedico(JsonNode j){

        TipoEspecializacion especializacion = TipoEspecializacion.forValue(j.findPath("tipo").asText());
        Medico mNuevo = crearMedico(j);

        switch (especializacion){
            case GENERAL:
               return crearMedicoGeneral(mNuevo);
            case CARDIOLOGO:
                return crearMedicoCardiologo(mNuevo);
            default:
                return null;
        }

    }

    private static Medico crearMedico(JsonNode j){
        String nombres = j.findPath("nombres").asText();
        String apellidos= j.findPath("apellidos").asText();

        return new Medico(nombres, apellidos);
    }

    private static Medico crearMedicoGeneral(Medico mNuevo){
        mNuevo.setEspecializacion(TipoEspecializacion.GENERAL);
        return mNuevo;
    }

    private static Medico crearMedicoCardiologo(Medico mNuevo){
        mNuevo.setPermMarcapasos(true);
        mNuevo.setEspecializacion(TipoEspecializacion.CARDIOLOGO);
        return mNuevo;
    }
}
