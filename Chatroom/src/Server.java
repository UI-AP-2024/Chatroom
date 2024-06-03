import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<String> usernames = new ArrayList<>();
    private static ArrayList<ClientThread> clientsThread = new ArrayList<ClientThread>();
    private static ArrayList<Massage> pvMessages = new ArrayList<>();

    public static ArrayList<String> getUsernames() {
        return usernames;
    }

    public static ArrayList<ClientThread> getClientsThread() {
        return clientsThread;
    }

    public static ArrayList<Massage> getPvMessages() {
        return pvMessages;
    }

    public static void setUsernames(ArrayList<String> usernames) {
        Server.usernames = usernames;
    }

    public static void setClientsThread(ArrayList<ClientThread> clientsThread) {
        Server.clientsThread = clientsThread;
    }

    public static void setPvMessages(ArrayList<Massage> pvMessages) {
        Server.pvMessages = pvMessages;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8999);
        while (true) {
            System.out.println("Waiting for connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Accepted connection from: " + socket.getRemoteSocketAddress());
            ClientThread newClient=new ClientThread(socket);
            clientsThread.add(newClient);
            newClient.start();
        }
    }
}