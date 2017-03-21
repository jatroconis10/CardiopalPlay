package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class MedicionEstres extends Model{

    public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static final Finder<Long, MedicionEstres> FINDER = new Finder<>(MedicionEstres.class);

    public enum NivelEstres {
        Bajo,
        Medio,
        Alto;

        private static Map<String, NivelEstres> namesMap = new HashMap<>(3);

        static {
            namesMap.put("1", Bajo);
            namesMap.put("2", Medio);
            namesMap.put("3", Alto);
        }

        @JsonCreator
        public static NivelEstres forValue(String value) {
            return namesMap.get(StringUtils.lowerCase(value));
        }

        @JsonValue
        public String toValue() {
            for (Map.Entry<String, NivelEstres> entry : namesMap.entrySet()) {
                if (entry.getValue() == this)
                    return entry.getKey();
            }

            return null;
        }
    }

    public static Finder<Long, MedicionEstres> find = new Finder<Long, MedicionEstres>(MedicionEstres.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private NivelEstres nivelEstres;

    private Date fecha;

    @ManyToOne(optional = false)
    private Historial historial;

    public MedicionEstres(){

    }

    public MedicionEstres(NivelEstres nivelEstres, Date fecha) {
        this.nivelEstres = nivelEstres;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NivelEstres getNivelEstres() {
        return nivelEstres;
    }

    public void setNivelEstres(NivelEstres nivelEstres) {
        this.nivelEstres = nivelEstres;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    //Metodos auxiliares

    public static MedicionEstres bind(JsonNode j){

        String fechaS = j.findPath("fecha").asText();
        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        NivelEstres nivelEstres = NivelEstres.forValue(j.findPath("nivel").asText());

        return new MedicionEstres(nivelEstres, fecha);

    }
}
