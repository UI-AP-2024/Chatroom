import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static Socket socket;
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            Client client = new Client(dataInputStream.readUTF(),socket);
            Thread thread = new Thread(client);
            thread.start();
        }
    }
}