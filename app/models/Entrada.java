package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ja.troconis10 on 04/03/2017.
 */
@Entity
public class Entrada extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private TipoEntrada tipo;

    private String descripcion;

    @ManyToOne
    private Historial historial;

    public Entrada(){

    }

    public Entrada(TipoEntrada tipo, String descripcion){
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.historial =historial;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoEntrada getTipo() {
        return tipo;
    }

    public void setTipo(TipoEntrada tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

//Metodos auxiliares

    public static Entrada bind(JsonNode j){

        String desc = j.findPath("descripcion").asText();
        TipoEntrada tipo = TipoEntrada.forValue(j.findPath("tipo").asText());

        return new Entrada(tipo, desc);

    }
}
