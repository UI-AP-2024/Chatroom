package com.example.chatroomserver;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.*;

public class HelloApplication extends Application implements Runnable {
    public HelloApplication() throws IOException {

    }
    @Override
    public void start(Stage stage) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);
        //Socket socket = serverSocket.accept();
        ResultSet resultSet = Database.chatroomMessages(Database.connectToDatabase());
        while (resultSet.next()) {
            System.out.println(resultSet.getString(2));
        }
        serverSocket.close();
    }
    @Override
    public void run(){

    }
}