package server;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class HistoryLogger {
    private static final int MAX_LINE_COUNT = 100;

    private final Writer writer;
    private final File file;

    public HistoryLogger(String nick) {
        this.file = new File("history_" + nick + ".txt");
        try {
            if (!file.exists()) {
                final boolean isCreate = file.createNewFile();
                if (!isCreate) {
                    try {
                        throw new HistoryFileException("Cannot create new file");
                    } catch (HistoryFileException e) {
                        e.printStackTrace();
                    }
                }
            }
            this.writer = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String loadHistory() {
        final List<String> messages = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                messages.add(line);
                if (messages.size() > MAX_LINE_COUNT) {
                    messages.remove(0);
                }
            }
        } catch (IOException e) {
            try {
                throw new HistoryFileException(e);
            } catch (HistoryFileException ex) {
                ex.printStackTrace();
            }
        }
        return String.join("\n", messages);
    }

    public void log(String message) {
        try {
            if (!message.isEmpty()) {
                writer.write(message + "\n");
            }
        } catch (IOException e) {
            try {
                throw new HistoryFileException(e);
            } catch (HistoryFileException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            try {
                throw new HistoryFileException(e);
            } catch (HistoryFileException ex) {
                ex.printStackTrace();
            }
        }
    }
}
