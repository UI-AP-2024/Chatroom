import java.util.ArrayList;

public class User {
    private String username;
    private String ID;
    private ArrayList<Massage> unSeenMessages=new ArrayList<Massage>();
    private boolean online=true;
    private boolean pvChat=false;

    public void setUnSeenMessages(ArrayList<Massage> unSeenMessages) {
        this.unSeenMessages = unSeenMessages;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setPvChat(boolean pvChat) {
        this.pvChat = pvChat;
    }

    public ArrayList<Massage> getUnSeenMessages() {
        return unSeenMessages;
    }

    public boolean isOnline() {
        return online;
    }

    public boolean isPvChat() {
        return pvChat;
    }

    public String getUsername() {
        return username;
    }

    public String getID() {
        return ID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public User(String username, String ID) {
        this.username = username;
        this.ID = ID;
    }
}
