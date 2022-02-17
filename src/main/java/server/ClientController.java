package server;

import client.ChatClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientController {

    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox messageBox;
    @FXML
    private ListView<String> clientList;

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
            client.sendMessage(message);
            messageField.clear();
            messageField.requestFocus();
        }
    }

    public void addMessage(String message) {
        messageArea.appendText(message + "\n");
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
           final String message = messageField.getText();
            final String client = clientList.getSelectionModel().getSelectedItem();
            messageField.setText(Command.PRIVATE_MESSAGE.getCommand() + " " + client + " " + message);
            messageField.requestFocus();
            messageField.selectEnd();
        }
    }

    public void setAuth(boolean isAuthSuccess) {
        loginBox.setVisible(!isAuthSuccess);
        messageBox.setVisible(isAuthSuccess);
    }

    public void updateClientsList(String[] clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }

    // Сохранение чата в формате .txt
    public void saveChatHistory() {
        // Проверка на существование файла, куда записывается чат
        File chatHistory = new File ("chatHistory.txt");
        if (!chatHistory.exists()) {
            try {
                chatHistory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Решил попробовать Buffer, так как на лекции он был заметно производительнее
        try (final BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(chatHistory))) {
            // Сообщения берутся прямо из TextArea
            bos.write(Integer.parseInt(messageArea.getText()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Выгрузка чата из текстового файла
    public void loadChatHistory() {
        // Записываю все в List, так как хочу, чтобы при подключении все сообщения из прошлого сразу печатались
        File chatHistory = new File("chatHistory.txt");
        List<String> historyChat = new ArrayList<>();
        int msgToRead = 100;

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(chatHistory))) {
            String singleMessage  = String.valueOf(bis.read());
            // Записываю весь текст в List<String> и потов печатаю его в TextArea
            while (singleMessage != null && historyChat.size() <= msgToRead) {
                historyChat.add(singleMessage);
            }
            messageArea.appendText(String.valueOf(historyChat));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}