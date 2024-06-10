import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    long timestamp;
    PublicKey sender;
    int portReceiver;
    String body;
    String route;



    public Message(String body, PublicKey pk, int portReceiver, String route) {
        this.body = body;
        this.sender = pk;
        this.portReceiver = portReceiver;
        this.timestamp = System.currentTimeMillis();
        this.route = route;
    }
}
