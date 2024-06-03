import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args)throws Exception {
        try {
            Database.getDatabase().makeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while (true){
                Socket socket=serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(socket);
                clientHandler.start();
                if(Database.getDatabase().getClients().isEmpty())
                    break;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        Database.getDatabase().finish();
    }
}