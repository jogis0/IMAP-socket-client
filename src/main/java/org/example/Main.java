package org.example;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    static final String HOST = "outlook.office365.com";
    static final int PORT = 993;

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
                System.out.println("Incorrect credentials, please try again.");
            } else
                break;
        }
        boolean appRunning = true;
        while (appRunning) {
            int selection = Menu.authenticatedStateMenu();

            switch (selection) {
                case 1:
                    //SELECT MAILBOX
                    var mailboxes = ImapService.getMailboxes();
                    int mailboxSelection = Menu.selectMailboxMenu(mailboxes);
                    if (mailboxSelection == mailboxes.size() + 1) {
                        break;
                    }

                    if (mailboxSelection > mailboxes.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    ImapService.selectMailbox(mailboxes.get(mailboxSelection - 1));

                    var emails = ImapService.getMailboxEmails();
                    int emailSelection = Menu.selectEmailMenu(emails);

                    if (emailSelection == emails.size() + 1) {
                        break;
                    }
                    if (emailSelection > emails.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    int backSelection = Menu.selectedEmailMenu(emails.get(emailSelection - 1));
                    if (backSelection == 1) {
                    }
                    else {
                        System.out.println("No such choice, choose again.");
                    }

                    break;
                case 2:
                    //CREATE MAILBOX
                    String mailboxName = Menu.getNewMailboxName();
                    if (ImapService.createMailbox(mailboxName))
                        System.out.println("Mailbox created successfully!");
                    else
                        System.out.println("Could not create mailbox.");
                    break;
                case 3:
                    //RENAME MAILBOX
                    var allRenameMailboxes = ImapService.getMailboxes();
                    int mailboxRenameSelection = Menu.selectMailboxMenu(allRenameMailboxes);
                    if (mailboxRenameSelection == allRenameMailboxes.size() + 1) {
                        break;
                    }

                    if (mailboxRenameSelection > allRenameMailboxes.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    var newMailboxName = Menu.getNewMailboxName();

                    if (ImapService.renameMailbox(allRenameMailboxes.get(mailboxRenameSelection - 1), newMailboxName))
                        System.out.println("Mailbox renamed successfully!");
                    else
                        System.out.println("Could not rename mailbox.");
                    break;
                case 4:
                    //DELETE MAILBOX
                    var allDeleteMailboxes = ImapService.getMailboxes();
                    int mailboxDeleteSelection = Menu.selectMailboxMenu(allDeleteMailboxes);
                    if (mailboxDeleteSelection == allDeleteMailboxes.size() + 1) {
                        break;
                    }

                    if (mailboxDeleteSelection > allDeleteMailboxes.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    if (ImapService.deleteMailbox(allDeleteMailboxes.get(mailboxDeleteSelection - 1)))
                        System.out.println("Mailbox deleted successfully!");
                    else
                        System.out.println("Could not delete mailbox.");
                    break;
                case 5:
                    //SUBSCRIBE
                    String subscribeMailboxName = Menu.getSubscribeMailboxName();
                    if (ImapService.subscribeToMailbox(subscribeMailboxName))
                        System.out.println("Mailbox subscribed successfully!");
                    else
                        System.out.println("Could not subscribe to mailbox.");
                    break;
                case 6:
                    //UNSUBSCRIBE
                    var subscribedMailboxes = ImapService.getSubscribedMailboxes();
                    int unsubscribeSelection = Menu.selectMailboxMenu(subscribedMailboxes);

                    if (unsubscribeSelection == subscribedMailboxes.size() + 1) {
                        break;
                    }

                    if (unsubscribeSelection > subscribedMailboxes.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    if (ImapService.unsubscribeFromMailbox(subscribedMailboxes.get(unsubscribeSelection - 1)))
                        System.out.println("Mailbox unsubscribed successfully!");
                    else
                        System.out.println("Could not unsubscribe from mailbox.");
                    break;
                case 7:
                    var subMailboxes = ImapService.getSubscribedMailboxes();
                    int subMailboxSelection = Menu.selectMailboxMenu(subMailboxes);
                    if (subMailboxSelection == subMailboxes.size() + 1) {
                        break;
                    }

                    if (subMailboxSelection > subMailboxes.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    ImapService.selectMailbox(subMailboxes.get(subMailboxSelection - 1));

                    var subEmails = ImapService.getMailboxEmails();
                    int subEmailSelection = Menu.selectEmailMenu(subEmails);

                    if (subEmailSelection == subEmails.size() + 1) {
                        break;
                    }
                    if (subEmailSelection > subEmails.size() + 1) {
                        System.out.println("No such choice, choose again.");
                        break;
                    }

                    int subBackSelection = Menu.selectedEmailMenu(subEmails.get(subEmailSelection - 1));
                    if (subBackSelection == 1) {
                    }
                    else {
                        System.out.println("No such choice, choose again.");
                    }
                    break;
                case 8:
                    //LOG OUT
                    System.out.println("Goodbye!");
                    appRunning = false;
                    break;
                default:
                    System.out.println("No such choice, choose again.");
                    break;
            }
        }
        socket.close();
    }
}