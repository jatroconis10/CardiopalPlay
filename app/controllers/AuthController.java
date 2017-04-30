package controllers;

import javax.inject.Inject;

import be.objectify.deadbolt.java.models.Role;
import models.Rol;
import models.User;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import security.Auth0ConfigKeys;
import security.AuthSupport;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Configuration;
import play.cache.CacheApi;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * Interactions with Auth0.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
public class AuthController extends Controller {

    private final CacheApi cache;
    private final AuthSupport authSupport;

    private final String clientId;
    private final String clientSecret;
    private final String domain;
    private final String redirectUri;

    @Inject
    HttpExecutionContext ec;
    @Inject
    WSClient ws;
    @Inject
    public AuthController(final AuthSupport authSupport,
                          final CacheApi cache,
                          final Configuration config) {
        this.cache = cache;
        this.authSupport = authSupport;

        this.clientId = config.getString(Auth0ConfigKeys.CLIENT_ID);
        this.clientSecret = config.getString(Auth0ConfigKeys.CLIENT_SECRET);
        this.domain = config.getString(Auth0ConfigKeys.DOMAIN);
        this.redirectUri = config.getString(Auth0ConfigKeys.REDIRECT_URI);
    }

    /**
     * Render the log-in view containing the Auth0 form
     */
    public CompletionStage<Result> logIn() {
        return CompletableFuture.supplyAsync(() -> login.render(this.clientId,
                                                    this.domain,
                                                    this.redirectUri))
                        .thenApplyAsync(Results::ok);
    }

    /**
     * Receives a callback from Auth0 with an authentication code.
     *
     * @param maybeCode the authentication code, used to obtain a user token
     * @return redirects to the index
     */
    public CompletionStage<Result> callback(final Optional<String> maybeCode) {
        return maybeCode.map(code -> getToken(code).thenComposeAsync (token -> getUser(token))
                                                   .thenApplyAsync(userAndToken -> {
                                                       // userAndToken._1 is the user
                                                       // userAndToken._2 is the token
                                                       cache.set(authSupport.cacheKey(userAndToken._2._1),
                                                                 userAndToken._1,
                                                                 60*15); // cache for 15 minutes
                                                       session("idToken",
                                                               userAndToken._2._1);
                                                       session("accessToken",
                                                               userAndToken._2._2);
                                                       return redirect(routes.Application.index());
                                                   },ec.current()))
                                            .orElse(CompletableFuture.completedFuture(badRequest("No parameters supplied")));    }

    /**
     * Get the user token from Auth0, using the code we received earlier.
     *
     * @param code the Auth0 code
     * @return a tuple containing the id and access tokens
     */
    private CompletionStage<F.Tuple<String, String>> getToken(final String code) {
        final ObjectNode root = Json.newObject();
        root.put("client_id",
                 this.clientId);
        root.put("client_secret",
                 this.clientSecret);
        root.put("redirect_uri",
                 this.redirectUri);
        root.put("code",
                 code);
        root.put("grant_type",
                 "authorization_code");
        return ws.url(String.format("https://%s/oauth/token",
                                    this.domain))
                 .setHeader(Http.HeaderNames.ACCEPT,
                            Http.MimeTypes.JSON)
                 .post(root)
                 .thenApplyAsync(WSResponse::asJson)
                 .thenApplyAsync(json -> new F.Tuple<>(json.get("id_token").asText(),
                                            json.get("access_token").asText()));
    }

    /**
     *
     * @param token
     * @return
     */
    private CompletionStage<F.Tuple<User, F.Tuple<String, String>>> getUser(final F.Tuple<String, String> token) {
        List<? extends Role> roles = new ArrayList<Rol>();
        return ws.url(String.format("https://%s/userinfo",
                this.domain))
                .setQueryParameter("access_token",
                        token._2)
                .get()
                .thenApplyAsync(WSResponse::asJson)
                .thenApplyAsync(json -> new User(json.get("user_id").asText(),
                        json.get("email").toString(),
                        json.get("picture").asText(),json.get("roles").toString()))
                .thenApplyAsync(localUser -> new F.Tuple<>(localUser,
                        token));
    }

    public CompletionStage<Result> logOut() {
        return CompletableFuture.supplyAsync(() ->
        {
            final Http.Session session = session();
            final String idToken = session.remove("idToken");
            session.remove("accessToken");
            cache.remove(authSupport.cacheKey(idToken));
            return "ignoreThisValue";
        },ec.current())
                .thenApplyAsync(id -> redirect(controllers.routes.AuthController.logIn()));
    }

    public CompletionStage<Result> denied() {
        final Http.Context ctx = ctx();
        return CompletableFuture.supplyAsync(() -> authSupport.currentUser(ctx))
                .thenApplyAsync(maybeUser -> maybeUser.map(user -> (Result) forbidden(denied.render(maybeUser)))
                        .orElseGet(() -> redirect(controllers.routes.AuthController.logIn())));
    }
}