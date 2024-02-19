import javax.crypto.*;
import java.security.*;
import java.util.*;

public class Cryptography {


    public static KeyPair generatePubPvtKey() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        KeyPairGenerator KPGenerator = KeyPairGenerator.getInstance("RSA");
        KPGenerator.initialize(2048, random);
        return KPGenerator.generateKeyPair();
    }

    public static String encrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

}
