import java.sql.*;
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
        String sqlcmd=String.format("INSERT INTO messages (ID,mes,num) VALUES ('%s' ,'%s',%d)",id,message,num);
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
        return 0;
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
}
