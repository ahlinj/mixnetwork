import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKI {
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();
    public static Map<String, Integer> PortUserMap = new ConcurrentHashMap<>();

    public static void addUserPK(String username){
        try{
            KeyPair kp = Cryptography.generateRSAKey();
            PKusermap.put(username, kp.getPublic());
            Main.prKey.set(kp.getPrivate());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }
    public static void addUserPort(String username, int port){
        PortUserMap.put(username, Main.port.get());
    }

    public static void removeUser(String username) {
        PKusermap.remove(username);
        Main.prKey.remove();
        PortUserMap.remove(username);
    }


}
