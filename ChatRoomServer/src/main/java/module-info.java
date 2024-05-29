module com.example.chatroomserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.chatroomserver to javafx.fxml;
    exports com.example.chatroomserver;
}