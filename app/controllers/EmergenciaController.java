package controllers;

import akka.actor.ActorSystem;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    @Restrict(@Group("Sensores"))
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
                e.setPaciente(p);
                e.save();
                return ok(Json.toJson(e));
            }
        });

    }
    @Restrict({@Group("Medicos"),@Group("Pacientes"),@Group("Cardiologo")})
    public CompletionStage<Result> darEmergenciasEn( Long id, String fechaI, String fechaF) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date fechaInit = format.parse(fechaI);
            Date fechaFinal = format.parse(fechaF);
            CompletionStage<List<Emergencia>> promiseE = CompletableFuture.supplyAsync(() -> {
                return Emergencia.FINDER.where()
                        .eq("paciente.id", id)
                        .ge("fecha", fechaInit)
                        .le("fecha", fechaFinal)
                        .findList();
            }, dbContext);
            return promiseE.thenApply(pEmergencias -> ok(Json.toJson(pEmergencias)));
        } catch (ParseException e) {
            return CompletableFuture.supplyAsync(() -> badRequest("Formato de fecha incorrect"))
                    .thenApply(pResult -> pResult);
        }
    }
}
