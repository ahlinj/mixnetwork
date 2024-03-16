import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKI {
    public static int numLayers = 4;
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();
    public static Map<String, Integer> PortUserMap = new ConcurrentHashMap<>();
    public static Map<String, Integer> LayerUserMap = new ConcurrentHashMap<>();

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
        PortUserMap.put(username, port);
    }

    public static void removeUser(String username) {
        PKusermap.remove(username);
        Main.prKey.remove();
        PortUserMap.remove(username);
    }

    public static void addUserLayer(String username){
        SecureRandom sr = new SecureRandom();
        int layer = sr.nextInt(numLayers)+1;
        LayerUserMap.put(username,layer);
        System.out.println("Layer number: "+layer+" has been assigned to: "+username);
    }

}
