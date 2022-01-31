module ru.gb.gbruchat {
    requires javafx.controls;
    requires javafx.fxml;


    opens server to javafx.fxml;
    exports server;
}