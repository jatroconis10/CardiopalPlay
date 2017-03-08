package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import models.Emergencia;
import models.Paciente;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by JavierAntonio on 3/6/2017.
 */
public class EmergenciaController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result create(Long id){

        JsonNode j = Controller.request().body().asJson();
        Paciente p = new Model.Finder<Long, Paciente>(Paciente.class).byId(id);
        if(p == null){
            return notFound("El paciente no existe");
        }
        else{
            Emergencia e = Emergencia.bind(j);
            e.setPaciente(p);
            e.save();
        }
        return ok();
    }
}
