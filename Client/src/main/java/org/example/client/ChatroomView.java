package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
public class ChatroomView implements Initializable {

    @FXML
    private TextField message;

    @FXML
    private GridPane messages;

    @FXML
    private Button sendButton;

    @FXML
    void messageAction(ActionEvent event) {

    }

    @FXML
    void sendButtonAction(ActionEvent event) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

        dataOutputStream.writeUTF("send message" + message.getText());
        String[] input = dataInputStream.readUTF().split("-");
        Label label = new Label();
        switch (input[0]) {
            case "show message" -> {
                label.setText(input[1]);
            }
        }
        messages.getChildren().add(0, label);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
