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

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(secretKey.getEncoded());

        Message encryptedMessage = new Message(Base64.getEncoder().encodeToString(encryptedKey) + ":" +
                Base64.getEncoder().encodeToString(encryptedData), message.sender);
        encryptedMessage.timestamp = message.timestamp;
        return encryptedMessage;
    }


    public static Message decrypt(Message encryptedMessage, PrivateKey privateKey) throws Exception {
        String[] parts = encryptedMessage.body.split(":");
        byte[] encryptedKey = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedData = Base64.getDecoder().decode(parts[1]);

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey secretKey = new SecretKeySpec(decryptedKeyBytes, 0, decryptedKeyBytes.length, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = aesCipher.doFinal(encryptedData);

        Message decryptedMessage = new Message(new String(decryptedData), encryptedMessage.sender);
        decryptedMessage.timestamp = encryptedMessage.timestamp;
        return decryptedMessage;    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = Cryptography.generateRSAKey();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message to encrypt and decrypt: ");
            String messageBody = scanner.nextLine();
            Message originalMessage = new Message(messageBody, publicKey);

            System.out.println(originalMessage.body);

            Message encryptedOnce = Cryptography.encrypt(originalMessage, publicKey);
            System.out.println(encryptedOnce.body);

            Message encryptedTwice = Cryptography.encrypt(encryptedOnce, publicKey);
            System.out.println(encryptedTwice.body);

            Message decryptedTwice = Cryptography.decrypt(encryptedTwice, privateKey);
            System.out.println(decryptedTwice.body);

            Message decryptedOnce = Cryptography.decrypt(decryptedTwice, privateKey);
            System.out.println(decryptedOnce.body);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
