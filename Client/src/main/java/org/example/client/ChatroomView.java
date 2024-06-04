package org.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
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
        dataOutputStream.writeUTF("send message-" + message.getText());
        showMessages();
    }
    public void showMessages() throws IOException {
        while (socket.isConnected()) {
            BorderPane borderPane = new BorderPane();
            Label label = new Label();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String[] msg = dataInputStream.readUTF().split("-");
            if (Objects.equals(msg[0], "other") && Objects.equals(msg[1], "message")) {
                label.setText(msg[2]);
                borderPane.setLeft(label);
                messages.add(borderPane, 0, counter++);
            }
            else if (Objects.equals(msg[0], "your") && Objects.equals(msg[1], "message")) {
                label.setText(msg[2]);
                borderPane.setRight(label);
                messages.add(borderPane, 0, counter++);
            }
            message.setText(" ");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread("controlMessagesThread") {
            @Override
            public void run() {
                try {
                    showMessages();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}
