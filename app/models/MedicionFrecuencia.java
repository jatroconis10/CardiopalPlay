package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class MedicionFrecuencia extends Model {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private Date fecha;

    private int latidosPMin;

    public MedicionFrecuencia(){

    }

    public MedicionFrecuencia(Date fecha, int latidosPMin){
        this.fecha = fecha;
        this.latidosPMin = latidosPMin;
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

        Long fechaL = j.findPath("fecha").asLong();
        Date fecha = new Date(fechaL);

        int frec = j.findPath("latidosPMin").asInt();

        return new MedicionFrecuencia(fecha, frec);

    }
}