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

        isLoggedIn = response.size() == 1 && ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
        return isLoggedIn;
    }

    public static boolean createMailbox(String mailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "CREATE " + mailboxName);
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
    }

    public static boolean renameMailbox(String oldMailboxName, String newMailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "RENAME " + oldMailboxName + " " + newMailboxName);
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
    }

    public static boolean deleteMailbox(String mailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "DELETE " + mailboxName);
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
    }

    public static boolean subscribeToMailbox(String mailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "SUBSCRIBE " + mailboxName);
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
    }

    public static ArrayList<String> getSubscribedMailboxes() {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "LSUB \"\" \"*\"");
        var response = readResponse(currentTag);

        return ResponseInterpreter.getMailboxNames(currentTag, response);
    }

    public static boolean unsubscribeFromMailbox(String mailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "UNSUBSCRIBE " + mailboxName);
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSimpleResponse(currentTag, response.get(0));
    }

    public static ArrayList<String> getMailboxes() {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "LIST \"\" \"*\"");
        var response = readResponse(currentTag);

        return ResponseInterpreter.getMailboxNames(currentTag, response);
    }

    public static boolean selectMailbox(String mailboxName) {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "SELECT \"" + mailboxName + "\"");
        var response = readResponse(currentTag);

        return ResponseInterpreter.checkSelectResponse(currentTag, response);
    }

    public static ArrayList<Email> getMailboxEmails() {
        ArrayList<ArrayList<String>> responseList = new ArrayList<>();
        var emailIds = getEmailIds();
        if (emailIds == null)
            return new ArrayList<Email>();

        for (String emailNum : getEmailIds()) {
            String currentTag = tag.getTag();
            sendCommand(currentTag, "FETCH " + emailNum + " (BODY.PEEK[HEADER.FIELDS (FROM SUBJECT)] BODY.PEEK[TEXT])");
            var response = readResponse(currentTag);
            responseList.add(response);
        }

        return ResponseInterpreter.getEmails(responseList);
    }

    private static ArrayList<String> getEmailIds() {
        String currentTag = tag.getTag();
        sendCommand(currentTag, "SEARCH ALL");
        var response = readResponse(currentTag);

        return ResponseInterpreter.parseSearchResponse(currentTag, response);
    }

    private static ArrayList<String> readResponse(String tag) {
        ArrayList<String> response = new ArrayList<>();
        String line;
        do {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.add(line);
        } while (!line.startsWith(tag + " ") && line != null);
        return response;
    }

    private static void sendCommand(String tag, String command) {
        writer.print(tag + " " + command + "\r\n");
        writer.flush();
    }
}
