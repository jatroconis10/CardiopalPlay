package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.inject.Inject;
import javax.persistence.*;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Cita extends Model {

    public static Finder<Long, Cita> FINDER = new Finder<Long, Cita>(Cita.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String dia;
    @ManyToOne
    private Paciente paciente;

    public Cita(String dia){
        this.dia = dia;
    }
    public String getDia(){
        return dia;
    }
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public static Cita bind(JsonNode j){

        String dia = j.findPath("dia").asText();

        return new Cita(dia);
    }
}

