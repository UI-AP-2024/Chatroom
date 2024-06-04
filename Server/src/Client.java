
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client implements Runnable{
    private static ArrayList<Socket> sockets = new ArrayList<>();
    private static int IDMaker = 1;
    private Socket socket;
    private String name;
    private int ID;

    public Client(String name,Socket socket) {
        this.name = name;
        this.ID = IDMaker++;
        this.socket = socket;
        sockets.add(socket);
    }

    @Override
    public void run() {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(name + " is connected");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        while (socket.isConnected()) {
            try {
                recognizeCommand((new DataInputStream(socket.getInputStream())).readUTF());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void recognizeCommand(String message) throws IOException {
        String[] command = message.split("-");
        switch (command[0]){
            case "send message" -> {
                showMessage(command[1]);
            }
        }
    }

    public void showMessage(String string) throws IOException {
        for (Socket socket : sockets){
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("show message-"+string);
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
