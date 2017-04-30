package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Role;
import be.objectify.deadbolt.java.models.Subject;

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

        this.roles = new ArrayList<>();
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(roles);
        while (m.find()) {
            Rol rol = new Rol(m.group(1));
            this.roles.add(rol);
            System.out.println(rol.getName());
        }
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
