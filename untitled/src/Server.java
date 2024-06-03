import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args)throws Exception {
        try {
            Database.getDatabase().makeConnection();
        } catch (Exception e) {
            throw e;
        }
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while (true){
                Socket socket=serverSocket.accept();
                ClientHandler clientHandler=new ClientHandler(socket);
                clientHandler.start();
            }
        } catch (IOException e) {
            throw e;
        }
//        Database.getDatabase().finish();
    }
}