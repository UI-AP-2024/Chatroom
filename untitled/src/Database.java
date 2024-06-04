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
        String sqlcmd=String.format("INSERT INTO messages (ID,mes,num,time) VALUES ('%s' ,'%s',%d,'%s')",id,message,num,temp);
        Statement statement=con.prepareStatement(sqlcmd);
        statement.execute(sqlcmd);
    }
    public int getMaxNum() throws SQLException {
        String query="SELECT MAX(num) from messages";
        Statement s=con.prepareStatement(query);
        ResultSet rs=s.executeQuery(query);
        while (rs.next()){
            return rs.getInt(1);
        }
        return -1;
    }
    public void login(String name,String ID)
    {
        try{
            String query="SELECT ID From users";
            Statement statement=con.prepareStatement(query);
            ResultSet rs=statement.executeQuery(query);
            boolean exist=false;
            while (rs.next())
            {
                if(rs.getString("ID").compareTo(ID)==0)
                    exist=true;
            }
            if(!exist)
            {
                String cmd=String.format("INSERT INTO users (name,ID) VALUES ('%s','%s')",name,ID);
                statement=con.prepareStatement(cmd);
                statement.execute(cmd);
            }
        }catch (Exception exception){

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
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
        return answer.toString();
    }

//    public String getOnlineUsers(){
//        StringBuilder
//        for(ClientHandler client : clients){
//
//        }
//    }
}
