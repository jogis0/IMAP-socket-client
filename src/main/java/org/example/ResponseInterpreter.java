package org.example;

public class ResponseInterpreter {
    public static boolean checkLogInResponse(String response, String tag) {
        return response.startsWith(tag + " OK");
    }
}
