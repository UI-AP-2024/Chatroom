import lombok.Getter;
import lombok.Setter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.time.LocalTime;
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
            } catch (IOException | ParseException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }

    public void recognizeCommand(String message) throws IOException, ParseException {
        String[] command = message.split("-");
        switch (command[0]){
            case "send message" -> {
                addMessage(command[1], this.getID());
                showMessage(command[1]);
            }
            case "search" -> {
                switch (command[1]) {
                    case "person"-> searchPerson(command[2]);
                    case "time" -> searchTime(command[2], command[3]);

                }
            }
        }
    }
    public void searchPerson(String person) throws IOException {
        Client c = null;
        for (Client client : clients)
            if (Objects.equals(client.getName(), person))
                c = client;
        ArrayList<String> result = new ArrayList<>();
        for (Message msg : messages) {
            assert c != null;
            if (Objects.equals(msg.getSentByID(), c.ID))
                result.add(msg.getContent() + "-" + msg.getTime());
        }

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(result);
    }
    public void searchTime(String start, String end) throws IOException {

        ArrayList<String> result = new ArrayList<>();
        for (Message msg : messages)
            if (msg.getTime().isAfter(LocalTime.parse(start)) && msg.getTime().isBefore(LocalTime.parse(end))) {
                StringBuilder s = new StringBuilder();
                s.append(msg.getContent()).append("-").append(msg.getTime()).append("-");
                for (Client client : clients)
                    if (client.getID() == msg.getSentByID())
                        s.append(client.getName());
                result.add(s.toString());
            }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(result);
    }
    public void addMessage(String content, int sentByID) throws ParseException {
        messages.add(new Message(content, sentByID));
    }
    public void showMessage(String string) throws IOException {
        for (Socket socket : sockets){
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            if ( socket != this.socket)
                dataOutputStream.writeUTF("other-message-" + string + "-" + this.getName());
            else
                dataOutputStream.writeUTF("your-message-" + string);
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
