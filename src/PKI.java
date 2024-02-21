import java.security.*;
import java.util.*;

public class PKI {
    private static Map<String, PublicKey> PKusermap = new HashMap<>();

    public static void addUser(String username){
        try{
            PKusermap.put(username, Cryptography.generateRSAKey().getPublic());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }


}
