package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ResponseInterpreter {
    public static boolean checkSimpleResponse(String tag, String response) {
        return response.startsWith(tag + " OK");
    }


    public static ArrayList<String> getMailboxNames(String tag, ArrayList<String> response) {
        ArrayList<String> mailboxNames = new ArrayList<>();
        for (String line : response) {
            if (line.startsWith(tag + " OK")) {
                break;
            }

            mailboxNames.add(line.substring(line.indexOf("\"/\"") + 4)); //4 because "/" is 3 and space is another 1
        }

//        * LIST (\HasNoChildren) "/" Archyvas
//        * LIST (\Marked \HasNoChildren) "/" Inbox
//        * LIST (\HasNoChildren \Sent) "/" Sent
//        * LIST (\HasNoChildren \Drafts) "/" Drafts
//        * LIST (\HasNoChildren \Junk) "/" Junk
//        * LIST (\HasNoChildren \Trash) "/" Deleted
//        * LIST (\HasNoChildren) "/" Notes
//        * LIST (\HasNoChildren) "/" Outbox
//        * LIST (\HasNoChildren) "/" TestFolder
//        a2 OK LIST completed.
        return mailboxNames;
    }

    public static boolean checkSelectResponse(String tag, ArrayList<String> response) {
        return response.get(response.size() - 1).startsWith(tag + " OK");

//        * 1 EXISTS
//        * 1 RECENT
//        * FLAGS (\Seen \Answered \Flagged \Deleted \Draft $MDNSent)
//        * OK [PERMANENTFLAGS (\Seen \Answered \Flagged \Deleted \Draft $MDNSent)] Permanent flags
//        * OK [UIDVALIDITY 215] UIDVALIDITY value
//        * OK [UIDNEXT 2] The next unique identifier value
//        a3 OK [READ-WRITE] SELECT completed.
    }

    public static ArrayList<String> parseSearchResponse(String tag, ArrayList<String> response) {
        if (response.get(0).startsWith("* SEARCH ") && response.get(1).startsWith(tag + " OK")) {
            var list = new ArrayList<>(Arrays.asList(response.get(0).substring(9).split("\\s+")));
            return list;
        }

        return null;
//        * SEARCH 1 2
//        a4 OK SEARCH completed.
    }

    public static ArrayList<Email> getEmails(ArrayList<ArrayList<String>> responseList) {
        ArrayList<Email> emails = new ArrayList<>();
        for (ArrayList<String> response : responseList) {
            emails.add(parseEmail(response));
        }

        return emails;
//        * 2 FETCH (BODY[HEADER.FIELDS (FROM SUBJECT)] {85}
//        From: =?UTF-8?B?Sm9rxatiYXMgVmlsaXXFoWlz?= <viliusisj@gmail.com>
//        Subject: Test 2
//
//        BODY[TEXT] {317}
//        --000000000000485dbd0616d8a22d
//        Content-Type: text/plain; charset="UTF-8"
//
//        Dar vienas testas
//
//                --000000000000485dbd0616d8a22d
//        Content-Type: text/html; charset="UTF-8"
//
//                <meta http-equiv="Content-Type" content="text/html; charset=utf-8"><div dir="ltr">Dar vienas testas</div>
//
//                --000000000000485dbd0616d8a22d--
//        )
//        a5 OK FETCH completed.

        //Failas atrodo taip
//        Content-Type: application/pdf; name="T0814u.pdf"
//        Content-Disposition: attachment; filename="T0814u.pdf"
//        Content-Transfer-Encoding: base64
//        Content-ID: <f_lvkni1y00>
//        X-Attachment-Id: f_lvkni1y00
    }

    //Very bad method here, just be happy that it works
    private static Email parseEmail(ArrayList<String> response) {
        if (response.get(response.size() - 1).contains("OK")) {
            Email email = parseEmailText(response);

            //Getting attached files and file data from the response
            int textSectionEndIndex = response.indexOf(findTextSectionEnd(response));
            var responseWithoutText = response.subList(textSectionEndIndex + 1, response.size() - 1); //+ 1 because we want to skip the marker line

            var files = parseFiles(responseWithoutText);
            if (files.size() != 0)
                email.files = files;
            return email;
        }

        return null;
    }

    private static Email parseEmailText(ArrayList<String> response) {
        String sender = null;
        String subject = null;
        StringBuilder text = new StringBuilder();

        //Getting email text from the response
        boolean inTextSection = false;

        for (String line : response) {
            if (line.startsWith("From: ") && sender == null) {
                sender = line.substring(line.indexOf("<") + 1, line.indexOf(">"));
            }
            if (line.startsWith("Subject: ") && subject == null) {
                subject = line.substring(9);
            }

            if (line.startsWith("Content-Type: text/plain;") && inTextSection == false) {
                inTextSection = true;
                continue;
            }
            if (line.startsWith("--") && inTextSection == true) {
                break;
            }

            if (inTextSection) {
                text.append(line + System.getProperty("line.separator"));
            }
        }

        if (sender == null || subject == null) {
            return null;
        }
        return new Email(sender, subject, text.toString());
    }

    private static ArrayList<FileInfo> parseFiles(List<String> response) {
        ArrayList<FileInfo> files = new ArrayList<>();

        String fileName = null;
        StringBuilder data = new StringBuilder();

        boolean dataReadingMode = false;
        for (String line : response) {
            if (line.startsWith("Content-Disposition:")) {
                fileName = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                continue;
            }

            if (line.startsWith("X-Attachment-Id:") && dataReadingMode == false) {
                //Start reading data
                dataReadingMode = true;
                continue;
            }

            if (line.startsWith("--") && dataReadingMode == true) {
                files.add(new FileInfo(fileName, Base64.getDecoder().decode(data.toString())));

                fileName = null;
                data = new StringBuilder();
                dataReadingMode = false;
                continue;
            }

            if (dataReadingMode) {
                data.append(line);
            }
        }
        return files;
    }

    //Helper method to find the end of the body text section
    //It's needed to find the start of the section about files and file data
    private static String findTextSectionEnd(ArrayList<String> data) {
        for (String item : data) {
            if (item.startsWith("--") && item.endsWith("--")) {
                return item;
            }
        }
        return null;
    }
}
