import java.security.PublicKey;
import java.util.Scanner;

public class UserInterface {
    private final Scanner sc;

    public UserInterface() {
        this.sc = new Scanner(System.in);
    }

    public void messageExchange(Peer peer){
        String option = sendMessage();

        if (option.equals("1")) {
            peer.updateUsermaps();
            String rec = enterReceiver(peer);
            String mes = enterMessage();
            PublicKey recPK = PKI.PKusermap.get(rec);
            Message message = new Message(mes,Main.username.get(),"-1");

            if(recPK != null) {
                try {
                    message = Cryptography.encrypt(message,recPK);
                    message = Message.addRouteInfo(message,PKI.portUserMap.get(rec).toString());
                    peer.sendMessage(message,PKI.portUserMap.get("EP"));
                    messageExchange(peer);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }else{
                System.out.println("Receiver not in the network");
                messageExchange(peer);
            }

        } else if (option.equals("2")) {
            peer.updateUsermaps();
            messageExchange(peer);
        } else if (option.equals("3")) {
            Main.closeServerSocket(peer);
            System.exit(0);
        } else {
            System.out.println("Please answer with one of possible numbers.");
            messageExchange(peer);
        }
    }

    public String enterUsername(){
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    public String sendMessage(){
        System.out.println("What do you want to do?");
        System.out.println("1: Send message");
        System.out.println("2: Show users in the network");
        System.out.println("3: Leave network and close the program");
        return sc.nextLine();
    }

    public String enterMessage(){
        System.out.println("Enter message: ");
        return sc.nextLine();
    }

    public String enterReceiver(Peer peer) {
        System.out.println("Enter receiver: ");
        String answer = sc.nextLine();
        if(PKI.portUserMap.containsKey(answer) && !answer.equals("EP") && !answer.equals(Main.username.get())){
            return answer;
        }
        System.out.println("You can only choose one of the currently connected users.");
        messageExchange(peer);
        return null;
    }
}
