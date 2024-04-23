package org.example;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    static final String HOST = "outlook.office365.com";
    static final int PORT = 993;
//    static final String EMAIL = "imap.test.2024@outlook.com";
//    static final String PASSWORD = "Testing1!2@3#";


    public static void main(String[] args) throws IOException {
        Socket socket = SSLSocketFactory.getDefault().createSocket(HOST, PORT);
        ImapService imapService = new ImapService(new BufferedReader(new InputStreamReader(socket.getInputStream())),
                new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
        );
        if (!ImapService.isConnected())
            return;

        //Log in the user
        while (true) {
            String email = Menu.getEmail();
            String password = Menu.getPassword();

            if (!ImapService.logIn(email, password)) {
                System.out.println("INCORRECT CREDENTIALS, PLEASE TRY AGAIN");
            } else
                break;
        }

        /*
        * --LOG IN MENU--
        * ENTER USERNAME:
        * ...
        * ENTER PASSWORD:
        * ...
        * ----------------
        * --AUTHENTICATED STATE MENU--
        * SELECT ACTION:
        * 1. Select mailbox
        * 2. Create mailbox
        * 3. Rename mailbox
        * 4. Delete mailbox
        * ...
        * n. LOGOUT
        * ----------------
        * --SELECT MAILBOX MENU--
        * SELECT MAILBOX:
        * 1. Mailbox 1
        * 2. Mailbox 2
        * 3. Mailbox 3
        * ...
        * n. BACK
        * ----------------
        * --MAILBOX MENU--
        * SELECT EMAIL:
        * 1. Email 1
        * 2. Email 2
        * 3. Email 3
        * ...
        * n. BACK
        * ----------------
        * --EMAIL SCREEN--
        * EMAIL HEADER
        * EMAIL CONTENTS
        * 1. BACK
         */

//        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//        sendCommand(writer, "a1", "LOGIN " + EMAIL + " " + PASSWORD);
//        readResponse(reader, "a1");
//
//        sendCommand(writer, "a2", "LIST \"\" \"*\"");
//        readResponse(reader, "a2");
//
//        sendCommand(writer, "a3", "SELECT \"TestFolder\"");
//        readResponse(reader, "a3");

//        String tag = "a1";
//        sendCommand(tag, "LOGIN " + EMAIL + " " + PASSWORD);
//
//        readResponse(tag);
//
//        tag = "a2";
//        sendCommand(tag, "SELECT Inbox");
//
//        readResponse(tag);
//
//        tag = "a3";
//        sendCommand(tag, "SEARCH ALL");
//
//        readResponse(tag);
//
//        tag = "a4";
//        sendCommand(tag, "FETCH 2 (BODY[HEADER])");
//
//        readResponse(tag);

        socket.close();
    }

//    private static void readResponse(BufferedReader reader, String tag) {
//        String line;
//        do {
//            try {
//                line = reader.readLine();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(line);
//        } while (!line.startsWith(tag) && line != null);
//    }
//
//    private static void sendCommand(PrintWriter writer, String tag, String command) {
//        writer.print(tag + " " + command + "\r\n");
//        writer.flush();
//    }
}