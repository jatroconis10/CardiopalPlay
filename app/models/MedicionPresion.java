package models;
/**
 * Created by ja.troconis10 on 25/02/2017.
 */

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class MedicionPresion extends Model {

    public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final Finder<Long, MedicionPresion> FINDER = new Finder<>(MedicionPresion.class);

    public static final String NORMAL = "NORMAL";

    public static final String PREHIPERTENSION = "PREHIPERTENSION";

    public static final String HIPERTENSION_SIS_ALTA = "HIPERTENSION_SIS_ALTA";

    public static final String ALTA = "HIPERTENSION_SIS_ALTA";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int presionDiastolica;

    private int presionSistolica;

    private Date fecha;

    private String estado;

    @ManyToOne(optional = false)
    private Historial historial;

    public MedicionPresion(){

    }

    public MedicionPresion(int presionDiastolica, int presionSistolica, Date fecha) {
        this.presionDiastolica = presionDiastolica;
        this.presionSistolica = presionSistolica;
        this.fecha = fecha;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the presionDiastolica
     */
    public int getPresionDiastolica() {
        return presionDiastolica;
    }

    /**
     * @param presionDiastolica the presionDiastolica to set
     */
    public void setPresionDiastolica(int presionDiastolica) {
        this.presionDiastolica = presionDiastolica;
    }

    /**
     * @return the presionSistolica
     */
    public int getPresionSistolica() {
        return presionSistolica;
    }

    /**
     * @param presionSistolica the presionSistolica to set
     */
    public void setPresionSistolica(int presionSistolica) {
        this.presionSistolica = presionSistolica;
    }

    //Metodos auxiliares

    public static MedicionPresion bind(JsonNode j){

        String fechaS = j.findPath("fecha").asText();
        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int presionDiastolica = j.findPath("presionD").asInt();

        int presionSistolica = j.findPath("presionS").asInt();

        return new MedicionPresion(presionDiastolica, presionSistolica, fecha);
    }
}