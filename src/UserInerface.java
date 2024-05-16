import java.security.PublicKey;
import java.util.Scanner;

public class UserInerface {
    private final Scanner sc;

    public UserInerface() {
        this.sc = new Scanner(System.in);
    }

    public void messageExchange(){
        String yesNo = sendMessage();

        if (yesNo.equals("Y")) {
            String rec = enterReceiver();
            String mes = enterMessage();
            Message message = new Message(mes,PKI.PKusermap.get(Main.username.get()),Protocol.MESSAGE);
            PublicKey recPK = null;
            recPK = PKI.PKusermap.get(rec);
            if(recPK != null) {
                try {
                    System.out.println(Cryptography.encrypt(message,recPK).body);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }else{
                System.out.println("Receiver not in the network");
                messageExchange();
            }

        } else if (yesNo.equals("N")) {
            //Main.closeServerSocket();
        } else {
            System.out.println("Please answer with Y or N");
            messageExchange();
        }
    }

    public String enterUsername(){
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    public String sendMessage(){
        System.out.println("Do you want to send the message? (Y/N)");
        return sc.nextLine();
    }

    public String enterMessage(){
        System.out.println("Enter message: ");
        return sc.nextLine();
    }

    public String enterReceiver(){
        System.out.println("Enter receiver: ");
        return sc.nextLine();
    }


}
