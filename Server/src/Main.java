import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static Socket socket;
    public static void main(String[] args) throws IOException {
        int counter = 1234;
        while (true) {
            ServerSocket serverSocket = new ServerSocket(counter++);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            Client client = new Client(dataInputStream.readUTF(),socket);
            Thread thread = new Thread(client);
            thread.start();
        }
    }
}