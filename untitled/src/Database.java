import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Database
{
    private String url="jdbc:mysql://localhost/chatroom";
    private String userName="acc";
    private String password="1234";
    private static Database database;
    private Connection con;
    private ArrayList<ClientHandler> clients=new ArrayList<>();
    private Database(){}
    public static Database getDatabase() {
        if(database==null)
            database=new Database();
        return database;
    }

    public ArrayList<ClientHandler> getClients() {
        return clients;
    }

    public void setClients(ArrayList<ClientHandler> clients) {
        this.clients = clients;
    }

    public void makeConnection() throws Exception{
        Class.forName("com.mysql.cj.jdbc.Driver");
        con= DriverManager.getConnection(url,userName,password);
    }
    public void finish()throws Exception
    {
        con.close();
    }
    public void saveMassage(int num,String id,String message)throws Exception
    {
        LocalTime time=LocalTime.now();
        String temp=getTimeString(time);
        String sqlcmd=String.format("INSERT INTO messages (ID,mes,num,time) VALUES ('%s' ,'%s',%d,'%s')",id,message,num,temp);
        Statement statement=con.prepareStatement(sqlcmd);
        statement.execute(sqlcmd);
    }
    public int getMaxNum(String tableName) throws SQLException {
        String query="SELECT MAX(num) from "+tableName;
        Statement s=con.prepareStatement(query);
        ResultSet rs=s.executeQuery(query);
        while (rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }
    public boolean login(String name,String ID)
    {
        try{
            String query="SELECT ID,name From users";
            Statement statement=con.prepareStatement(query);
            ResultSet rs=statement.executeQuery(query);
            boolean exist=false;
            boolean rightName=true;
            while (rs.next())
            {
                if(rs.getString("ID").compareTo(ID)==0)
                {
                    if(rs.getString("name").compareTo(name)!=0)
                        rightName=false;
                    exist=true;
                }
            }
            if(exist)
            {
                if(rightName)
                    return true;
                else
                    return false;
            }
            else
            {
                String cmd=String.format("INSERT INTO users (name,ID) VALUES ('%s','%s')",name,ID);
                statement=con.prepareStatement(cmd);
                statement.execute(cmd);
                return true;
            }
        }catch (Exception exception){

            return false;
        }
    }
    public String searchName(String name){
        StringBuilder answer=new StringBuilder("Massages from "+name+":\n");
        try {
            String queryUsers="SELECT ID from users WHERE name='"+name+"'";
            Statement sUser=con.prepareStatement(queryUsers);
            ResultSet users=sUser.executeQuery(queryUsers);
            while (users.next()){
            String query="SELECT mes,ID from messages WHERE ID='"+users.getString("ID")+"' ORDER BY num ASC";
            Statement s=con.prepareStatement(query);
            ResultSet rs=s.executeQuery(query);
            while (rs.next())
                answer.append(rs.getString("ID")).append(" : ").append(rs.getString("mes")).append("\n");
            }
        }catch (Exception e){}
        return answer.toString();
    }
    public String searchTime(String time1, String time2){
        LocalTime startTime=LocalTime.parse(time1);
        LocalTime endTime=LocalTime.parse(time2);
        StringBuilder answer=new StringBuilder("Massages from "+time1+" to "+time2+":\n");
        try {
            String query="SELECT mes,time,ID from messages ORDER BY num ASC";
            Statement s=con.prepareStatement(query);
            ResultSet rs=s.executeQuery(query);
            while (rs.next()){
                LocalTime time=LocalTime.parse(rs.getString("time"));
                if(time.isAfter(startTime) && time.isBefore(endTime))
                    answer.append(rs.getString("ID")).append(" : ").append(rs.getString("mes")).append("\n");
                else if (time.compareTo(startTime) == 0 && time.isBefore(endTime)) {
                    answer.append(rs.getString("ID")).append(" : ").append(rs.getString("mes")).append("\n");
                } else if (time.isAfter(startTime) && time.compareTo(endTime) == 0) {
                    answer.append(rs.getString("ID")).append(" : ").append(rs.getString("mes")).append("\n");
                } else if (time.compareTo(startTime) == 0 && time.compareTo(endTime) == 0) {
                    answer.append(rs.getString("ID")).append(" : ").append(rs.getString("mes")).append("\n");
                }
            }
        }catch (Exception e){

        }
        return answer.toString();
    }
    public String showPreviousMessages()
    {
        StringBuilder answer=new StringBuilder();
        try {
            String queryMess="SELECT ID,mes from messages ORDER BY num ASC";
            Statement s=con.prepareStatement(queryMess);
            ResultSet rsMess=s.executeQuery(queryMess);
            while (rsMess.next()){
                String queryUsers="SELECT name,ID from users WHERE users.ID='"+rsMess.getString("ID")+"'";
                Statement sUser=con.prepareStatement(queryUsers);
                ResultSet user=sUser.executeQuery(queryUsers);
                if(user.next())
                    answer.append(user.getString("name")).append(" :\n");
                answer.append(rsMess.getString("mes")).append("\n");
            }
        }catch (Exception e){

        }
        return answer.toString();
    }
    public String getOnlineUsers(String meUserName){
        StringBuilder answer= new StringBuilder("--------------online users----------------\n");
        for(ClientHandler client : clients)
            if(client!=null && client.getID().compareTo(meUserName)!=0)
                answer.append(client.getID()).append("\n");
        answer.append("------------------------------------------\n");
        return answer.toString();
    }
    public boolean userNameExist(String ID)
    {
        for(ClientHandler client :clients)
            if(client!=null && client.getID().compareTo(ID)==0)
                return true;
        return false;
    }
    public void savePVMessages(String senderID,String receiverID,String mess)throws Exception
    {
        int num=getMaxNum("pv");
        try {
            String message=String.format("INSERT into PV (num,senderID,recieverID,mess) VALUES (%d,'%s','%s','%s')",num+1,senderID,receiverID,mess);
            Statement s=con.prepareStatement(message);
            s.execute(message);
        }catch (Exception e){

        }
    }

    public String showPreviousPVMessages(ClientHandler caller,String senderID,String receiverID){
        StringBuilder answer=new StringBuilder();
        try {
            String queryMess="SELECT senderID,recieverID,mess,num from pv ORDER BY num ASC";
            Statement s=con.prepareStatement(queryMess);
            ResultSet rsMess=s.executeQuery(queryMess);
            while (rsMess.next()){
                if(rsMess.getString("senderID").compareTo(senderID)==0 && rsMess.getString("recieverID").compareTo(receiverID)==0)
                {
                    answer.append("You: ");
                    answer.append(rsMess.getString("mess")).append("\n");
                    caller.setLastSeenNum(rsMess.getInt("num"));
                }
                else if(rsMess.getString("senderID").compareTo(receiverID)==0 && rsMess.getString("recieverID").compareTo(senderID)==0){
                    String queryUsers="SELECT name from users WHERE users.ID='"+rsMess.getString("senderID")+"'";
                    Statement sUser=con.prepareStatement(queryUsers);
                    ResultSet user=sUser.executeQuery(queryUsers);
                    if(user.next())
                        answer.append(user.getString("name")).append(" : ");
                    answer.append(rsMess.getString("mess")).append("\n");
                    caller.setLastSeenNum(rsMess.getInt("num"));
                }
            }
        }catch (Exception e){

        }
        return answer.toString();
    }
    public String showCurrentMessage(ClientHandler caller,String senderID,String receiverID){
        StringBuilder answer=new StringBuilder();
        try {
            String queryMess="SELECT senderID,recieverID,mess,num from pv WHERE num>"+caller.getLastSeenNum()+" ORDER BY num ASC";
            Statement s=con.prepareStatement(queryMess);
            ResultSet rsMess=s.executeQuery(queryMess);
            while (rsMess.next()){
                if(rsMess.getString("senderID").compareTo(receiverID)==0 && rsMess.getString("recieverID").compareTo(senderID)==0){
                    String queryUsers="SELECT name from users WHERE users.ID='"+rsMess.getString("senderID")+"'";
                    Statement sUser=con.prepareStatement(queryUsers);
                    ResultSet user=sUser.executeQuery(queryUsers);
                    if(user.next())
                        answer.append(user.getString("name")).append(" : ");
                    answer.append(rsMess.getString("mess")).append("\n");
                    caller.setLastSeenNum(rsMess.getInt("num"));
                }
            }
        }catch (Exception e){

        }
        return answer.toString();
    }
    public void clearHistory(ClientHandler caller,String senderID,String receiverID){
        try {
            String queryMess="SELECT senderID,recieverID,mess,num from pv ORDER BY num ASC";
            Statement s=con.prepareStatement(queryMess);
            ResultSet rsMess=s.executeQuery(queryMess);
            while (rsMess.next()){
                if(rsMess.getString("senderID").compareTo(senderID)==0 && rsMess.getString("recieverID").compareTo(receiverID)==0)
                {
                    String cmd=String.format("DELETE from pv WHERE senderID='%s' AND recieverID='%s'",senderID,receiverID);
                    Statement statement=con.prepareStatement(cmd);
                    statement.execute(cmd);
                }
                else if(rsMess.getString("senderID").compareTo(receiverID)==0 && rsMess.getString("recieverID").compareTo(senderID)==0){
                    String cmd=String.format("DELETE from pv WHERE senderID='%s' AND recieverID='%s'",receiverID,senderID);
                    Statement statement=con.prepareStatement(cmd);
                    statement.execute(cmd);
                }
            }
            caller.setLastSeenNum(0);
        }catch (Exception e){

        }
    }

    public String unseenMessChatroom(ClientHandler caller)
    {
        StringBuilder answer=new StringBuilder();
        try {
            String queryMess="SELECT ID,mes,time from messages ORDER BY num ASC";
            Statement s=con.prepareStatement(queryMess);
            ResultSet rsMess=s.executeQuery(queryMess);
            while (rsMess.next()){
                LocalTime tempTime=LocalTime.parse(rsMess.getString("time"));
                if(tempTime.isAfter(caller.getLastTimeInChatroom()))
                {
                    String queryUsers="SELECT name,ID from users WHERE users.ID='"+rsMess.getString("ID")+"'";
                    Statement sUser=con.prepareStatement(queryUsers);
                    ResultSet user=sUser.executeQuery(queryUsers);
                    if(user.next())
                        answer.append(user.getString("name")).append(" :\n");
                    answer.append(rsMess.getString("mes")).append("\n");
                }
            }
        }catch (Exception e){

        }
        return answer.toString();
    }
    public String getTimeString(LocalTime time){
        String hour;
        String minute;
        String second;
        if(time.getHour() >=0 && time.getHour()<=9)
            hour="0"+time.getHour();
        else hour=String.valueOf(time.getHour());
        if(time.getMinute() >=0 && time.getMinute()<=9)
            minute="0"+time.getMinute();
        else minute=String.valueOf(time.getMinute());
        if(time.getSecond() >=0 && time.getSecond()<=9)
            second="0"+time.getSecond();
        else second=String.valueOf(time.getSecond());
        String temp=hour+":"+minute+":"+second;
        return temp;
    }

    public String showAllUsers() throws SQLException{
        StringBuilder answer=new StringBuilder("-----------------all users----------------\n");
        String sqlcmd="SELECT ID from users ";
        Statement statement=con.prepareStatement(sqlcmd);
        ResultSet resultSet=statement.executeQuery(sqlcmd);
        while (resultSet.next()){
            answer.append(resultSet.getString("ID")).append("\n");
        }
        answer.append("------------------------------------------\n");
        return answer.toString();
    }

    public boolean blockUser(String blockedID,String blockerID,boolean isInsert){
        try{
            String query="SELECT * From blockedusers ";
            Statement statement=con.prepareStatement(query);
            ResultSet rs=statement.executeQuery(query);
            boolean exist=false;
            while (rs.next())
            {
                if(rs.getString("blockedID").compareTo(blockedID)==0 && rs.getString("blockerID").compareTo(blockerID)==0)
                {
                    exist=true;
                }
            }
            if(exist)
            {
                return exist;
            }
            else
            {
                if(isInsert){
                    int num=getMaxNum("blockedusers");
                    String cmd=String.format("INSERT INTO blockedusers (num,blockedID,blockerID) VALUES (%d,'%s','%s')",num,blockedID,blockerID);
                    statement=con.prepareStatement(cmd);
                    statement.execute(cmd);
                    return true;
                }
                else return false;
            }
        }catch (Exception exception){
            return false;
        }
    }
}
