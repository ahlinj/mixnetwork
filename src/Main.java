import java.security.KeyPair;

public class Main {
    public static void main(String[] args) throws Exception {
        KeyPair keypair = Cryptography.generatePubPvtKey();

        System.out.println("Public Key is: " + keypair.getPublic());

        System.out.println("Private Key is: " + keypair.getPrivate());

        Message mes = new Message("Hello");
        mes.body = Cryptography.encrypt(mes.body, keypair.getPublic());
        System.out.println(mes.body);
        mes.body = Cryptography.decrypt(mes.body,keypair.getPrivate());
        System.out.println(mes.body);


    }



}
