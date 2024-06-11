import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKI {
    public static final int numLayers = 4;
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();
    public static Map<String, Integer> portUserMap = new ConcurrentHashMap<>();
    public static Map<String, Integer> layerUserMap = new ConcurrentHashMap<>();

    public static String setPrKeyGetPubKey(){
        try{
            KeyPair kp = Cryptography.generateRSAKey();
            Main.prKey.set(kp.getPrivate());
            return publicKeyToString(kp.getPublic());
        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public static <X,Y> Map.Entry<X, Y> getRandomEntryFromMap(ConcurrentHashMap<X,Y> hashmap) {
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

    public static PublicKey stringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }


    public static void removeUser(String username) {
        PKusermap.remove(username);
        Main.prKey.remove();
        portUserMap.remove(username);
    }

    public static void addUserLayer(String username){
        SecureRandom sr = new SecureRandom();
        int layer = sr.nextInt(numLayers)+1;
        layerUserMap.put(username,layer);
        System.out.println("Layer number: "+layer+" has been assigned to: "+username);
    }

    //FOR LATER
    public static void shuffleLayers(Set<String> dontShuffle){
        SecureRandom sr = new SecureRandom();
        for(Map.Entry<String, Integer> entry : layerUserMap.entrySet()){
            if (!dontShuffle.contains(entry.getKey())) {
                entry.setValue(sr.nextInt(numLayers) + 1);
            }
        }
    }
}
