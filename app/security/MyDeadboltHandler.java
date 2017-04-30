package security;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import javax.inject.Singleton;
import be.objectify.deadbolt.java.ConfigKeys;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import models.User;
import play.Configuration;
import play.cache.CacheApi;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import views.html.*;

/**
 *
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
@Singleton
public class MyDeadboltHandler implements DeadboltHandler {

    private final AuthSupport authSupport;
    private final CacheApi cache;

    private final String clientId;
    private final String domain;
    private final String redirectUri;

    @Inject
    public MyDeadboltHandler(final AuthSupport authSupport,
                             final CacheApi cache,
                             final Configuration config) {
        this.authSupport = authSupport;
        this.cache = cache;

        this.clientId = config.getString(Auth0ConfigKeys.CLIENT_ID);
        this.domain = config.getString(Auth0ConfigKeys.DOMAIN);
        this.redirectUri = config.getString(Auth0ConfigKeys.REDIRECT_URI);
    }

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(final Http.Context context) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public CompletionStage<Optional<? extends be.objectify.deadbolt.java.models.Subject>> getSubject(Http.Context context) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(cache.get(authSupport.cacheKey(context.session().get("idToken")))));
    }


    @Override
    public CompletionStage<Result> onAuthFailure( Http.Context context,
                                            Optional<String> s) {
        return getSubject(context)
                .thenApplyAsync(maybeSubject ->
                             maybeSubject.map(subject -> Optional.of((User)subject))
                                         .map(user -> new F.Tuple<>(true,
                                                                    denied.render(user)))
                                         .orElseGet(() -> new F.Tuple<>(false,
                                                                        login.render(clientId,
                                                                                     domain,
                                                                                     redirectUri))))
                .thenApplyAsync(subjectPresentAndContent -> subjectPresentAndContent._1
                                                 ? Results.forbidden(subjectPresentAndContent._2)
                                                 : Results.unauthorized(subjectPresentAndContent._2));
    }

    @Override
    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(final Http.Context context) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public String handlerName() {
        return ConfigKeys.DEFAULT_HANDLER_KEY;
    }
}
