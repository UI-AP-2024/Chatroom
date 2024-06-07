package org.example.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class OnlinePeople implements Initializable {

    @FXML
    private GridPane peopleGridPane;

    @FXML
    private FontAwesomeIcon refreshIcon;

    @FXML
    void refreshIconClicked(MouseEvent event) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(ChatroomPage.socket.getOutputStream());
        dataOutputStream.writeUTF("online-people");
    }

        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                System.out.println("before listener");
                listener();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }).start();
    }

    public void listener() throws IOException {
        while (true){
            DataInputStream dataInputStream = new DataInputStream(ChatroomPage.socket.getInputStream());
            String[] strings = dataInputStream.readUTF().split("-");
            new Thread(() -> {
                try {
                    showPeople(strings);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }).start();
        }
    }

    public void showPeople(String[] strings) throws IOException {
        Platform.runLater(() -> {
            for (int i = 1; i < strings.length - 1; i++) {
                Label label = new Label(strings[i++]);
                label.setFont(Font.font(25));
                BorderPane borderPane = new BorderPane(label);
                borderPane.setId(strings[i]);
                borderPane.setCenter(label);
                label.setTextAlignment(TextAlignment.CENTER);
                borderPane.setOnMouseClicked(event -> {
                    System.out.println(borderPane.getId());
                });
                peopleGridPane.add(borderPane, 0, i);
            }
        });
    }
}
