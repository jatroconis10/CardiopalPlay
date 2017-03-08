package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Historial extends Model{

    @Id
    Long id;

    @OneToMany
    List<MedicionFrecuencia> medicionesFrec;

    @OneToMany
    List<MedicionEstres> medicionesEstres;

    @OneToMany
    List<MedicionPresion> medicionesPres;

    @OneToMany
    List<Entrada> entradas;

    public Historial(Long id){

        this.id = id;
        medicionesEstres = new LinkedList<>();
        medicionesFrec = new LinkedList<>();
        medicionesPres = new LinkedList<>();
        entradas = new LinkedList<>();

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
    }

    public void agregarMedicionFrec(MedicionFrecuencia mF){
        medicionesFrec.add(mF);
    }

    public void agregarMedicionPres(MedicionPresion mP){
        medicionesPres.add(mP);
    }

    public ReporteMediciones darReporte(Date fechaI, Date fechaF){
        return null;
    }

    private class ReporteMediciones{

        private List<MedicionEstres> medicionesEstres;

        private List<MedicionFrecuencia> medicionFrecuencias;

        private List<MedicionPresion> medicionPresiones;

        public ReporteMediciones(List<MedicionEstres> mE, List<MedicionFrecuencia> mF, List<MedicionPresion> mP){
            medicionesEstres = mE;
            medicionesFrec = mF;
            medicionesPres = mP;
        }

        public List<MedicionEstres> getMedicionesEstres() {
            return medicionesEstres;
        }

        public void setMedicionesEstres(List<MedicionEstres> pMedicionesEstres) {
            medicionesEstres = pMedicionesEstres;
        }

        public List<MedicionFrecuencia> getMedicionFrecuencias() {
            return medicionFrecuencias;
        }

        public void setMedicionFrecuencias(List<MedicionFrecuencia> pMedicionFrecuencias) {
            medicionFrecuencias = pMedicionFrecuencias;
        }

        public List<MedicionPresion> getMedicionPresiones() {
            return medicionPresiones;
        }

        public void setMedicionPresiones(List<MedicionPresion> pMedicionPresiones) {
            medicionPresiones = pMedicionPresiones;
        }
    }
}
