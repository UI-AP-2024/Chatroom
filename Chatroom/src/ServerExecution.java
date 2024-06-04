import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerExecution {
    private static ArrayList<User> users = new ArrayList<User>();
    private static ArrayList<ClientThread> clientsThread = new ArrayList<ClientThread>();
    private static ArrayList<Massage> pvMessages = new ArrayList<>();

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static void setUsers(ArrayList<User> users) {
        ServerExecution.users = users;
    }

    public static ArrayList<ClientThread> getClientsThread() {
        return clientsThread;
    }

    public static ArrayList<Massage> getPvMessages() {
        return pvMessages;
    }


    public static void setClientsThread(ArrayList<ClientThread> clientsThread) {
        ServerExecution.clientsThread = clientsThread;
    }

    public static void setPvMessages(ArrayList<Massage> pvMessages) {
        ServerExecution.pvMessages = pvMessages;
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