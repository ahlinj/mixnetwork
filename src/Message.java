import java.io.Serializable;
import java.security.PublicKey;

public class Message implements Serializable {
    long timestamp;
    PublicKey sender;
    int portReceiver;
    String body;



    public Message(String body, PublicKey pk, int portReceiver) {
        this.body = body;
        this.sender = pk;
        this.portReceiver = portReceiver;
        this.timestamp = System.currentTimeMillis();
    }
}
