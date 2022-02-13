module ru.gb.gbruchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens server to javafx.fxml;
    exports server;
}