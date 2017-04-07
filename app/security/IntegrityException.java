package security;

/**
 * Created by ja.troconis10 on 07/04/2017.
 */
public class IntegrityException extends Exception {

    public  IntegrityException(){
        super("El mensaje no es llego integro");
    }
}
