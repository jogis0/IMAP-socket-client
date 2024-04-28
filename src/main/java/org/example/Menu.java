package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Menu {
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
        System.out.println("5. Subscribe to mailbox");
        System.out.println("6. Unsubscribe from mailbox");
        System.out.println("7. Select subscribed mailbox");
        System.out.println("8. Log out");

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
        System.out.println(n + ". Back");

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
        System.out.println(n + ". Back");

        return new Scanner(System.in).nextInt();
    }

    public static int selectedEmailMenu(Email email) {
        System.out.println("EMAIL:");
        System.out.println("--------------------");
        System.out.println("FROM: " + email.sender);
        System.out.println("SUBJECT: " + email.subject);
        System.out.println(email.content);
        System.out.println("--------------------");
        System.out.println("1. Back");

        return new Scanner(System.in).nextInt();
    }

    public static String getNewMailboxName() {
        System.out.println("ENTER NEW MAILBOX NAME:");
        return new Scanner(System.in).nextLine();
    }

    public static String getSubscribeMailboxName() {
        System.out.println("ENTER MAILBOX NAME TO SUBSCRIBE:");
        return new Scanner(System.in).nextLine();
    }
}
