package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by JavierAntonio on 3/6/2017.
 */
@Entity
public class Emergencia extends Model {

    private Date fecha;

    @ManyToOne
    private Paciente paciente;

    public Emergencia(){

    }

    public Emergencia(Date pFecha, Paciente pPaciente) {
        fecha = pFecha;
        paciente = pPaciente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date pFecha) {
        fecha = pFecha;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente pPaciente) {
        paciente = pPaciente;
    }

    //Metodos Auxiliares

    public static Emergencia bind(JsonNode j){
        Long fechaL = j.findPath("fecha").asLong();
        Date fecha = new Date(fechaL);

        Emergencia nE = new Emergencia();
        nE.setFecha(fecha);
        return nE;

    }
}
