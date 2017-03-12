package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Consejo extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private Medico medico;

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

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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
