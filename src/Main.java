import java.io.IOException;
import java.net.*;
import java.security.*;
import java.util.Enumeration;

public class Main {
    public static String myIp;
    private static final ThreadLocal<ServerSocket> serverSocket = new ThreadLocal<>();
    private static final ThreadLocal<Integer> port = new ThreadLocal<>();
    public static final ThreadLocal<PrivateKey> prKey = new ThreadLocal<>();
    public static void main(String[] args) throws Exception {
        findMyIp();

        Thread t1 = new Thread(() -> {
            findMyIp();
            initializeServerSocket();
            closeServerSocket();
            PKI.addUser("Jaka1");
            System.out.println("1: "+PKI.PKusermap.get("Jaka2"));
        });
        Thread t2 = new Thread(() -> {
            findMyIp();
            initializeServerSocket();
            closeServerSocket();
            PKI.addUser("Jaka2");
            System.out.println("2: "+PKI.PKusermap.get("Jaka1"));
        });

        t1.start();
        t2.start();
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
            ServerSocket ss = new ServerSocket(0);
            serverSocket.set(ss);
            port.set(ss.getLocalPort());
            System.out.println("Server started on port: " + ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void closeServerSocket() {
        try {
            ServerSocket ss = serverSocket.get();
            if (ss != null && !ss.isClosed()) {
                ss.close();
                System.out.println("Server socket closed for current thread.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
