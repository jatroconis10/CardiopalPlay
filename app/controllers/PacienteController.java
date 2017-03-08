package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.MedicionEstres;
import models.MedicionFrecuencia;
import models.MedicionPresion;
import models.Paciente;
import play.libs.Json;
import play.mvc.*;

import java.util.List;

/**
 * Created by JavierAntonio on 3/4/2017.
 */
public class PacienteController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        JsonNode j = Controller.request().body().asJson();
        Paciente ciudad = Paciente.bind(j);
        ciudad.save();
        return ok(Json.toJson(ciudad));
    }

    public Result read() {
        List<Paciente> pacientes = new Model.Finder<Long, Paciente>(Paciente.class).all();
        return ok(Json.toJson(pacientes));
    }

    public Result get(Long id) {
        Paciente paciente =  new Model.Finder<Long, Paciente>(Paciente.class).byId(id);
        ObjectNode result = Json.newObject();
        if(paciente == null)
            return ok(Json.toJson(result));
        else {
            return ok(Json.toJson(paciente));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addEstres(Long id){
        JsonNode j = Controller.request().body().asJson();
        Paciente p = new Model.Finder<Long, Paciente>(Paciente.class).byId(id);
        if(p == null){
            return notFound("Paciente no encontrado");
        }
        else {
            MedicionEstres mE = MedicionEstres.bind(j);
            mE.save();
            p.getHistorial().agregarMedicionEstres(mE);
            return ok(Json.toJson(mE));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addPresion(Long id){
        JsonNode j = Controller.request().body().asJson();
        Paciente p = new Model.Finder<Long, Paciente>(Paciente.class).byId(id);
        if(p == null){
            return notFound("Paciente no encontrado");
        }
        else {
            MedicionPresion mP = MedicionPresion.bind(j);
            p.getHistorial().agregarMedicionPres(mP);
            mP.save();
            return ok(Json.toJson(mP));
        }
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result addFrec(Long id){
        JsonNode j = Controller.request().body().asJson();
        Paciente p = new Model.Finder<Long, Paciente>(Paciente.class).byId(id);
        if(p == null){
            return notFound("Paciente no encontrado");
        }
        else {
            MedicionFrecuencia mF = MedicionFrecuencia.bind(j);
            p.getHistorial().agregarMedicionFrec(mF);
            mF.save();
            return ok(Json.toJson(mF));
        }
    }

}
