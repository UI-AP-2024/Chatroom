import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static Socket socket;
    public static void main(String[] args) throws IOException {
        while(true){
            ServerSocket serverSocket = new ServerSocket(1324);
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            Client client = new Client(dataInputStream.readUTF());
            client.run();
        }
    }
}