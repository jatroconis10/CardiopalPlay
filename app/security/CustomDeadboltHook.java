package security;

import be.objectify.deadbolt.java.cache.HandlerCache;
import be.objectify.deadbolt.java.filters.AuthorizedRoutes;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

import javax.inject.Singleton;

/**
 * Bindings for Deadbolt integration.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
public class CustomDeadboltHook extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment,
                                    final Configuration configuration) {
        return this.seq(bind(MyDeadboltHandler.class).toSelf().eagerly(),
                        bind(HandlerCache.class).to(MyHandlerCache.class).eagerly(),
                        bind(AuthorizedRoutes.class).to(MyAuthorizedRoutes.class).in(Singleton.class));
    }

}