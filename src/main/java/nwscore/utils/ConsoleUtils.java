package nwscore.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Strings.repeat;

/**
 * Helper methods for using console.
 * @author  Bora Tashan
 * @version 1.0
 * @since   2017-1-23
 */
public class ConsoleUtils {

    private static List<String> recordList;

    public static void startRecording() {
        recordList = new ArrayList<>();
    }

    public static void commitRecording(File recordingFile) throws IOException {
        if (Objects.isNull(recordList)) return;
        BufferedWriter writer = new BufferedWriter(new FileWriter(recordingFile));
        for (String s : recordList) {
            writer.write(s);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static void println(String message) {
        System.out.print(String.format("%s\n", message));
        if (Objects.nonNull(recordList)) recordList.add(message);
    }

    public static void printlnLine() {
        println(repeat(" ", 24) + "**"+repeat(" ", 24));

    }

    public static void printLnException(Exception e) {
                println(String.format("[EXCEPTION DURING EXECUTION] %s", e.getMessage().equals("null") ? e.toString() : e.getMessage()));
    }
}
