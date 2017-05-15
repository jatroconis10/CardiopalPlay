package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ja.troconis10 on 01/05/2017.
 */
public class Marcapasos extends Model {

    public static Finder<Long, Marcapasos> FINDER = new Finder<Long, Marcapasos>(Marcapasos.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;

    private Long configuracion;

    private Date ultimaFechaModificado;

    @NotNull
    private Paciente paciente;

    public Marcapasos(String marca, Long configuracion) {

        this.modelo = marca;
        this.configuracion = configuracion;

        this.ultimaFechaModificado = Calendar.getInstance().getTime();
    }
    public Marcapasos(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Long getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(Long configuracion) {
        this.configuracion = configuracion;
    }

    public Date getUltimaFechaModificado() {
        return ultimaFechaModificado;
    }

    public void setUltimaFechaModificado(Date ultimaFechaModificado) {
        this.ultimaFechaModificado = ultimaFechaModificado;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public void modificarConfiguracion(Long configuracion){

        this.configuracion = configuracion;

        this.ultimaFechaModificado = Calendar.getInstance().getTime();
    }

    public static Marcapasos bind(JsonNode j){

        Long config = j.findPath("config").asLong();
        String modelo = j.findPath("modelo").asText();

        return new Marcapasos(modelo, config);
    }
}
