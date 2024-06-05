package org.example.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstPage implements Initializable {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField usernameField;

    @FXML
    void loginButtonClicked(MouseEvent event) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("login-"+usernameField.getText()+passwordField.getText()  );
        ChatroomView.socket = socket;
        HelloApplication.myStage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("chatroom-view.fxml")).load()));
    }

    @FXML
    void signUpButtonClicked(MouseEvent event) throws IOException {
        Socket socket = new Socket("127.0.0.1", 1234);
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("signup-"+usernameField.getText()+passwordField.getText()  );
        ChatroomView.socket = socket;
        HelloApplication.myStage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("chatroom-view.fxml")).load()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        signUpButton.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
    }
}
