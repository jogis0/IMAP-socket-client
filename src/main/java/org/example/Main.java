package org.example;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.*;

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
                case 1 -> {
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
                    var selectedEmail = emails.get(emailSelection - 1);
                    int backSelection = Menu.selectedEmailMenu(selectedEmail);
                    emailActionSelection(selectedEmail, backSelection);
                }
                case 2 -> {
                    //CREATE MAILBOX
                    String mailboxName = Menu.getNewMailboxName();
                    if (ImapService.createMailbox(mailboxName))
                        System.out.println("Mailbox created successfully!");
                    else
                        System.out.println("Could not create mailbox.");
                }
                case 3 -> {
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
                }
                case 4 -> {
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
                }
                case 5 -> {
                    //SUBSCRIBE
                    String subscribeMailboxName = Menu.getSubscribeMailboxName();
                    if (ImapService.subscribeToMailbox(subscribeMailboxName))
                        System.out.println("Mailbox subscribed successfully!");
                    else
                        System.out.println("Could not subscribe to mailbox.");
                }
                case 6 -> {
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
                }
                case 7 -> {
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
                    var selectedSubEmail = subEmails.get(subEmailSelection - 1);
                    int subBackSelection = Menu.selectedEmailMenu(selectedSubEmail);
                    emailActionSelection(selectedSubEmail, subBackSelection);
                }
                case 8 -> {
                    //LOG OUT
                    System.out.println("Goodbye!");
                    appRunning = false;
                }
                default -> System.out.println("No such choice, choose again.");
            }
        }

        socket.close();
    }

    private static void emailActionSelection(Email selectedSubEmail, int subBackSelection) throws IOException {
        if (selectedSubEmail.files != null) {
            if (selectedSubEmail.files.size() == 1  && subBackSelection == 1) {
                createFile(selectedSubEmail.files.get(0));
            } else if (selectedSubEmail.files.size() > 1 && subBackSelection == 1) {
                zipFiles(selectedSubEmail.files, selectedSubEmail.subject + ".zip");
            }
            else {
                System.out.println("No such choice, choose again.");
            }
        } else if (subBackSelection != 1)
            System.out.println("No such choice, choose again.");
    }

    private static void zipFiles(ArrayList<FileInfo> files, String name) throws IOException {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "data", name);

        FileOutputStream fos = new FileOutputStream(path.toFile());
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (FileInfo fileInfo : files) {
            ZipEntry zipEntry = new ZipEntry(fileInfo.name);
            zipOut.putNextEntry(zipEntry);
            zipOut.write(fileInfo.data, 0, fileInfo.data.length);
        }

        zipOut.close();
        fos.close();
        System.out.println("Attachments downloaded successfully!");
    }

    private static void createFile(FileInfo fileInfo) {
        Path path = Paths.get(System.getProperty("user.dir"), "src", "data");
        File file = new File(path.toFile(), fileInfo.name);

        try {
            Files.createDirectories(path);

            if (!file.createNewFile())
                System.out.println("File already exists.");
            else {
                try (FileOutputStream outputStream = new FileOutputStream(file)) {
                    outputStream.write(fileInfo.data);
                    System.out.println("File downloaded successfully!");
                } catch (IOException e) {
                    System.out.println("FileOutputStream: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("File: " + e.getMessage());
        }
    }
}