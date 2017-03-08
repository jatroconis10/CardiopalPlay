package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Consejo extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String descripcion;

    Paciente paciente;

    Medico medico;

    public Consejo() {
    }

    public Consejo(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    //Metodos Auxiliares

    public static Consejo bind(JsonNode j){
        String descripcion = j.findPath("descripcion").asText();
        return new Consejo(descripcion);
    }
}
