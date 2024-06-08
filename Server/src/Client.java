

import lombok.Getter;
import lombok.Setter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@Getter
@Setter
public class Client implements Runnable{
    static ArrayList<Socket> sockets = new ArrayList<>();
    static ArrayList<Client> clients = new ArrayList<>();
    static ArrayList<Message> messages = new ArrayList<>();
    static ArrayList<PvMessage> pvMessages = new ArrayList<>();
    private static int IDMaker = 1;
    private Socket socket;
    private String name;
    private int ID;
    private String password;


    public Client(String name,Socket socket, String password) throws SQLException {
        this.name = name;
        this.ID = IDMaker++;
        this.socket = socket;
        this.password = password;
        sockets.add(socket);
        clients.add(this);
        Database.addClient(this.ID, name, password);
    }
    public Client(String name, String password) {
        this.name = name;
        this.ID = IDMaker++;
        this.password = password;
        clients.add(this);
    }

    @Override
    public void run() {
        while (socket.isConnected()) {
            try {
                recognizeCommand((new DataInputStream(socket.getInputStream())).readUTF());
            } catch (IOException | ParseException | SQLException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
    public void recognizeCommand(String message) throws IOException, ParseException, SQLException {
        String[] command = message.split("-");
        switch (command[0]){
            case "send message" -> {
                if (Objects.equals(command[1], "whisper"))
                    sendWhisper(command);
                else {
                    Message m = new Message(command[1], this.ID);
                    messages.add(m);
                    Database.addMessage(m.getContent(), String.valueOf(m.getTime()), m.getSentByID());
                    showMessage(command[1]);
                }
            }
            case "search" -> {
                switch (command[1]) {
                    case "person"-> searchPerson(command[2]);
                    case "time" -> searchTime(command[2], command[3]);
                }
            }
            case "online" -> showOnlinePeople();
            case "ping" -> sendPing();
            case "pv" -> {
                if (Objects.equals(command[1], "message"))
                    sendToPv(command);
                else
                    sendPvPreviousMessages(command[2]);
            }
            case "waitThread" -> {
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF("waitThread");
            }
            case "all" -> sendRoomPreviousMessages();
        }
    }

    public void sendRoomPreviousMessages() throws IOException {
        for (Socket s : sockets) {
            DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
            for (Message message : messages) {
                String name = "";
                if (s == this.socket) {
                    for (Client client : clients)
                        if (message.getSentByID() == client.getID())
                            name = client.getName();
                    dataOutputStream.writeUTF("message-your-" + message.getContent() + "-" + name);
                }
                else {
                    for (Client client : clients)
                        if (message.getSentByID() == client.getID())
                            name = client.getName();
                    dataOutputStream.writeUTF("message-other-" + message.getContent() + "-" + name);
                }
            }
        }
    }

    public void sendWhisper(String[] command) throws IOException{
        String content = command[3];
        String name = command[2];
        Socket s = null;
        for (Client client : clients)
            if (Objects.equals(client.getName(), name)) {
                s = client.socket;
                break;
            }
        DataOutputStream dataOutputStream;
        for (Socket socket : sockets){
            dataOutputStream = new DataOutputStream (socket.getOutputStream());
            if ( socket == this.socket)
                dataOutputStream.writeUTF("whisper-your-" + content + "-" + this.getName());
            else if(socket == s)
                dataOutputStream.writeUTF("whisper-other-" + content + "-" + this.getName());
            else
                dataOutputStream.writeUTF("whisper-them-" + content + "-" + this.getName());
        }
    }

    public void sendPvPreviousMessages(String name) throws IOException{
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        boolean check = false;
        for (PvMessage pvMessage : pvMessages) {
            for (Client client : clients)
                if (Objects.equals(client.getName(), name))
                    if (pvMessage.getSentByID() == this.getID() && pvMessage.getSentToID() == client.getID()) {
                        dataOutputStream.writeUTF("pv-" + "your-" + pvMessage.getContent());
                        check = true;
                    }
                    else if (pvMessage.getSentToID() == this.getID() && pvMessage.getSentByID() == client.getID()) {
                        dataOutputStream.writeUTF("pv-" + "other-" + pvMessage.getContent());
                        check = true;
                    }
        }
        if (!check)
            dataOutputStream.writeUTF("null");
    }

    public void sendToPv(String[] message) throws ParseException, IOException, SQLException {
        DataOutputStream dataOutputStream1 = new DataOutputStream(socket.getOutputStream());
        for (Client client : clients) {
            if (Objects.equals(client.getName(), message[3])) {
                DataOutputStream dataOutputStream = new DataOutputStream(client.getSocket().getOutputStream());
                PvMessage pvMessage = new PvMessage(message[2], this.ID, client.getID());
                pvMessages.add(pvMessage);
                Database.addPvMessage(pvMessage.getContent(), String.valueOf(pvMessage.getTime()), pvMessage.getSentByID(), pvMessage.getSentToID());
                dataOutputStream1.writeUTF("pv-" + "your-" + message[2]);
                if (client.getSocket().isConnected()) {
                    dataOutputStream.writeUTF("pv-" + "other-" + message[2]);
                }
                break;
           }
       }
    }

    public void sendPing() throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("ping");
    }
    public void showOnlinePeople() throws IOException {
        StringBuilder result = new StringBuilder("people");
        for (Client client : clients){
            if (client.getSocket().isConnected() && this.socket!= client.socket){
                result.append("-").append(client.getName()).append("-").append(client.getID());
            }
        }
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(result.toString());

    }
    public void searchPerson(String person) throws IOException {
        StringBuilder result = new StringBuilder("person");
        for (Client clientReader : clients) {
            if (Objects.equals(clientReader.getName(), person)) {
                for (Message msg : messages) {
                    if (Objects.equals(msg.getSentByID(), clientReader.getID()))
                        result.append("-").append(msg.getContent());
                }
            }
        }
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(result.toString());
    }
    public void searchTime(String start, String end) throws IOException {
        StringBuilder result = new StringBuilder("time");
        for (Message msg : messages) {
            if (msg.getTime().isAfter(LocalTime.parse(start)) && msg.getTime().isBefore(LocalTime.parse(end))) {
                result.append("-").append(msg.getContent()).append("-");
                for (Client client : clients) {
                    if (client.getID() == msg.getSentByID()) {
                        result.append(client.getName());
                        break;
                    }
                }
            }
        }
        System.out.println(result);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF(result.toString());
    }
    public void showMessage(String string) throws IOException{
        for (Socket socket : sockets){
            DataOutputStream dataOutputStream = new DataOutputStream (socket.getOutputStream());
            if ( socket != this.socket )
                dataOutputStream.writeUTF("message-other-" + string + "-" + this.getName());
            else
                dataOutputStream.writeUTF("message-your-" + string + "-" + this.getName());
        }
    }

    public static void handleLoginAndSignup(String input, Socket socket) throws SQLException {
        String[] strings = input.split("-");
        if (Objects.equals(strings[0],"login")){
            login(strings[1], strings[2], socket);
        }
        else if (Objects.equals(strings[0],"signup")) {
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

    public static void signup(String name, String password, Socket socket) throws SQLException {
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
