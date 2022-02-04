package server;

import java.util.Arrays;

public enum Command {

    AUTH("/auth"),
   AUTHOK("/authok"),
   PRIVATE_MESSAGE("/w"),
    END("/end"),
    CLIENTS("/clients");

    private String command;

    public String getCommand() {
        return command;
    }

    Command(String command) {
        this.command = command;
    }

    public static Command getCommandByText(String text) {
        return Arrays.stream(values()).
                filter(cmd -> text.startsWith(cmd.getCommand()))
                        .findAny().orElseThrow(() -> new RuntimeException("Unknown command"));
    }

    public static String getCommandPrefix() {
        return "/";
    }
}
