package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class MedicionFrecuencia extends Model {

    public static final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static final Finder<Long, MedicionFrecuencia> FINDER = new Finder<>(MedicionFrecuencia.class);

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date fecha;

    private int latidosPMin;

    @ManyToOne(optional = false)
    private Historial historial;

    public MedicionFrecuencia(){

    }

    public MedicionFrecuencia(Date fecha, int latidosPMin){
        this.fecha = fecha;
        this.latidosPMin = latidosPMin;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getLatidosPMin() {
        return latidosPMin;
    }

    public void setLatidosPMin(int latidosPMin) {
        this.latidosPMin = latidosPMin;
    }

    //Metodos auxiliares

    public static MedicionFrecuencia bind(JsonNode j){

        String fechaS = j.findPath("fecha").asText();
        Date fecha = null;
        try {
            fecha = format.parse(fechaS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int frec = j.findPath("latidosPMin").asInt();

        return new MedicionFrecuencia(fecha, frec);

    }
}