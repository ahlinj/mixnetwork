import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        KeyPair keypair = Cryptography.generatePubPvtKey();

        System.out.println("Public Key is: " + keypair.getPublic());

        System.out.println("Private Key is: " + keypair.getPrivate());
    }

}
