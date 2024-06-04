package org.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Statement;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.ResourceBundle;
public class ChatroomView implements Initializable {
    public static Socket socket;

    @FXML
    private TextField message;

    @FXML
    private GridPane messages;

    @FXML
    private Button sendButton;

    @FXML
    void messageAction(ActionEvent event) {

    }
    static int counter = 0;
    @FXML
    public void sendButtonAction(ActionEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        if (Objects.equals(message.getText(), ""))
            message.setText(" ");
        dataOutputStream.writeUTF("send message-" + message.getText());
        String[] input = dataInputStream.readUTF().split("-");
        Label label = new Label();
        switch (input[0]) {
            case "show message" -> {
                label.setText(input[1]);
            }
        }
        messages.add(label, 0, counter++);
        message.setText("");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
