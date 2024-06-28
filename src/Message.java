import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    long timestamp;
    String sender;
    String body;
    String route;
    private static final char PADDING_CHAR = '*';



    public Message(String body, String sender, String route) {
        this.body = body;
        this.sender = sender;
        this.timestamp = System.currentTimeMillis();
        this.route = route;
    }

    public static Message addRouteInfo(Message message, String addRoute){
        return new Message(
                message.body,
                message.sender,
                addRoute+"::"+message.route
        );
    }
    public static Message deleteRouteInfo(Message message){
        String[] parts = message.route.split("::");
        String save = parts[1];


        return new Message(
                message.body,
                message.sender,
                save
        );
    }

    public static String addPadding(String data, int FIXED_LENGTH){
        StringBuilder paddedData = new StringBuilder(data);
        while (paddedData.length() < FIXED_LENGTH) {
            paddedData.append(PADDING_CHAR);
        }
        return paddedData.toString();
    }

    public static String removePadding(String data){
        int paddingStartIndex = data.indexOf(PADDING_CHAR);
        if (paddingStartIndex == -1) {
            return data;
        }
        return data.substring(0, paddingStartIndex);
    }

}
