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

}
