import java.security.*;
import java.util.*;

public class PKI {
    public static Map<String, PublicKey> PKusermap = new HashMap<>();

    public static void addUser(String username){
        try{
            KeyPair kp = Cryptography.generateRSAKey();
            PKusermap.put(username, kp.getPublic());
            Main.prKey.set(kp.getPrivate());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }


}
