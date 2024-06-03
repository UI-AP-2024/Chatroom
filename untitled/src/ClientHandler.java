import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket socket;
    private String ID;
    private String name;
    private int num;
    public ClientHandler(Socket socket) {
        this.socket=socket;
        Database.getDatabase().getClients().add(this);
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    @Override
    public void run(){
        try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));){
            this.ID=bufferedReader.readLine();
            this.name=bufferedReader.readLine();
            Database.getDatabase().login(name,ID);
            String massage;
            while ((massage=bufferedReader.readLine()).compareTo("exit")!=0){
                num=Database.getDatabase().getMaxNum()+1;
                Database.getDatabase().saveMassage(num,this.ID,massage);
                for(ClientHandler client: Database.getDatabase().getClients()){
                    if(client!=null && client.getID().compareTo(this.ID)!=0){
                        PrintWriter writer=new PrintWriter(client.getSocket().getOutputStream());
                        writer.write(this.getName()+":\n"+massage+"\n");
                    }
                }
            }
            Database.getDatabase().getClients().remove(this);
            this.socket.close();
        }
         catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
