package org.example.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChatroomPage implements Initializable {
    static int counter = 2;
    public static Socket socket;
    private long start;
    @FXML
    private ScrollPane scrollPane;

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
        firstField.setPromptText("Name");
        secondField.setVisible(false);
        firstField.setVisible(true);
    }

    @FXML
    void timeToTimeSearchClicked(ActionEvent event) throws IOException {
        firstField.setPromptText("From");
        secondField.setPromptText("Until");
        secondField.setVisible(true);
        firstField.setVisible(true);
    }

    @FXML
    void pingButtonClicked(MouseEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        start = System.currentTimeMillis();
        dataOutputStream.writeUTF("ping");
    }

    @FXML
    void privateChatButtonClicked(MouseEvent event) throws IOException {
        OnlinePeople.socket = socket;
        HelloApplication.myStage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("online-people.fxml")).load()));
    }

    @FXML
    void searchIconClicked(MouseEvent event) throws IOException, InterruptedException, ClassNotFoundException {
        if (!secondField.isVisible()) {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("search-person-" + firstField.getText());
        }else{
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("search-time-"+firstField.getText()+"-"+secondField.getText());
        }
        firstField.setVisible(false);
        secondField.setVisible(false);
        firstField.setText(null);
        secondField.setText(null);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        firstField.setVisible(false);
        secondField.setVisible(false);
        new Thread(() -> {
            try {
                splitCommand();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        },"messageController").start();
    }

    public void splitCommand() throws IOException {
        while (true) {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            String[] strings = dataInputStream.readUTF().split("-");
            switch (strings[0]) {
                case "message" -> {
                    new Thread(() -> {
                        showMessages(strings);
                    }, "message").start();
                }
                case "person" -> {
                    new Thread(() -> {
                        searchPerson(strings);
                    }, "person").start();
                }
                case "time" -> {
                    new Thread(() -> {
                        searchTime(strings);
                    }, "time").start();
                }
                case "ping" -> {
                    new Thread(() -> {
                        showPing();
                    }, "ping").start();
                }
            }
        }
    }

    public void showPing(){
        System.out.println((System.currentTimeMillis()-start));
    }
    public void searchTime(String[] strings){
        for (int i = 1 ; i < strings.length-1 ; i++){
            System.out.print(strings[i]);
            System.out.println(strings[++i]);
        }
    }
    public void searchPerson(String[] strings){
        for (int i = 1 ; i < strings.length ; i++){
            System.out.println(strings[i]);
        }
    }
    public void showMessages(String[] string){
        Platform.runLater(() -> {
            BorderPane borderPane = new BorderPane();
            TextArea message = new TextArea();
            if (Objects.equals(string[0], "message") && Objects.equals(string[1], "other")) {
                message.setText(string[2]+"  ");
                designMessage(borderPane,message);
                message.setBackground(Background.fill(Paint.valueOf("black")));
                messageGridPane.add(borderPane, 0, counter++);
            } else if (Objects.equals(string[0], "message") && Objects.equals(string[1], "your")) {
                message.setText(string[2]+"  ");
                designMessage(borderPane,message);
                message.setBackground(Background.fill(Paint.valueOf("purple")));
                messageGridPane.add(borderPane, 2, counter++);
            }
        });
    }
    public void designMessage(BorderPane borderPane , TextArea message){
        message.setFont(Font.font(18));
        message.setPadding(new Insets(10 ,0,10,10));
        message.setWrapText(true);
        message.setMaxWidth(160);
        borderPane.setCenter(message);
        message.setDisable(true);
    }
}
