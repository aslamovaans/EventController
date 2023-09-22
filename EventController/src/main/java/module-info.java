module com.example.volonterhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires h2;


    opens com.example.eventController to javafx.fxml;
    exports com.example.eventController;
}