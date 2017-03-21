package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Paciente extends Model {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;

    private String apellidos;

    @OneToMany(mappedBy = "paciente")
    private List<Cita> citas;

    @OneToMany(mappedBy = "paciente")
    private List<Consejo> consejos;

    @OneToOne(mappedBy = "paciente")
    private Historial historial;

    @OneToOne(mappedBy = "paciente")
    private Brazalete brazalete;

    private List<Emergencia> emergencias;

    //Auxiliar para consultas
    public static Finder<Long, Paciente> find = new Finder<>(Paciente.class);

    //Constructores
    public Paciente() {
    }

    public Paciente(String nombres, String apellidos) {
        this.nombres = nombres;
        this.apellidos = apellidos;

        this.citas = new ArrayList<>();
        this.consejos = new ArrayList<>();
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

    public Brazalete getBrazalete() {
        return brazalete;
    }

    public void setBrazalete(Brazalete brazalete) {
        this.brazalete = brazalete;
    }

    public List<Cita> getCitas() {
        return citas;
    }

    public void setCitas(List<Cita> pCitas) {
        citas = pCitas;
    }

    public List<Consejo> getConsejos() {
        return consejos;
    }

    public void setConsejos(List<Consejo> pConsejos) {
        consejos = pConsejos;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial pHistorial) {
        historial = pHistorial;
    }

    public void setEmergencias(List<Emergencia> emergencias) {
        this.emergencias = emergencias;
    }

    //Metodos Auxiliares

    public void inicializarHistorial(){
        if (historial == null){
            this.historial = new Historial(id);
            historial.setPaciente(this);
        }
    }

    public static Paciente bind(JsonNode j){
        String nombres = j.findPath("nombres").asText();
        String apellidos = j.findPath("apellidos").asText();

        return new Paciente(nombres, apellidos);
    }
}
