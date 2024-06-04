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
    private PrintWriter writer;
    public ClientHandler(Socket socket) {
        this.socket=socket;
        Database.getDatabase().getClients().add(this);
        try
        {
            writer=new PrintWriter(this.getSocket().getOutputStream());
        }catch (Exception exception){}
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
            writer.println("connected");
            writer.flush();
            writer.write(Database.getDatabase().showPreviousMessages());
            writer.flush();
            while ((massage=bufferedReader.readLine())!=null){
                if(massage.compareTo("exit")==0)
                    break;
                else if(massage.compareTo("")==0)
                {
                    writer.println("connected");
                    writer.flush();
                }
                else if(massage.compareTo("search")==0){
                    String function=bufferedReader.readLine();
                    String[] orders=function.split(" ");
                    if(orders.length>1){
                        writer.write(Database.getDatabase().searchTime(orders[0],orders[2]));
                        writer.flush();
                    }
                    else{
                        writer.write(Database.getDatabase().searchName(function));
                        writer.flush();
                    }
                }
                else if(massage.compareTo("pv")==0){
                    ;
                }
                else
                {
                    num=Database.getDatabase().getMaxNum()+1;
                    Database.getDatabase().saveMassage(num,this.ID,massage);
                    for(ClientHandler client: Database.getDatabase().getClients()){
                        if(client!=null && client.getID().compareTo(this.ID)!=0){
                            writer=new PrintWriter(client.getSocket().getOutputStream());
                            writer.write(this.getUserName()+":\n"+massage+"\n");
                            writer.flush();
                        }
                    }
                }
            }
            Database.getDatabase().getClients().remove(this);
            this.socket.close();
            writer.close();
            if(Database.getDatabase().getClients().size()==0)
                Server.getServerSocket().close();
        }
         catch (Exception e) {
             System.out.println(e.getMessage());
         }
    }
}
