package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JavierAntonio on 3/6/2017.
 */
@Entity
public class Emergencia extends Model {

    public static final SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

    public static final Finder<Long, Emergencia> FINDER = new Finder(Emergencia.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fecha;

    private Double lat;

    private Double lon;

    @ManyToOne(optional = false)
    private Paciente paciente;

    public Emergencia(){

    }

    public Emergencia(Date pFecha, Paciente pPaciente, Double lat, Double lon) {
        fecha = pFecha;
        paciente = pPaciente;
        this.lat = lat;
        this.lon = lon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date pFecha) {
        fecha = pFecha;
    }

    public void setPaciente(Paciente pPaciente) {
        paciente = pPaciente;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    //Metodos Auxiliares

    public static Emergencia bind(JsonNode j){
        String fechaS = j.findPath("fecha").asText();
        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Double lat = j.findPath("lat").asDouble();
        Double lon = j.findPath("lon").asDouble();

        Emergencia nE = new Emergencia();
        nE.setFecha(fecha);
        nE.setLon(lon);
        nE.setLat(lat);
        return nE;

    }
}
