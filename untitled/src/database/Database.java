package database;

import java.sql.Connection;
import java.sql.DriverManager;

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
    public void finish()
    {
        try{
            con.close();
        }catch (Exception exception){
            System.out.println("error"+exception.getMessage());
        }
    }
}
