import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static String name="harry";
    private static String ID="jeonharry";
    private static boolean run=true;
    public static void main(String[] args) throws IOException {
        Client.socket=new Socket("127.0.0.1",1234);
        new Thread(){
            public void run()
            {
                while (run)
                {
                    try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));)
                    {
                        String mess;
                        while ((mess=bufferedReader.readLine())!=null)
                            System.out.println(mess);
                    }catch (Exception exception){}
                }
            }
        }.start();
        View.getView().start();
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        Client.socket = socket;
    }

    public static void setID(String ID) {
        Client.ID = ID;
    }

    public static void setName(String name) {
        Client.name = name;
    }

    public static String getName() {
        return name;
    }

    public static String getID() {
        return ID;
    }

    public static void setRun(boolean run) {
        Client.run = run;
    }

    public static boolean isRun() {
        return run;
    }
}