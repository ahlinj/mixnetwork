public class Message {
    long timestamp;
    String signature;
    String sender;
    String body;


    public Message(String body) {
        this.body = body;
        this.sender = ""; // public key
        this.signature = ""; // ecc sign
        this.timestamp = System.currentTimeMillis();
    }
}
