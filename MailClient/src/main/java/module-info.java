module com.example.mailclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.mailclient to javafx.fxml;
    opens com.example.mailclient.Client to javafx.fxml;
    opens com.example.mailclient.Server to javafx.fxml;
    opens com.example.mailclient.Model to javafx.fxml;

    exports com.example.mailclient.Client;
    exports com.example.mailclient.Server;
    exports com.example.mailclient.Model;
}