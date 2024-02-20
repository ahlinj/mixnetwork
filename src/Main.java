import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.security.KeyPair;
import java.util.Enumeration;

public class Main {
    public static String myIp;
    private static ServerSocket serverSocket;
    private static int port;
    public static KeyPair key;
    public static void main(String[] args) throws Exception {
        KeyPair keypair1RSA = Cryptography.generateRSAKey();
        KeyPair keypair2RSA = Cryptography.generateRSAKey();
        KeyPair keypair3RSA = Cryptography.generateRSAKey();

        findMyIp();
        initializeServerSocket();

        Message mes = new Message("Hello",keypair1RSA.getPublic());
        System.out.println(mes.body);
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair1RSA.getPublic()));
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair2RSA.getPublic()));
        System.out.println(mes.body = Cryptography.encrypt(mes.body, keypair3RSA.getPublic()));

        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair3RSA.getPrivate()));
        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair2RSA.getPrivate()));
        System.out.println(mes.body = Cryptography.decrypt(mes.body, keypair1RSA.getPrivate()));

        closeServerSocket();
    }


    public static void findMyIp(){
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface inter = (NetworkInterface) e.nextElement();
                if (inter.isUp() && !inter.isLoopback()) {
                    Enumeration addr = inter.getInetAddresses();
                    while (addr.hasMoreElements()) {
                        InetAddress address = (InetAddress) addr.nextElement();
                        myIp = address.getHostAddress().replace("/", "");
                        if (myIp.length() <= 15) {
                            System.out.println("LocalIp: " + myIp);
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public static void initializeServerSocket() {
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            System.out.println("Server started on port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
