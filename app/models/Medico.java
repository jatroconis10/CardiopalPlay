package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by ja.troconis10 on 04/03/2017.
 */
@Entity
public class Medico extends Model{

    public static final Finder<Long, Medico> FINDER = new Finder<>(Medico.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;

    private String apellidos;

    private TipoEspecializacion especializacion;

    public Medico(){

    }

    public Medico(String nombres, String apellidos, TipoEspecializacion especializacion){
        this .nombres= nombres;
        this.apellidos=apellidos;
        this.especializacion=especializacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public TipoEspecializacion getEspecializacion() {
        return especializacion;
    }

    public void setEspecializacion(TipoEspecializacion especializacion) {
        this.especializacion = especializacion;
    }

    //Metodos Auxiliares

    public static Medico bind(JsonNode j){

        String nombres = j.findPath("nombres").asText();
        String apellidos= j.findPath("apellidos").asText();

        TipoEspecializacion especializacion = TipoEspecializacion.forValue(j.findPath("tipo").asText());

        return new Medico(nombres, apellidos, especializacion);

    }
}
