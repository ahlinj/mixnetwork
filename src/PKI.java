import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKI {
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();

    public static void addUser(String username){
        try{
            KeyPair kp = Cryptography.generateRSAKey();
            PKusermap.put(username, kp.getPublic());
            Main.prKey.set(kp.getPrivate());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    public static void removeUser(String username) {
        PKusermap.remove(username);
        Main.prKey.remove();
    }


}
