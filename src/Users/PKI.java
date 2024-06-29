package Users;

import java.security.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKI {
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();
    public static Map<String, Integer> portUserMap = new ConcurrentHashMap<>();
    public static Map<String, String> ipUserMap = new ConcurrentHashMap<>();

    public static PublicKey setPrKeyGetPubKey(){
        try{
            KeyPair kp = Cryptography.generateRSAKey();
            Main.prKey.set(kp.getPrivate());
            //System.out.println(kp.getPublic());
            return kp.getPublic();
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <X,Y> Map.Entry<X, Y> getRandomEntryFromMap(Map<X,Y> hashmap) {
        if (hashmap.isEmpty()) {
            return null;
        } else {
            Random rand = new Random();
            Object[] values = hashmap.entrySet().toArray();
            return (Map.Entry<X, Y>) values[rand.nextInt(values.length)];
        }
    }

    public static String publicKeyToString(PublicKey publicKey) {
        byte[] publicKeyBytes = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }
}
