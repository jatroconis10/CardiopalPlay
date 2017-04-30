package security;

import javax.inject.Inject;
import be.objectify.deadbolt.java.filters.DeadboltRoutePathFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

public class Filters implements HttpFilters {

    // plus any other filters you have
    private final DeadboltRoutePathFilter deadbolt;

    @Inject
    public Filters(final DeadboltRoutePathFilter deadbolt) {
        this.deadbolt = deadbolt;
    }

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[]{deadbolt};
    }
}