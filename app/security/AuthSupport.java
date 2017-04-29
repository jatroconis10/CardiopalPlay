package security;

import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import models.User;
import play.cache.CacheApi;
import play.mvc.Http;

/**
 * Utility methods for user caching.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
@Singleton
public class AuthSupport {

    private final CacheApi cache;

    @Inject
    public AuthSupport(final CacheApi cache) {
        this.cache = cache;
    }

    public Optional<User> currentUser(final Http.Context context)
    {
        return Optional.ofNullable(cache.get(cacheKey(context.session().get("idToken"))));
    }

    public String cacheKey(final String key)
    {
        return "user.cache." + key;
    }
}
