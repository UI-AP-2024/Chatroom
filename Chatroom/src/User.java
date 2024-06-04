public class User {
    private String username;
    private String ID;

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
