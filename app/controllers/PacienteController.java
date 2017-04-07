package controllers;

import akka.actor.ActorSystem;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.*;
import play.libs.Json;
import play.mvc.*;
import security.IntegrityException;
import utils.ReporteMediciones;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

/**
 * Created by JavierAntonio on 3/4/2017.
 */
public class PacienteController extends Controller {

    private final Executor dbContext;

    @Inject
    public PacienteController(ActorSystem system){
        dbContext = system.dispatchers().lookup("db-context");
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create() {
        JsonNode j = Controller.request().body().asJson();
        Paciente paciente = Paciente.bind(j);

        return CompletableFuture.supplyAsync(() -> {
            paciente.save();
            return paciente;
        }, dbContext).thenApply(pac -> ok(Json.toJson(pac))
        );
    }

    public CompletionStage<Result> read() {
        CompletionStage<List<Paciente>> promiseList = CompletableFuture.supplyAsync( () -> new Model.Finder<Long, Paciente>(Paciente.class).all(), dbContext);
        return promiseList.thenApply( pacientes -> ok(Json.toJson(pacientes)));

    }

    public CompletionStage<Result> get(Long id) {

        CompletionStage<Paciente> promisePaciente = CompletableFuture.supplyAsync( () -> Paciente.find.byId(id)
        , dbContext);
        return promisePaciente.thenApply(paciente -> {
            ObjectNode result = Json.newObject();
            if(paciente == null)
                return ok(Json.toJson(result));
            else {
                return ok(Json.toJson(paciente));
            }
        });
    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addEstres(Long id){
        JsonNode j = Controller.request().body().asJson();

        CompletionStage<Paciente> promisePac = CompletableFuture.supplyAsync(() -> new Model.Finder<Long, Paciente>(Paciente.class).byId(id), dbContext);
        return promisePac.thenApply((p) -> {
            if(p == null){
                return notFound("Paciente no encontrado");
            }
            else {
                try {
                    MedicionEstres mE = MedicionEstres.bind(j);
                    if (p.getHistorial() == null) {
                        p.inicializarHistorial();
                        p.getHistorial().save();
                    }
                    p.getHistorial().agregarMedicionEstres(mE);
                    mE.save();
                    return ok(Json.toJson(mE));
                }catch (IntegrityException e){
                    return badRequest();
                }
            }
        });

    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> addPresion(Long id){
        JsonNode j = Controller.request().body().asJson();
        CompletionStage<Paciente> promisePac = CompletableFuture.supplyAsync(() -> new Model.Finder<Long, Paciente>(Paciente.class).byId(id), dbContext);
        return promisePac.thenApply((p) -> {
            if(p == null){
                return notFound("Paciente no encontrado");
            }
            else {
                try {
                    MedicionPresion mP = MedicionPresion.bind(j);
                    if (p.getHistorial() == null) {
                        p.inicializarHistorial();
                        p.getHistorial().save();
                    }
                    p.getHistorial().agregarMedicionPres(mP);
                    mP.save();
                    return ok(Json.toJson(mP));
                }
                catch(IntegrityException e){
                    return badRequest();
                }
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
                try {
                    MedicionFrecuencia mF = MedicionFrecuencia.bind(j);
                    if (p.getHistorial() == null) {
                        p.inicializarHistorial();
                        p.getHistorial().save();
                    }
                    p.getHistorial().agregarMedicionFrec(mF);
                    mF.save();
                    return ok(Json.toJson(mF));
                }
                catch (IntegrityException e){
                    return badRequest();
                }
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

             return promiseE.thenAcceptBoth( promiseF, (medicionEstres, medicionFrecuencias) -> {
                reporteMediciones.setMedicionesEstres(medicionEstres);
                reporteMediciones.setMedicionFrecuencias(medicionFrecuencias);
            }).thenCombine(promiseP, (aVoid, medicionPresions) -> {
                reporteMediciones.setMedicionPresiones(medicionPresions);
                return reporteMediciones;
            }).thenApply(reporteMediciones1 -> ok(Json.toJson(reporteMediciones)));


        }catch (ParseException e){
            return CompletableFuture.supplyAsync(()-> {
                return badRequest("Formato incorrecto para las fechas");
            });
        }

    }

    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> createEntrada(Long id){
        JsonNode j = Controller.request().body().asJson();
        return CompletableFuture.supplyAsync(() -> Paciente.find.byId(id),dbContext)
                .thenApply(paciente -> {
                    if(paciente == null){
                        return notFound("No existe el paciente");
                    }
                    else{
                        Entrada e = Entrada.bind(j);
                        if(paciente.getHistorial() == null){
                            paciente.inicializarHistorial();
                            paciente.getHistorial().save();
                        }
                        e.setHistorial(paciente.getHistorial());
                        paciente.getHistorial().getEntradas().add(e);
                        e.save();
                        return ok(Json.toJson(e));
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
                        return ok(Json.toJson(historial.getEntradas()));
                    }
                });
    }

}
