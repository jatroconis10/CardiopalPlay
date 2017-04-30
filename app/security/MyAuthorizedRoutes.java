package security;

import static be.objectify.deadbolt.java.filters.Methods.POST;
import static be.objectify.deadbolt.java.utils.TemplateUtils.allOf;
import static be.objectify.deadbolt.java.utils.TemplateUtils.allOfGroup;
import static be.objectify.deadbolt.java.filters.Methods.GET;
import static be.objectify.deadbolt.java.utils.TemplateUtils.anyOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Provider;
import be.objectify.deadbolt.java.filters.AuthorizedRoute;
import be.objectify.deadbolt.java.filters.AuthorizedRoutes;
import be.objectify.deadbolt.java.filters.FilterConstraints;
import be.objectify.deadbolt.java.utils.TemplateUtils;

public class MyAuthorizedRoutes extends AuthorizedRoutes {

    public static final String MEDICO = "Medico";
    public static final String ADMINISTRADOR = "Administrador";
    public static final String CARDIOLOGO = "Cardiologo";
    public static final String PACIENTE = "Paciente";
    public static final String SENSORES = "Sensores";
    @Inject
    public MyAuthorizedRoutes(final Provider<FilterConstraints> filterConstraints) {
        super(filterConstraints);
    }

    @Override
    public List<AuthorizedRoute> routes() {
        return Arrays.asList(new AuthorizedRoute(POST,
                        "/paciente",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO)))),
                new AuthorizedRoute(GET,
                        "/paciente/$id<[^/]+>",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO),allOf(PACIENTE)))),
                new AuthorizedRoute(GET,
                        "/paciente",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO)))),
                new AuthorizedRoute(POST,
                        "/paciente/$id<[^/]+>/estres",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(SENSORES)))),
                new AuthorizedRoute(POST,
                        "/paciente/$id<[^/]+>/frec",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(SENSORES)))),
                new AuthorizedRoute(POST,
                        "/paciente/$id<[^/]+>/entrada",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(SENSORES)))),
                new AuthorizedRoute(GET,
                        "/paciente/$id<[^/]+>/meds",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO),allOf(PACIENTE)))),
                new AuthorizedRoute(GET,
                        "/paciente/$id<[^/]+>/historial",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO),allOf(PACIENTE)))),
                new AuthorizedRoute(POST,
                        "/medico",
                        filterConstraints.restrict(allOfGroup(ADMINISTRADOR))),
                new AuthorizedRoute(GET,
                        "/medico",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO),allOf(PACIENTE)))),
                new AuthorizedRoute(POST,
                        "/medico/$idM<[^/]+>/consejo/$idP<[^/]+>",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO)))),
                new AuthorizedRoute(POST,
                        "/paciente/$id<[^/]+>/emergencia",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(SENSORES)))),
                new AuthorizedRoute(GET,
                        "/paciente/$id<[^/]+>/emergencia",
                        filterConstraints.restrict(anyOf(allOf(ADMINISTRADOR),allOf(MEDICO),allOf(PACIENTE)))));
    }
}
