package controllers;

import akka.actor.ActorSystem;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Consejo;
import models.Medico;
import models.Paciente;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * Created by JavierAntonio on 3/5/2017.
 */
public class MedicoController extends Controller {

    private final Executor dbContext;

    @Inject
    HttpExecutionContext ec;

    @Inject
    public MedicoController(ActorSystem system){
        dbContext = system.dispatchers().lookup("db-context");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        JsonNode j = Controller.request().body().asJson();
        Medico medico = Medico.bind(j);
        medico.save();
        return ok(Json.toJson(medico));
    }

    public CompletionStage<Result> read() {
        return CompletableFuture.supplyAsync(()->Medico.FINDER.all() , dbContext)
                .thenApply(medicos -> ok(Json.toJson(medicos)));
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

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> darConsejo(Long idP, Long idM){

        JsonNode j = Controller.request().body().asJson();
        CompletionStage<Medico> promiseM = CompletableFuture.supplyAsync(() ->
            Medico.FINDER.byId(idM)
        ,dbContext);

        CompletionStage<Paciente> promiseP = CompletableFuture.supplyAsync( () ->
            Paciente.find.byId(idP)
        ,dbContext);
        return promiseM.thenCombine(promiseP, (medico, paciente) -> {
            if(medico == null){
                return notFound("Usted no es un medico registrado");
            }
            else if(paciente == null){
                return notFound("El paciente no fue encontrado");
            }
            else{
                Consejo c = Consejo.bind(j);
                c.setPaciente(paciente);
                c.setMedico(medico);
                c.save();
                paciente.getConsejos().add(c);
                return ok(Json.toJson(c));
            }
        }).thenApply(result -> result);
    }
}
