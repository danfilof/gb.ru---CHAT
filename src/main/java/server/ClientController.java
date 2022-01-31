package server;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientController {

    @FXML
    private TextArea messageArea;

    @FXML
    private TextField messageField;

    final ChatClient client;

    public ClientController() {

        client = new ChatClient(this);
    }

    public void onClickSendButton (ActionEvent actionEvent) {
        final String message = messageField.getText();
        if (message != null && !message.isEmpty()) {
            messageArea.appendText(message + "\n");
            client.sendMessage(message);
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void addMessage(String message) {
        messageArea.appendText(message + "\n");
    }
}