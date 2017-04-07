package security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ja.troconis10 on 07/04/2017.
 */
public class IntegrityVerifier {

    public static boolean verifySHA256(String receivedHash, String message){
        byte[] receivedHashBytes = receivedHash.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageBytes = message.getBytes();
            byte[] digestMessage = md.digest(messageBytes);
            return compare(receivedHashBytes, digestMessage);

        }catch (NoSuchAlgorithmException e){
            return false;
        }

    }

    private static boolean compare(byte[] received, byte[] calculated){
        int l = received.length;
        if(l != calculated.length){
            return false;
        }
        for(int i = 0; i < l; i++){
            if(received[i]!=calculated[i]){
                return false;
            }
        }
        return true;
    }
}
