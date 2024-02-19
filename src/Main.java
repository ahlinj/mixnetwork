import java.security.KeyPair;

public class Main {
    public static void main(String[] args) throws Exception {
        KeyPair keypair1RSA = Cryptography.generateRSAKey();
        KeyPair keypair2RSA = Cryptography.generateRSAKey();
        KeyPair keypair3RSA = Cryptography.generateRSAKey();

        Message mes = new Message("Hello");
        System.out.println(mes.body);
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair1RSA.getPublic()));
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair2RSA.getPublic()));
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair3RSA.getPublic()));

        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair3RSA.getPrivate()));
        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair2RSA.getPrivate()));
        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair1RSA.getPrivate()));
    }



}
