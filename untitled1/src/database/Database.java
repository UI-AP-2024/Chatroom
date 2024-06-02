package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database
{
    private String url="jdbc:mysql://localhost/chatroom";
    private String userName="acc";
    private String password="1234";
    private static Database database;
    private Connection con;
    private Database(){}
    public static Database getDatabase() {
        if(database==null)
            database=new Database();
        return database;
    }
    public void makeConnection() {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(url,userName,password);
        }catch (Exception exception)
        {
            System.out.println("error"+exception.getMessage());
        }
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
            System.out.println("error "+exception.getMessage());
        }
    }
    public void finish()
    {
        try{
            con.close();
        }catch (Exception exception){
            System.out.println("error"+exception.getMessage());
        }
    }
}
