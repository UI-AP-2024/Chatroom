package org.example.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatroomPage implements Initializable {

    static int counter = 0;
    public static Socket socket;

    @FXML
    private TextField firstField;

    @FXML
    private TextField messageField;

    @FXML
    private GridPane messageGridPane;

    @FXML
    private Button pingButton;

    @FXML
    private Button privateChatButton;

    @FXML
    private MenuButton searchButton;

    @FXML
    private FontAwesomeIcon searchIcon;

    @FXML
    private TextField secondField;

    @FXML
    private FontAwesomeIcon sendIcon;

    @FXML
    private FontAwesomeIcon stickerIcon;

    @FXML
    void messageFieldPressed(KeyEvent event) {

    }

    @FXML
    void personSearchClicked(ActionEvent event) {

    }

    @FXML
    void pingButtonClicked(MouseEvent event) {

    }

    @FXML
    void privateChatButtonClicked(MouseEvent event) {

    }

    @FXML
    void searchIconClicked(MouseEvent event) {

    }

    @FXML
    void sendIconClicked(MouseEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        dataOutputStream.writeUTF("send message-" + messageField.getText());
        messageField.clear();
    }

    @FXML
    void stickerIconClicked(MouseEvent event) {

    }

    @FXML
    void timeToTimeSearchClicked(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                showMessages();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }, "controlMessagesThread").start();
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
                        messageGridPane.add(borderPane, 0, counter++);
                    } else if (Objects.equals(msg[0], "your") && Objects.equals(msg[1], "message")) {
                        label.setText(msg[2]);
                        borderPane.setRight(label);
                        messageGridPane.add(borderPane, 0, counter++);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
