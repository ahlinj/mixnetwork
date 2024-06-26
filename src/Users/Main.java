package Users;

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

    public static void main(String[] args){
        UserInterface userInterface = new UserInterface();
        Main.findMyIp();
        Main.username.set(userInterface.enterUsername());
        PKI.ipUserMap.put(username.get(),myIp);
        Main.initializeServerSocket(Constants.SERVICE_PORT);
        PublicKey pk = PKI.setPrKeyGetPubKey();

        PeerListener peerListener = new PeerListener(serverSocket.get(),Main.prKey.get());
        peerListener.start();
        Peer peer = new Peer(username.get(),port.get(), pk, Constants.SERVICE_PORT,args[0],myIp, Constants.NUM_ENCRYPTIONS);
        peer.start();
        userInterface.messageExchange(peer);
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

    public static void initializeServerSocket(int portNum) {
        try {
            ServerSocket ss = new ServerSocket(portNum);
            serverSocket.set(ss);
            port.set(ss.getLocalPort());
            PKI.portUserMap.put(username.get(),ss.getLocalPort());
            //System.out.println("Your port: " + ss.getLocalPort());
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
                //System.out.println("Server socket closed for thread: "+ss.getLocalPort());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
