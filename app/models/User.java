package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import be.objectify.deadbolt.core.models.Permission;
import be.objectify.deadbolt.core.models.Role;
import be.objectify.deadbolt.core.models.Subject;

/**
 * A very simple implementation of {@link Subject}.
 *
 * @author Steve Chaloner (steve@objectify.be)
 */
public class User implements Subject {

    private final String userId;
    private final String name;
    private final String image;
    private List<Rol> roles;

    public User(final String userId,
                final String name,
                final String image,
                String roles) {
        this.userId = userId;
        this.name = name;
        roles.split(",");
        Rol rol = new Rol("roles");
        this.roles = new ArrayList<Rol>();
        this.roles.add(rol);
        this.image = image;
    }

    @Override
    public List<? extends Role> getRoles() {
        return roles;
    }

    @Override
    public List<? extends Permission> getPermissions() {
        return Collections.emptyList();
    }

    @Override
    public String getIdentifier() {
        return userId;
    }


    public String getName() {
        return name;
    }

}
