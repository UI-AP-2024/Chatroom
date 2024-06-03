import java.io.IOException;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static String name="mmd";
    private static String ID="mmd24";
    public static void main(String[] args) throws IOException {
        Client.socket=new Socket("127.0.0.1",1234);
        View.getView().start();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public static void setID(String ID) {
        Client.ID = ID;
    }

    public static void setName(String name) {
        Client.name = name;
    }

    public static String getName() {
        return name;
    }

    public static String getID() {
        return ID;
    }
}