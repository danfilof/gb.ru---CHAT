module ru.gb.gbruchat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
//    requires org.testng;
    requires org.junit.jupiter.api;


    opens server to javafx.fxml;
    exports server;
}