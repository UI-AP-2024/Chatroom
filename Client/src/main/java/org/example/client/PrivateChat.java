package org.example.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrivateChat implements Initializable {
    Thread runner;
    public static int counter = 2;

    public static String name;

    @FXML
    private FontAwesomeIcon backIcon;

    @FXML
    private TextField messageField;

    @FXML
    private GridPane messageGridPane;

    @FXML
    private FontAwesomeIcon sendIcon;

    @FXML
    private Button startMessagingButton;

    @FXML
    void backIconClicked(MouseEvent event) throws IOException, InterruptedException {
        DataOutputStream dataOutputStream = new DataOutputStream(ChatroomPage.socket.getOutputStream());
        dataOutputStream.writeUTF("waitThread");
        Thread.sleep(500);
        HelloApplication.myStage.setScene(new Scene(new FXMLLoader(HelloApplication.class.getResource("chatroom-page.fxml")).load()));
    }


    @FXML
    void startMessagingButtonClicked(MouseEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(ChatroomPage.socket.getOutputStream());
        dataOutputStream.writeUTF("pv-person-" + name);
        startMessagingButton.setDisable(true);
        startMessagingButton.setVisible(false);
    }

    @FXML
    void sendIconClicked(MouseEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(ChatroomPage.socket.getOutputStream());
        dataOutputStream.writeUTF("pv-message-" + messageField.getText() + "-" + name);
        messageField.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        runner = new Thread(() -> {
            try {
                listener();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        });
        runner.start();
    }

    public void listener() throws IOException {
        while (true) {
            DataInputStream dataInputStream = new DataInputStream(ChatroomPage.socket.getInputStream());
            String[] strings = dataInputStream.readUTF().split("-");
            switch (strings[0]){
                case "null" -> {
                    System.out.println("nothing");
                }
                case "pv" ->{
                    new Thread(() -> {
                        showMessages(strings);
                    }).start();
                }
                case "waitThread" -> {
                    try {
                        runner.wait();
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }

    synchronized public void showMessages(String[] string) {
        Platform.runLater(() -> {
            BorderPane borderPane = new BorderPane();
            TextArea message = new TextArea();
            if (Objects.equals(string[0], "pv") && Objects.equals(string[1], "other")) {
                message.setText(string[2] + "  ");
                ChatroomPage.designMessage(borderPane, message);
                message.setBackground(Background.fill(Paint.valueOf("black")));
                messageGridPane.add(borderPane, 0, counter++);
            } else if (Objects.equals(string[0], "pv") && Objects.equals(string[1], "your")) {
                message.setText(string[2] + "  ");
                ChatroomPage.designMessage(borderPane, message);
                message.setBackground(Background.fill(Paint.valueOf("purple")));
                messageGridPane.add(borderPane, 1, counter++);
            }
        });
    }
}
