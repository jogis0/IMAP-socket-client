package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
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
     * 5. Subscribe to mailbox
     * 6. Unsubscribe from mailbox?
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

    public static String getEmail() {
        System.out.println("ENTER EMAIL:");
        return new Scanner(System.in).nextLine();
    }

    public static String getPassword() {
        System.out.println("ENTER PASSWORD:");
        return new Scanner(System.in).nextLine();
    }

    public static int authenticatedStateMenu() {
        System.out.println("SELECT ACTION:");
        System.out.println("--------------------");
        System.out.println("1. Select mailbox");
        System.out.println("2. Create mailbox");
        System.out.println("3. Rename mailbox");
        System.out.println("4. Delete mailbox");
        System.out.println("5. LOGOUT");

        return new Scanner(System.in).nextInt();
    }

    public static int selectMailboxMenu(ArrayList<String> mailboxes) {
        System.out.println("SELECT MAILBOX:");
        System.out.println("--------------------");
        int n = 1;
        for (String mailbox : mailboxes) {
            System.out.println(n++ + ". " + mailbox);
        }
        System.out.println("--------------------");
        System.out.println(n + ". BACK");

        return new Scanner(System.in).nextInt();
    }

    public static int selectEmailMenu(ArrayList<Email> emails) {
        System.out.println("SELECT EMAIL:");
        System.out.println("--------------------");
        int n = 1;
        if (emails != null) {
            for (Email email : emails) {
                System.out.println(n++ + ". SUBJECT: " + email.subject);
            }
        }

        System.out.println("--------------------");
        System.out.println(n + ". BACK");

        return new Scanner(System.in).nextInt();
    }

    public static int selectedEmailMenu(Email email) {
        System.out.println("EMAIL:");
        System.out.println("--------------------");
        System.out.println("FROM: " + email.sender);
        System.out.println("SUBJECT: " + email.subject);
        System.out.println(email.content);
        System.out.println("--------------------");
        System.out.println("1. BACK");

        return new Scanner(System.in).nextInt();
    }
}
