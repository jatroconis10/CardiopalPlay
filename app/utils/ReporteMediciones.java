package utils;

import models.MedicionEstres;
import models.MedicionFrecuencia;
import models.MedicionPresion;

import java.util.List;

/**
 * Created by ja.troconis10 on 12/03/2017.
 */
public class ReporteMediciones{

    private List<MedicionEstres> medicionesEstres;

    private List<MedicionFrecuencia> medicionFrecuencias;

    private List<MedicionPresion> medicionPresiones;

    public ReporteMediciones(){
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
