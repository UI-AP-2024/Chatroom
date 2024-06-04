
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
public class Client implements Runnable{
    private static int IDMaker = 1;
    private Socket socket;
    private String name;
    private int ID;

    public Client(String name) {
        this.name = name;
        this.ID = IDMaker++;
    }

    @Override
    public void run() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("connected" + name);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }




    public static int getIDMaker() {
        return IDMaker;
    }

    public static void setIDMaker(int IDMaker) {
        Client.IDMaker = IDMaker;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
