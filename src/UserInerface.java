import java.util.Scanner;

public class UserInerface {
    private final Scanner sc;

    public UserInerface() {
        this.sc = new Scanner(System.in);
    }

    public String enterUsername(){
        System.out.println("Enter your username: ");
        return sc.nextLine();
    }

    public String enterReceiver(){
        System.out.println("Do you want to send the message? (Y/N)");
        String answer = sc.nextLine();
        if(answer.equals("Y")){
            //sender has to be in the first layer
            PKI.layerUserMap.put(Main.username.get(),1);
            System.out.println("Who do you want to send the message to?");
            String sendTo = sc.nextLine();
            //receiver has to be in the last layer
            PKI.layerUserMap.put(sendTo,PKI.numLayers);
            return sendTo;
        }else {
            return enterReceiver();
        }
    }

    public String enterMessage(){
        System.out.println("Enter message: ");
        return sc.nextLine();
    }


}
