public class Massage {
    private String text;
    private String sender;
    private String receiver;
    private MassageType type;
    public Massage(String text, String sender, String receiver, MassageType type) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setType(MassageType type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public MassageType getType() {
        return type;
    }
}
