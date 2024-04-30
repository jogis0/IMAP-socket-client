package org.example;

import java.util.ArrayList;

public class Email {
    public String sender;
    public String subject;
    public String content;
    public ArrayList<FileInfo> files;

    public Email(String sender, String subject, String content) {
        this.sender = sender;
        this.subject = subject;
        this.content = content;
    }
}
