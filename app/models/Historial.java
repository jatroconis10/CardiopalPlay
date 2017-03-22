package models;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Historial extends Model{

    public static final Finder<Long, Historial> FINDER = new Finder<>(Historial.class);

    @Id
    Long id;

    @JsonIgnore
    @OneToOne
    private Paciente paciente;

    private List<MedicionFrecuencia> medicionesFrec;

    private List<MedicionEstres> medicionesEstres;

    private List<MedicionPresion> medicionesPres;

    @OneToMany(mappedBy = "historial")
    private List<Entrada> entradas;

    public Historial(Long id){

        this.id = id;
        medicionesEstres = new LinkedList<>();
        medicionesFrec = new LinkedList<>();
        medicionesPres = new LinkedList<>();
        entradas = new LinkedList<>();

    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MedicionFrecuencia> getMedicionesFrec() {
        return medicionesFrec;
    }

    public void setMedicionesFrec(List<MedicionFrecuencia> medicionesFrec) {
        this.medicionesFrec = medicionesFrec;
    }

    public List<MedicionEstres> getMedicionesEstres() {
        return medicionesEstres;
    }

    public void setMedicionesEstres(List<MedicionEstres> medicionesEstres) {
        this.medicionesEstres = medicionesEstres;
    }

    public List<MedicionPresion> getMedicionesPres() {
        return medicionesPres;
    }

    public void setMedicionesPres(List<MedicionPresion> medicionesPres) {
        this.medicionesPres = medicionesPres;
    }

    public List<Entrada> getEntradas() {
        return entradas;
    }

    public void setEntradas(List<Entrada> entradas) {
        this.entradas = entradas;
    }

    //Metodos Auxiliares

    public void agregarMedicionEstres(MedicionEstres mE){
        medicionesEstres.add(mE);
        mE.setHistorial(this);
    }

    public void agregarMedicionFrec(MedicionFrecuencia mF){
        medicionesFrec.add(mF);
        mF.setHistorial(this);
    }

    public void agregarMedicionPres(MedicionPresion mP){
        medicionesPres.add(mP);
        mP.setHistorial(this);
    }
}
