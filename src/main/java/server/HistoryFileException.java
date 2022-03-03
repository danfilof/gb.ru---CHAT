package server;

public class HistoryFileException extends Throwable {
    public HistoryFileException(Exception e) {
        super(e);
    }

    public HistoryFileException(String msg) {
        super(msg);
    }
}
