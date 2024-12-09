import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException, ParseException {
        Database.executeSQL();
        Database.addClientsToList();
        Database.addMessageToList();
        Database.addPvMessageToList();
        ServerSocket serverSocket = new ServerSocket(1234);
        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String input = dataInputStream.readUTF();
            Client.handleLoginAndSignup(input, socket);
        }
    }
}