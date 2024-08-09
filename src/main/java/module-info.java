module ca.georgian.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;

    opens ca.georgian.assignment2 to javafx.fxml;
    exports ca.georgian.assignment2;
}
