import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.util.ArrayList;

public class ClientHandler extends Thread{
    private Socket socket;
    private String ID;
    private String name;
    private int num;
    private int lastSeenNum;
    private PrintWriter writer;
    private boolean pvEnd=false;
    private LocalTime lastTimeInChatroom=null;
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

    public int getLastSeenNum() {
        return lastSeenNum;
    }

    public void setLastSeenNum(int lastSeenNum) {
        this.lastSeenNum = lastSeenNum;
    }

    public LocalTime getLastTimeInChatroom() {
        return lastTimeInChatroom;
    }

    public void setLastTimeInChatroom(LocalTime lastTimeInChatroom) {
        this.lastTimeInChatroom = lastTimeInChatroom;
    }

    @Override
    public void run(){
        try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));){
            do {
                this.ID = bufferedReader.readLine();
                this.name = bufferedReader.readLine();
                if(!Database.getDatabase().login(name,ID))
                {
                    writer.println("Wrong Information");
                    writer.flush();
                }
            }while (!Database.getDatabase().login(name,ID));
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
                        writer.write(Database.getDatabase().getOnlineUsers(this.ID));
                        writer.flush();
                        String ID=bufferedReader.readLine();
                        if(Database.getDatabase().userNameExist(ID))
                        {
                            if(Database.getDatabase().blockUser(this.ID,ID,false)){
                                writer.println("You are blocked!");
                                writer.flush();
                            }
                            else{
                                lastTimeInChatroom=LocalTime.now();
                                writer.println("pv started");
                                writer.flush();
                                writer.write(Database.getDatabase().showPreviousPVMessages(this,this.ID,ID));
                                writer.flush();
                                pvEnd=true;
                                new Thread(){
                                    public void run()
                                    {
                                        readingCurrentMessages(ID);
                                    }
                                }.start();
                                pv(ID);
                                writer.write(Database.getDatabase().unseenMessChatroom(this));
                                writer.flush();
                            }
                        }
                        else
                        {
                            writer.println("username Not Found Or is offline");
                            writer.flush();
                        }
                    }
                    else if(massage.compareTo("block")==0){
                        writer.write(Database.getDatabase().showAllUsers());
                        writer.flush();
                        String ID=bufferedReader.readLine();
                        Database.getDatabase().blockUser(ID,this.ID,true);
                    }
                    else
                    {
                        num=Database.getDatabase().getMaxNum("messages")+1;
                        Database.getDatabase().saveMassage(num,this.ID,massage);
                        for(ClientHandler client: Database.getDatabase().getClients()){
                            if(client!=null && client.getID().compareTo(this.ID)!=0 && !client.pvEnd){
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
            if(Database.getDatabase().getClients().isEmpty())
                Server.getServerSocket().close();
        }
         catch (Exception e) {
//             System.out.println("err "+e.getMessage());
         }
    }
    public void pv(String ID) throws Exception {
        try
        {
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String massage;
            while ((massage=bufferedReader.readLine())!=null)
            {
                if(massage.compareTo("finish")==0)
                {
                    pvEnd=false;
                    break;
                }
                else if(massage.compareTo("clear history")==0)
                {
                    writer.println("History Cleaned!");
                    Database.getDatabase().clearHistory(this,this.ID,ID);
                }
                else
                    Database.getDatabase().savePVMessages(this.ID,ID,massage);
            }
        }catch (Exception e){
         throw e;
        }
    }
    public void readingCurrentMessages(String ID)
    {
        while (true)
        {
            writer.write(Database.getDatabase().showCurrentMessage(this,this.ID,ID));
            writer.flush();
            if(!pvEnd)
                break;
        }
    }
}
