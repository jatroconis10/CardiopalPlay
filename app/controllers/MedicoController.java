package controllers;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Medico;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by JavierAntonio on 3/5/2017.
 */
public class MedicoController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        JsonNode j = Controller.request().body().asJson();
        Medico medico = Medico.bind(j);
        medico.save();
        return ok(Json.toJson(medico));
    }

    public Result read() {
        List<Medico> medicos = new Model.Finder<Long, Medico>(Medico.class).all();
        return ok(Json.toJson(medicos));
    }

    public Result get(Long id) {
        Medico Medico =  new Model.Finder<Long, Medico>(Medico.class).byId(id);
        ObjectNode result = Json.newObject();
        if(Medico == null)
            return ok(Json.toJson(result));
        else {
            return ok(Json.toJson(Medico));
        }
    }
}
