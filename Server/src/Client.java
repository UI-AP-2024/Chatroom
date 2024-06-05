
import javax.naming.ldap.SortKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

public class Client implements Runnable{
    private static ArrayList<Socket> sockets = new ArrayList<>();
    private static ArrayList<Client> clients = new ArrayList<>();
    private static int IDMaker = 1;
    private Socket socket;
    private String name;
    private int ID;
    private String password;

    public Client(String name,Socket socket, String password) {
        this.name = name;
        this.ID = IDMaker++;
        this.socket = socket;
        sockets.add(socket);
        this.password = password;
        clients.add(this);
    }


    @Override
    public void run() {
//        try {
//            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
//            dataOutputStream.writeUTF(name + " is connected");
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
        while (socket.isConnected()) {
            try {
                recognizeCommand((new DataInputStream(socket.getInputStream())).readUTF());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                break;
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
            if ( socket != this.socket) {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("other-message-" + string);
            }else{
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("your-message-" + string);
            }
        }
    }
    public static void handleLoginAndSignup(String input, Socket socket) {
        String[] strings = input.split("-");
        if (Objects.equals(strings[0], "login")){
            login(strings[1], strings[2], socket);
        }
        else if (Objects.equals(strings[0], "signup")) {
            signup(strings[1], strings[2], socket);
        }
    }
    public static void login(String name, String password, Socket socket) {
        for (Client client : clients) {
            if (Objects.equals(name, client.getName())) {
                if (Objects.equals(client.password, password)) {
                    client.socket = socket;
                    client.run();
                }
            }
        }
    }
    public static void signup(String name, String password, Socket socket) {
        boolean check = true;
        for (Client client : clients) {
            if (Objects.equals(name, client.getName())) {
                check = false;
            }
        }
        if (check) {
            Client client = new Client(name, socket, password);
            clients.add(client);
            Thread thread = new Thread(client);
            thread.start();
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

    public String getPassword() {
        return password;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
