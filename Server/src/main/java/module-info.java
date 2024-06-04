module org.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.server to javafx.fxml;
    exports org.example.server;
}