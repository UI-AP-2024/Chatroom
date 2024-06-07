package org.example.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.io.DataOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class PrivateChat implements Initializable {
    public static String personID ;

    @FXML
    private TextField messageField;

    @FXML
    private GridPane messageGridPane;

    @FXML
    private FontAwesomeIcon sendIcon;

    @FXML
    void sendIconClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        DataOutputStream dataOutputStream = new DataOutputStream(ChatroomPage.socket.getOutputStream());
//        dataOutputStream.writeUTF("pv-"+personID);
    }
}
