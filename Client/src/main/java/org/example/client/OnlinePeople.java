package org.example.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class OnlinePeople implements Initializable {
    public static Socket socket;

    @FXML
    private GridPane peopleGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("online-people");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        new Thread(() -> {
            try {
                showPeople();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        },"onlinePeople").start();
    }

    public void showPeople() throws IOException {
        while (true) {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String[] strings = dataInputStream.readUTF().split("-");
            Platform.runLater(() -> {
                for (int i = 0; i < strings.length - 1; i++) {
                    Label label = new Label(strings[i++]);
                    label.setFont(Font.font(25));
                    BorderPane borderPane = new BorderPane(label);
                    borderPane.setId(strings[i]);
                    label.setTextAlignment(TextAlignment.CENTER);
                    borderPane.setOnMouseClicked(event -> {
                        System.out.println(borderPane.getId());
                    });
                    peopleGridPane.add(borderPane, 0, i);
                }
            });
        }
    }
}
