package EntryPoint;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PKIEP {
    public static Map<String, PublicKey> PKusermap = new ConcurrentHashMap<>();
    public static Map<String, Integer> portUserMap = new ConcurrentHashMap<>();
    public static Map<String, String> ipUserMap = new ConcurrentHashMap<>();


    public static PublicKey stringToPublicKey(String publicKeyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
    }
}
