module com.example.dsiigame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dsiigame to javafx.fxml;
    exports com.example.dsiigame;
}