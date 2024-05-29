package com.example.chatroomserver;

import java.sql.*;

public class Database {
    public static Connection connectToDatabase() throws ClassNotFoundException, SQLException {
        String URL = "jdbc:mysql://localhost/chatroom";
        String UserName = "root";
        String Password = "1234";
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection(URL, UserName, Password);
        return connection;
    }

    // ای دی چت روم -1 است
    public static ResultSet chatroomMessages(Connection connection) throws SQLException {
        String sql = "SELECT senderID, message FROM chats WHERE receiverID = -1";
        Statement statement = connection.prepareStatement(sql);
        return statement.executeQuery(sql);
    }
}
