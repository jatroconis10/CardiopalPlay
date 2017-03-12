package models;

import com.avaje.ebean.Model;

import javax.persistence.*;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Brazalete extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Paciente paciente;

    public Brazalete(){

    }



}
