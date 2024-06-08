import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    static Connection connection;
    public static void executeSQL() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String PASS = "1234";
        String USERNAME = "Masih-Mohsen";
        String URL = "jdbc:mysql://localhost/silent-room";
        connection = DriverManager.getConnection(URL, USERNAME, PASS);
        System.out.println("Connected");
    }
    public static void addClient(int ID, String name, String pass) throws SQLException {
        System.out.println(ID + "  " + name + "   " + pass);
        String sqlFormat = String.format("INSERT INTO  client (ID, Name, Password) VALUES (%s, '%s', '%s')", ID, name, pass);
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
    }
    public void addMessage() {

    }
    public void addPvMessage() {

    }
}
