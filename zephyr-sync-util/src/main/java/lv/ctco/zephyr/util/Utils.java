package lv.ctco.zephyr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {

    public static void log(String msg) {
        System.out.println("##### " + msg);
    }

    public static void log(String msg, Exception e) {
        System.out.println("##### " + msg);
        e.printStackTrace(System.out);
    }

    public static String readInputStream(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            response.append(inputLine);
        }
        br.close();
        return response.toString();
    }

    public static String normalizeKey(String input) {
        if (input != null && input.length() > 0) {
            String[] tokens = input.split(" ");
            input = "";
            for (String token : tokens) {
                input += token.substring(0, 1).toUpperCase();
            }
        }
        return input;
    }
}
