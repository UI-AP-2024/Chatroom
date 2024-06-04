package org.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageView implements Initializable {

    @FXML
    private Button commandButton;

    @FXML
    private TextField messageField;

    @FXML
    void commandButtonAction(ActionEvent event) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream.writeUTF(messageField.getText());
        String s = dataInputStream.readUTF();
        System.out.println(s);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
