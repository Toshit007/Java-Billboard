module ca.georgian.assignment2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.georgian.assignment2 to javafx.fxml;
    exports ca.georgian.assignment2;
}