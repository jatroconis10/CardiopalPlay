package utils;

import com.fasterxml.jackson.databind.JsonNode;
import models.MedicionEstres;
import models.MedicionFrecuencia;
import models.MedicionPresion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ja.troconis10 on 01/05/2017.
 */
public class MedicionFactory {

    private static final Logger LOGGER = Logger.getLogger(MedicionFactory.class.getName());

    private static final String ERROR_FECHA = "Error parsing date";

    public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    /**
     * Crea una medidcion de frecuencia a partir de un nooo de JSON
     * @param j  nodo de JSON para construir la medicion
     * @return nueva medicion
     */
    public static MedicionFrecuencia createMedFrec(JsonNode j){

        String fechaS = j.findPath("fecha").asText();

        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, ERROR_FECHA);
        }

        int frec = j.findPath("latidosPMin").asInt();

        return new MedicionFrecuencia(fecha, frec);
    }

    /**
     * Crea una medidcion de estres a partir de un nooo de JSON
     * @param j  nodo de JSON para construir la medicion
     * @return nueva medicion
     */
    public static MedicionEstres createMedEstres(JsonNode j){
        String fechaS = j.findPath("fecha").asText();
        String nvEstres = j.findPath("nivel").asText();


        MedicionEstres.NivelEstres nivelEstres = MedicionEstres.NivelEstres.forValue(nvEstres);

        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, ERROR_FECHA);
        }


        return new MedicionEstres(nivelEstres, fecha);
    }

    /**
     * Crea una medidcion de estres a partir de un nooo de JSON
     * @param j  nodo de JSON para construir la medicion
     * @return nueva medicion
     */
    public static MedicionPresion createMedPresion(JsonNode j) {
        String fechaS = j.findPath("fecha").asText();

        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, ERROR_FECHA);
        }

        int presionDiastolica = j.findPath("presionD").asInt();

        int presionSistolica = j.findPath("presionS").asInt();

        return new MedicionPresion(presionDiastolica, presionSistolica, fecha);
    }
}
