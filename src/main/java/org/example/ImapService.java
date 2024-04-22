package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ImapService {
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static final Tag tag = new Tag("a", 1);
    public static boolean isLoggedIn = false;

    public ImapService(BufferedReader bufferedReader, PrintWriter printWriter) {
        reader = bufferedReader;
        writer = printWriter;
    }

    public static boolean isConnected() {
        var response = readResponse("*");
        return response.size() == 1 && response.get(0).startsWith("* OK");
    }

    public static boolean logIn(String email, String password) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "LOGIN " + email + " " + password);
        var response = readResponse(currentTag);

        isLoggedIn = response.size() == 1 && ResponseInterpreter.checkLogInResponse(response.get(0), currentTag);
        return isLoggedIn;
    }

    public static ArrayList<String> readResponse(String tag) {
        ArrayList<String> response = new ArrayList<>();
        String line;
        do {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.add(line);
        } while (!line.startsWith(tag) && line != null);
        return response;
    }

    public static void sendCommand(String tag, String command) {
        writer.print(tag + " " + command + "\r\n");
        writer.flush();
    }
}
