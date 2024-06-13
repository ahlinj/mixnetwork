import java.io.IOException;
import java.net.*;
import java.security.*;
import java.util.*;

public class Main {
    public static String myIp;
    public static final ThreadLocal<String> username = new ThreadLocal<>();
    public static final ThreadLocal<ServerSocket> serverSocket = new ThreadLocal<>();
    public static final ThreadLocal<Integer> port = new ThreadLocal<>();
    public static final ThreadLocal<PrivateKey> prKey = new ThreadLocal<>();
    public static final ThreadLocal<PublicKey> pbKey = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        UserInterface userInterface = new UserInterface();
        if (args[0].equals("EP")) {
            Main.findMyIp();
            Main.username.set(args[0]);
            Main.initializeServerSocket();

            EntryPointListener entryPointListener = new EntryPointListener(Main.serverSocket.get());
            entryPointListener.start();

/*
            while (true) {
                try {
                    Thread.sleep(4000);
                    System.out.println("PortUserMap:");
                    for (Map.Entry<String, Integer> entry : PKI.portUserMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                    System.out.println("LayerUserMap:");
                    for (Map.Entry<String, Integer> entry : PKI.layerUserMap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(4000);
                    System.out.println("PublicKeyUserMap:");
                    for (Map.Entry<String, PublicKey> entry : PKI.PKusermap.entrySet()) {
                        System.out.println(entry.getKey() + ": " + entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

 */

        } else {
            Main.findMyIp();
            Main.username.set(userInterface.enterUsername());
            Main.initializeServerSocket();
            pbKey.set(PKI.stringToPublicKey(PKI.setPrKeyGetPubKey()));

            PeerListener peerListener = new PeerListener(Main.serverSocket.get(),Main.prKey.get());
            peerListener.start();
            Peer peer = new Peer(Main.username.get(), Main.port.get(), Main.pbKey.get());
            peer.start();
            userInterface.messageExchange(peer);

        }
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
                            //System.out.println("LocalIp: " + myIp);
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
            PKI.portUserMap.put(username.get(),ss.getLocalPort());
            System.out.println("Your port: " + ss.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void closeServerSocket(Peer peer) {
        try {
            ServerSocket ss = serverSocket.get();
            if (ss != null && !ss.isClosed()) {
                peer.removeUser();
                ss.close();
                System.out.println("Server socket closed for thread: "+ss.getLocalPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
