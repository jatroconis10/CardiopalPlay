package controllers;

import akka.actor.ActorSystem;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import models.Emergencia;
import models.Paciente;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import scala.concurrent.ExecutionContext;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * Created by JavierAntonio on 3/6/2017.
 */
public class EmergenciaController extends Controller {

    private final Executor dbContext;

    @Inject
    public EmergenciaController(ActorSystem system){
        dbContext = system.dispatchers().lookup("db-context");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create(Long id){

        JsonNode j = Controller.request().body().asJson();
        CompletionStage<Paciente> promiseP = CompletableFuture.supplyAsync(
                ()-> new Model.Finder<Long, Paciente>(Paciente.class).byId(id)
        ,dbContext);
        return promiseP.thenApply(p -> {
            if(p == null){
                return notFound("El paciente no existe");
            }
            else{
                Emergencia e = Emergencia.bind(j);
                p.getEmergencias().add(e);
                e.setPaciente(p);
                e.save();
                return ok(Json.toJson(e));
            }
        });

    }
}
