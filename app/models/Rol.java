package models;

import be.objectify.deadbolt.core.models.Role;

/**
 * Created by ja.troconis10 on 29/04/2017.
 */
public class Rol implements Role {

    private String name;

    public Rol(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }
}
