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

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedKey = rsaCipher.doFinal(secretKey.getEncoded());

        Message encryptedMessage = new Message(
                Base64.getEncoder().encodeToString(encryptedKey) + "::" + Base64.getEncoder().encodeToString(encryptedData),
                message.sender,
                message.portReceiver,
                Base64.getEncoder().encodeToString(encryptedRoute));
        encryptedMessage.timestamp = message.timestamp;
        return encryptedMessage;
    }

    public static Message addRouteInfo(Message message, String addRoute){
        return new Message(
                message.body,
                message.sender,
                message.portReceiver,
                addRoute+"::"+message.route
        );
    }
    public static Message deleteRouteInfo(Message message){
        String[] parts = message.route.split("::");
        String save = parts[1];


        return new Message(
                message.body,
                message.sender,
                message.portReceiver,
                save
        );
    }


    public static Message decrypt(Message encryptedMessage, PrivateKey privateKey) throws Exception {
        String[] parts = encryptedMessage.body.split("::");
        byte[] encryptedKey = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedData = Base64.getDecoder().decode(parts[1]);
        byte[] encryptedRoute = Base64.getDecoder().decode(encryptedMessage.route);

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedKeyBytes = rsaCipher.doFinal(encryptedKey);
        SecretKey secretKey = new SecretKeySpec(decryptedKeyBytes, 0, decryptedKeyBytes.length, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = aesCipher.doFinal(encryptedData);
        byte[] decryptedRoute = aesCipher.doFinal(encryptedRoute);

        Message decryptedMessage = new Message(new String(decryptedData), encryptedMessage.sender, encryptedMessage.portReceiver,new String(decryptedRoute));
        decryptedMessage.timestamp = encryptedMessage.timestamp;
        return decryptedMessage;
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = Cryptography.generateRSAKey();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter message to encrypt and decrypt: ");
            String messageBody = scanner.nextLine();
            Message originalMessage = new Message(messageBody, publicKey,0,"-1");

            System.out.println(originalMessage.body);
            System.out.println(originalMessage.route);

            originalMessage = addRouteInfo(originalMessage,"65432");

            System.out.println(originalMessage.body);
            System.out.println(originalMessage.route);

            Message encryptedOnce = Cryptography.encrypt(originalMessage, publicKey);
            System.out.println(encryptedOnce.body);
            System.out.println(encryptedOnce.route);

            encryptedOnce = addRouteInfo(encryptedOnce,"56234");

            System.out.println(encryptedOnce.body);
            System.out.println(encryptedOnce.route);

            Message encryptedTwice = Cryptography.encrypt(encryptedOnce, publicKey);
            System.out.println(encryptedTwice.body);
            System.out.println(encryptedTwice.route);

            encryptedTwice = addRouteInfo(encryptedTwice,"65789");

            System.out.println(encryptedTwice.body);
            System.out.println(encryptedTwice.route);

            encryptedTwice = deleteRouteInfo(encryptedTwice);

            System.out.println(encryptedTwice.body);
            System.out.println(encryptedTwice.route);

            Message decryptedTwice = Cryptography.decrypt(encryptedTwice, privateKey);
            System.out.println(decryptedTwice.body);
            System.out.println(decryptedTwice.route);

            decryptedTwice = deleteRouteInfo(decryptedTwice);

            System.out.println(decryptedTwice.body);
            System.out.println(decryptedTwice.route);

            Message decryptedOnce = Cryptography.decrypt(decryptedTwice, privateKey);
            System.out.println(decryptedOnce.body);
            System.out.println(decryptedOnce.route);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
