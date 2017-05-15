package controllers;

import akka.actor.ActorSystem;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import utils.MedicionFactory;
import utils.ReporteMediciones;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import views.html.*;

/**
 * Created by JavierAntonio on 3/4/2017.
 */
public class PacienteController extends Controller {

    private final Executor dbContext;

    @Inject
    public PacienteController(ActorSystem system) {
        this.dbContext = system.dispatchers().lookup("db-context");
    }

    @Inject
    FormFactory formFactory;


    public Result formulario(){
        final Form<Paciente> form = formFactory.form(Paciente.class);
        return ok(pacienteCrear.render(form));
    }
    public Result formMarcapasos(Long id){
        final Form<Marcapasos> form = formFactory.form(Marcapasos.class);
        Paciente pac = new Model.Finder<Long,Paciente>(Paciente.class).byId(id);
        return ok(marcapasosCrear.render("",pac, form));
    }

    public CompletionStage<Result> create() {
        final Form<Paciente> form = formFactory.form(Paciente.class);
        Form<Paciente> completedForm = form.bindFromRequest();
        Paciente paciente = completedForm.get();
        return CompletableFuture.supplyAsync(() -> {
            paciente.save();
            return new Model.Finder<Long, Paciente>(Paciente.class).all();
        }, dbContext).thenApply(pac -> ok(pacientes.render("Lista de Pacientes",pac))
        );
    }



    public CompletionStage<Result> read() {
        CompletionStage<List<Paciente>> promiseList = CompletableFuture.supplyAsync( () -> new Model.Finder<Long, Paciente>(Paciente.class).all(), dbContext);
        return promiseList.thenApply( pacientess -> ok(pacientes.render("Lista de Pacientes",pacientess)));
    }

    public CompletionStage<Result> get(Long id) {

        CompletionStage<Paciente> promisePaciente = CompletableFuture.supplyAsync( () -> Paciente.find.byId(id)
        , dbContext);
        return promisePaciente.thenApply(pac -> {
            ObjectNode result = Json.newObject();
            if(pac == null)
                return ok(Json.toJson(result));
            else {
                final Form<Entrada> form = formFactory.form(Entrada.class);
                return ok(paciente.render("",pac, form));
            }
        });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addEstres(Long id){
        JsonNode j = Controller.request().body().asJson();

        CompletionStage<Paciente> promisePac = CompletableFuture.supplyAsync(() -> new Model.Finder<Long, Paciente>(Paciente.class).byId(id), dbContext);
        return promisePac.thenApply(p -> {
            if(p == null){
                return notFound("Paciente no encontrado");
            }
            else {
                    MedicionEstres mE = MedicionFactory.createMedEstres(j);
                    if (p.getHistorial() == null) {
                        p.inicializarHistorial();
                        p.getHistorial().save();
                    }
                    p.getHistorial().agregarMedicionEstres(mE);
                    mE.save();
                    return ok(Json.toJson(mE));
            }
        });

    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addPresion(Long id){
        JsonNode j = Controller.request().body().asJson();
        CompletionStage<Paciente> promisePac = CompletableFuture.supplyAsync(() -> new Model.Finder<Long, Paciente>(Paciente.class).byId(id), dbContext);
        return promisePac.thenApply(p -> {
            if(p == null){
                return notFound("Paciente no encontrado");
            }
            else {
                MedicionPresion mP = MedicionFactory.createMedPresion(j);
                if (p.getHistorial() == null) {
                    p.inicializarHistorial();
                    p.getHistorial().save();
                }
                p.getHistorial().agregarMedicionPres(mP);
                mP.save();
                return ok(Json.toJson(mP));
            }
        });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addFrec(Long id){
        JsonNode j = Controller.request().body().asJson();
        CompletionStage<Paciente> promisePac = CompletableFuture.supplyAsync(() -> new Model.Finder<Long, Paciente>(Paciente.class).byId(id), dbContext);
        return promisePac.thenApply((p) -> {
            if(p == null){
                return notFound("Paciente no encontrado");
            }
            else {
                MedicionFrecuencia mF = MedicionFactory.createMedFrec(j);
                if (p.getHistorial() == null) {
                    p.inicializarHistorial();
                    p.getHistorial().save();
                }
                p.getHistorial().agregarMedicionFrec(mF);
                mF.save();
                return ok(Json.toJson(mF));
            }
        });
    }

    public CompletionStage<Result> darMedicionesEn( Long id, String fechaI, String fechaF){
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            Date fechaInit = format.parse(fechaI);
            Date fechaFinal = format.parse(fechaF);
            ReporteMediciones reporteMediciones = new ReporteMediciones();
            CompletionStage<List<MedicionEstres>> promiseE = CompletableFuture.supplyAsync(() -> {
                return MedicionEstres.FINDER.where()
                        .eq("historial.id",id)
                        .ge("fecha",fechaInit)
                        .le("fecha",fechaFinal)
                        .findList();
            },dbContext);
            CompletionStage<List<MedicionFrecuencia>> promiseF = CompletableFuture.supplyAsync(() -> {
                return MedicionFrecuencia.FINDER.where()
                        .eq("historial.id",id)
                        .ge("fecha",fechaInit)
                        .le("fecha",fechaFinal)
                        .findList();
            },dbContext);
            CompletionStage<List<MedicionPresion>> promiseP = CompletableFuture.supplyAsync(() -> {
                return MedicionPresion.FINDER.where()
                        .eq("historial.id",id)
                        .ge("fecha",fechaInit)
                        .le("fecha",fechaFinal)
                        .findList();
            },dbContext);
            Paciente pac = new Model.Finder<Long, Paciente>(Paciente.class).byId(id);

             return promiseE.thenAcceptBoth( promiseF, (medicionEstres, medicionFrecuencias) -> {
                reporteMediciones.setMedicionesEstres(medicionEstres);
                reporteMediciones.setMedicionFrecuencias(medicionFrecuencias);
            }).thenCombine(promiseP, (aVoid, medicionPresions) -> {
                reporteMediciones.setMedicionPresiones(medicionPresions);
                return reporteMediciones;
            }).thenApply(reporteMediciones1 -> ok(alertas.render("",pac,reporteMediciones1)));


        }catch (ParseException e){
            return CompletableFuture.supplyAsync(()-> {
                return badRequest("Formato incorrecto para las fechas");
            });
        }

    }

    public CompletionStage<Result> createEntrada(Long id){
        final Form<Entrada> form = formFactory.form(Entrada.class);
        Form<Entrada> completedForm = form.bindFromRequest();
        Entrada e = completedForm.get();
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(id),dbContext)
                .thenApply(pac -> {
                    if(pac == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        if(pac.getHistorial() == null){
                            pac.inicializarHistorial();
                            pac.getHistorial().save();
                        }
                        e.setHistorial(pac.getHistorial());
                        pac.getHistorial().getEntradas().add(e);
                        e.save();
                        final Form<Entrada> forma = formFactory.form(Entrada.class);
                        return ok(paciente.render("",pac,forma));
                    }
                });
    }

    public CompletionStage<Result> darEntradas(Long id){
        return CompletableFuture.supplyAsync(() -> Historial.FINDER.byId(id),dbContext)
                .thenApply(historial -> {
                    if(historial == null){
                        return notFound("No existe ese historiañ");
                    }
                    else{
                        return ok(entradas.render("",new Model.Finder<Long,Paciente>(Paciente.class).byId(id),historial.getEntradas()));
                    }
                });
    }

    public CompletionStage<Result> getConsejo( Long idP, Long idC) {
        CompletionStage<List<Consejo>> promiseC = CompletableFuture.supplyAsync(() -> {
            return Consejo.FINDER.where()
                    .eq("paciente.id", idP)
                    .eq("id", idC)
                    .findList();
        }, dbContext);
        return promiseC.thenApply(consejo -> {
            ObjectNode result = Json.newObject();
            if (consejo == null)
                return ok(Json.toJson(result));
            else {
                return ok(Json.toJson(consejo));
            }
        });
    }

    public CompletionStage<Result> getConsejos( Long idP) {
        CompletionStage<List<Consejo>> promiseC = CompletableFuture.supplyAsync(() -> {
            return Consejo.FINDER.where()
                    .eq("paciente.id", idP)
                    .findList();
        }, dbContext);
        return promiseC.thenApply(consejo -> {
            ObjectNode result = Json.newObject();
            if (consejo == null)
                return ok(Json.toJson(result));
            else {
                return ok(Json.toJson(consejo));
            }
        });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> crearMarcapasos( Long idPaciente){
        JsonNode j = Controller.request().body().asJson();
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(idPaciente),dbContext)
                .thenApply(paciente -> {
                    if(paciente == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        Marcapasos m = Marcapasos.bind(j);
                        paciente.setMarcapasos(m);
                        m.setPaciente(paciente);
                        m.save();
                        return ok(Json.toJson(m));
                    }
                });
    }

    public CompletionStage<Result> modificarMarcapasos( Long idPaciente, Long config){
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(idPaciente),dbContext)
                .thenApply(paciente -> {
                    if(paciente == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        Marcapasos m = paciente.getMarcapasos();
                        if(m != null){
                           m.modificarConfiguracion(config);
                           m.save();
                           return ok(Json.toJson(m));
                        }
                        else{
                            return notFound("El paciente no tiene un marcapaso");
                        }
                    }
                });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> crearCita( Long idPaciente){
        JsonNode j = Controller.request().body().asJson();
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(idPaciente),dbContext)
                .thenApply(paciente -> {
                    if(paciente == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        Cita m = Cita.bind(j);
                        paciente.getCitas().add(m);
                        paciente.setCitas(paciente.getCitas());
                        m.setPaciente(paciente);
                        m.save();
                        return ok(Json.toJson(m));
                    }
                });
    }
    public CompletionStage<Result> readCitas() {
        CompletionStage<List<Cita>> promiseList = CompletableFuture.supplyAsync( () -> Cita.FINDER.all(), dbContext);
        return promiseList.thenApply( marcapasoss -> ok(citas.render("",marcapasoss)));

    }

    public CompletionStage<Result> getCitas( Long idP) {
        CompletionStage<List<Cita>> promiseC = CompletableFuture.supplyAsync(() -> {
            return Cita.FINDER.where()
                    .eq("paciente.id", idP)
                    .findList();
        }, dbContext);
        return promiseC.thenApply(cita -> {
            ObjectNode result = Json.newObject();
            if (cita == null)
                return ok(Json.toJson(result));
            else {
                return ok(Json.toJson(cita));
            }
        });
    }

    public CompletionStage<Result> readMarcapasos() {
        CompletionStage<List<Marcapasos>> promiseList = CompletableFuture.supplyAsync( () -> Marcapasos.FINDER.all(), dbContext);
        return promiseList.thenApply( marcapasoss -> ok(Json.toJson(marcapasoss)));

    }
}
