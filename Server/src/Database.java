import java.sql.*;
import java.text.ParseException;

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
        String sqlFormat = String.format("INSERT INTO  Client (ID, Name, Password) VALUES (%s, '%s', '%s')", ID, name, pass);
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
    }

    public static void addMessage(String content, String time, int sentBy) throws SQLException {
        String sqlFormat = String.format("INSERT INTO  Message (Content, Time, SentBy) VALUES ('%s', '%s', %s)", content, time, sentBy);
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
    }

    public static void addPvMessage(String content, String time, int sentBy, int sentTo) throws SQLException {
        String sqlFormat = String.format("INSERT INTO  PvMessage (Content, Time, SentBy, SentTo) VALUES ('%s', '%s', %s, %s)", content, time, sentBy, sentTo);
        Statement statement = connection.prepareStatement(sqlFormat);
        statement.execute(sqlFormat);
    }

    //---------------------
    public static void addClientsToList() throws SQLException {
        String sqlFormat = String.format("SELECT * FROM Client");
        Statement statement = connection.prepareStatement(sqlFormat);
        ResultSet resultSet = statement.executeQuery(sqlFormat);
        while (resultSet.next()) {
            new Client(resultSet.getString("Name"), resultSet.getString("Password"));
        }
    }

    public static void addMessageToList() throws SQLException, ParseException {
        String sqlFormat = String.format("SELECT * FROM Message");
        Statement statement = connection.prepareStatement(sqlFormat);
        ResultSet resultSet = statement.executeQuery(sqlFormat);
        while (resultSet.next()) {
            Client.messages.add(new Message(resultSet.getString("Content"), resultSet.getInt("SentBy"), resultSet.getString("Time")));
        }
    }

    public static void addPvMessageToList() throws SQLException, ParseException {
        String sqlFormat = String.format("SELECT * FROM PvMessage");
        Statement statement = connection.prepareStatement(sqlFormat);
        ResultSet resultSet = statement.executeQuery(sqlFormat);
        while (resultSet.next()) {
            Client.messages.add(new PvMessage(resultSet.getString("Content"), resultSet.getInt("SentBy"), resultSet.getInt("SentTo"), resultSet.getString("Time")));
        }
    }
}