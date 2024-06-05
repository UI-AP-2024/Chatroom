
import lombok.Getter;
import lombok.Setter;

import javax.naming.ldap.SortKey;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.MessageDigestSpi;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
public class Client implements Runnable{
    final private static ArrayList<Socket> sockets = new ArrayList<>();
    final private static ArrayList<Client> clients = new ArrayList<>();
    final private static ArrayList<Message> messages = new ArrayList<>();
    private static int IDMaker = 1;
    private Socket socket;
    private String name;
    private int ID;
    private String password;


    public Client(String name,Socket socket, String password) {
        this.name = name;
        this.ID = IDMaker++;
        this.socket = socket;
        this.password = password;
        sockets.add(socket);
        clients.add(this);
    }
    @Override
    public void run() {
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
                addMessage(command[1], true, this);
                showMessage(command[1]);
            }
            case "search" -> {
                switch (command[1]) {
                    case "person"-> {
                        ArrayList<Message> result = new ArrayList<>();
                        for (Message msg : messages) {
                            if (Objects.equals(msg.getSentBy().getName(), command[2]))
                                result.add(msg);
                        }
                    }
                    case "time" -> {

                    }
                }
            }
        }
    }
    public void searchResult(ArrayList<Message> messages) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(messages);
    }
    public void addMessage(String content,  boolean value, Client sentBy) {
        messages.add(new Message(content, value, sentBy));
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
                    sockets.remove(client.socket);
                    client.socket = socket;
                    sockets.add(socket);
                    client.run();
                }
            }
        }
    }

    public static void signup(String name, String password, Socket socket) {
        for (Client client : clients) {
            if (Objects.equals(name, client.getName())) {
                return;
            }
        }
        Client client = new Client(name, socket, password);
        Thread thread = new Thread(client);
        thread.start();
    }
}
