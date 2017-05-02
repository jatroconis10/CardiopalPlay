package controllers;

import be.objectify.deadbolt.java.actions.SubjectPresent;
import play.libs.concurrent.HttpExecutionContext;
import security.AuthSupport;
import com.google.inject.Inject;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

import java.util.concurrent.CompletableFuture;

/**
 * A very simple controller whose sole action requires a subject to be
 * present, i.e. the user be authenticated.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
public class Application extends Controller {

    private final AuthSupport authSupport;
    @Inject
    HttpExecutionContext ec;
    @Inject
    public Application(final AuthSupport authSupport) {

        this.authSupport = authSupport;
    }
    @SubjectPresent
    public CompletableFuture<Result> index() {
        return CompletableFuture.supplyAsync(() -> index.render("Protected content",
                authSupport.currentUser(ctx())),ec.current())
                .thenApplyAsync(Results::ok);
    }


}
