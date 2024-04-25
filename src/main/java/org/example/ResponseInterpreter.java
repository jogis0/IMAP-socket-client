package org.example;

import java.util.ArrayList;

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
}
