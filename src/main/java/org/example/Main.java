package org.example;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static final String HOST = "outlook.office365.com";
    static final int PORT = 993;
    static final String EMAIL = "imap.test.2024@outlook.com";
    static final String PASSWORD = "Testing1!2@3#";

    static BufferedReader reader;
    static PrintWriter writer;


    public static void main(String[] args) throws IOException {
        Socket socket = SSLSocketFactory.getDefault().createSocket(HOST, PORT);

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        //LOGIN LOOP
//        while(!LoggedIn){
//            scanner.nextLine();
//            ...
//        }

//        while (true) {
//            int selection = getUserInput();
//
//        }
        /*
        * LOG IN MENU
        * ENTER USERNAME:
        * ...
        * ENTER PASSWORD:
        * ...
        * ----------------
        * ACTION MENU
        * SELECT ACTION:
        * 1. Delete box
        * 2. Rename box
        * 3. Create box
        * ...
        * n. LOGOUT
        * ----------------
        * BOX SELECTION MENU
        * SELECT BOX:
        * 1. Box 1
        * 2. Box 2
        * 3. Box 3
        * ...
        * n. BACK
        * ----------------
        * BOX MENU
        * SELECT EMAIL:
        * 1. Email 1
        * 2. Email 2
        * 3. Email 3
        * ...
        * n. BACK
        * ----------------
        * EMAIL SCREEN
        * EMAIL HEADER
        * EMAIL CONTENTS
        * 1. BACK
         */

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

    public static void readResponse(String tag) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.startsWith(tag)) {
                break;
            }
        }
    }

    public static void sendCommand(String tag, String command) {
        writer.print(tag + " " + command + "\r\n");
        writer.flush();
    }

    public static int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        int selection;
        try {
            selection = scanner.nextInt();
        } catch (Exception e) {
            selection = -1;
        }

        return selection;
    }
}