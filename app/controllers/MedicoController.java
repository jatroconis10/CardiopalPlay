package controllers;

import akka.actor.ActorSystem;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Consejo;
import models.Entrada;
import models.Medico;
import models.Paciente;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import utils.MedicoBuilder;
import views.html.consejoCrear;
import views.html.paciente;

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
    FormFactory formFactory;
    @Inject
    public MedicoController(ActorSystem system){
        dbContext = system.dispatchers().lookup("db-context");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public Result create() {
        JsonNode j = Controller.request().body().asJson();
        Medico medico = MedicoBuilder.buildMedico(j);
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
    public Result formulario(Long idP){
        final Form<Consejo> form = formFactory.form(Consejo.class);
        Paciente paciente = Paciente.find.byId(idP);
        return ok(consejoCrear.render(paciente, form));
    }
    public CompletionStage<Result> darConsejo(Long idP){

        final Form<Consejo> form = formFactory.form(Consejo.class);
        Form<Consejo> completedForm = form.bindFromRequest();
        Consejo e = completedForm.get();
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(idP),dbContext)
                .thenApply(pac -> {
                    if(pac == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        if(pac.getHistorial() == null){
                            pac.inicializarHistorial();
                            pac.getHistorial().save();
                        }
                        e.setPaciente(pac);
                        pac.getConsejos().add(e);
                        e.save();
                        final Form<Entrada> forma = formFactory.form(Entrada.class);
                        return ok(paciente.render("",pac,forma));
                    }
                });
    }


}
