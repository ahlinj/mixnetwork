import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.*;

public class Cryptography {


    public static KeyPair generateRSAKey() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        KeyPairGenerator KPGenerator = KeyPairGenerator.getInstance("RSA");
        KPGenerator.initialize(2048, random);
        return KPGenerator.generateKeyPair();
    }


    public static Message encrypt(Message message, PublicKey publicKey) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey secretKey = keyGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = aesCipher.doFinal(message.body.getBytes());
        byte[] encryptedRoute = aesCipher.doFinal(message.route.getBytes());
        byte[] encryptedSender = aesCipher.doFinal(message.sender.getBytes());

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(secretKey.getEncoded());

        Message encryptedMessage = new Message(
                Base64.getEncoder().encodeToString(encryptedKey) + "::" + Base64.getEncoder().encodeToString(encryptedData),
                Base64.getEncoder().encodeToString(encryptedSender),
                Base64.getEncoder().encodeToString(encryptedRoute));
            encryptedMessage.timestamp = message.timestamp;
        return encryptedMessage;
    }



    public static Message decrypt(Message encryptedMessage, PrivateKey privateKey) throws Exception {
        String[] parts = encryptedMessage.body.split("::");
        byte[] encryptedKey = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedData = Base64.getDecoder().decode(parts[1]);
        byte[] encryptedRoute = Base64.getDecoder().decode(encryptedMessage.route);
        byte[] encryptedSender = Base64.getDecoder().decode(encryptedMessage.sender);

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey secretKey = new SecretKeySpec(decryptedKeyBytes, 0, decryptedKeyBytes.length, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = aesCipher.doFinal(encryptedData);
        byte[] decryptedRoute = aesCipher.doFinal(encryptedRoute);
        byte[] decryptedSender = aesCipher.doFinal(encryptedSender);

        Message decryptedMessage = new Message(new String(decryptedData), new String(decryptedSender),new String(decryptedRoute));
        decryptedMessage.timestamp = encryptedMessage.timestamp;
        return decryptedMessage;
    }
}
