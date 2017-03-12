package models;

import com.avaje.ebean.Model;

import javax.inject.Inject;
import javax.persistence.*;

/**
 * Created by ja.troconis10 on 25/02/2017.
 */
@Entity
public class Cita extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    private Paciente paciente;
}
