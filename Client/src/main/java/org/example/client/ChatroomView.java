package org.example.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;

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
        message.clear();
    }
    public void showMessages() throws IOException {
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        while (socket.isConnected()) {
            try {
                String[] msg = dataInputStream.readUTF().split("-");
                Platform.runLater(() -> {
                    BorderPane borderPane = new BorderPane();
                    Label label = new Label();
                    if (Objects.equals(msg[0], "other") && Objects.equals(msg[1], "message")) {
                        label.setText(msg[2]);
                        borderPane.setLeft(label);
                        messages.add(borderPane, 0, counter++);
                    } else if (Objects.equals(msg[0], "your") && Objects.equals(msg[1], "message")) {
                        label.setText(msg[2]);
                        borderPane.setRight(label);
                        messages.add(borderPane, 0, counter++);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                showMessages();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "controlMessagesThread").start();
    }
}
