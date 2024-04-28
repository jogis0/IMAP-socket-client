package org.example;

import java.util.ArrayList;
import java.util.Arrays;

public class ResponseInterpreter {
    public static boolean checkLogInResponse(String tag, String response) {
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
    }

    private static Email parseEmail(ArrayList<String> response) {
//        for (String line : response) {
//            System.out.println(line);
//        }
        if (response.get(response.size() - 1).contains("OK")) {
            String sender = response.get(1).substring(response.get(1).indexOf("<") + 1, response.get(1).indexOf(">"));
            String subject = response.get(2).substring(9);
            StringBuilder text = new StringBuilder();

            boolean inTextField = false;
            for (String line : response.subList(5, response.size() - 1)) {
                if (line.startsWith("--") && inTextField == false) {
                    inTextField = true;
                    continue;
                }
                if (line.startsWith("--") && inTextField == true) {
                    break;
                }
                if (line.startsWith("Content-"))
                    continue;
                text.append(line + System.getProperty("line.separator"));
            }

            return new Email(sender, subject, text.toString());
        }

        return null;
    }
}
