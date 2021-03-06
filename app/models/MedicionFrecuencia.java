package models;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class MedicionFrecuencia extends Model {

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

}