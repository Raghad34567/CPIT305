/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*
 * RSVPLogger handles reading and writing RSVP responses to a text log file.
 *
 * This class demonstrates the IOStream requirement of the project:
 *   - WRITE : FileWriter (append mode) + BufferedWriter  -> writes each RSVP entry
 *   - READ  : FileReader + BufferedReader               -> reads all entries for Reports
 *
 * Log file location : <user.home>/invitation_logs/rsvp_log.txt
 * Log line format   : [YYYY-MM-DD HH:mm] | GuestName | EventName | Response | N companion(s)
 */
public class RSVPLogger {

    // Directory and file path for the RSVP log
    private static final String LOG_DIR =
            System.getProperty("user.home") + File.separator + "invitation_logs";

    private static final String LOG_FILE =
            LOG_DIR + File.separator + "rsvp_log.txt";

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // ─────────────────────────────────────────────────────────────
    //  WRITE — append one RSVP entry to the log file
    // ─────────────────────────────────────────────────────────────

    /**
     * Appends a new RSVP response line to the log file.
     * Uses FileWriter in append mode so old entries are never overwritten.
     * Creates the log directory automatically if it does not exist yet.
     */
    public static void logResponse(String guestName,
                                   String eventName,
                                   String response,
                                   int guestCount) {
        try {
            // Create the directory if it does not exist
            Files.createDirectories(Paths.get(LOG_DIR));

            String timestamp = LocalDateTime.now().format(FORMATTER);

            String line = "[" + timestamp + "] | "
                    + guestName  + " | "
                    + eventName  + " | "
                    + response   + " | "
                    + guestCount + " companion(s)"
                    + System.lineSeparator();

            // FileWriter(path, true) -> append mode, never overwrites
            try (FileWriter fw = new FileWriter(LOG_FILE, true);
                 BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(line);
            }

            System.out.println("RSVPLogger: entry saved -> " + line.trim());

        } catch (IOException e) {
            System.err.println("RSVPLogger: could not write log: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────────────────────
    //  READ — return every log line as a list of strings
    // ─────────────────────────────────────────────────────────────

    /**
     * Reads every line from the RSVP log file using FileReader + BufferedReader.
     * Returns an empty list if the file does not exist yet (no RSVPs logged).
     */
    public static List<String> readAllLogs() {
        List<String> lines = new ArrayList<>();
        File file = new File(LOG_FILE);

        // If no one has responded yet, just return an empty list
        if (!file.exists()) {
            return lines;
        }

        try (FileReader fr = new FileReader(file);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }

        } catch (IOException e) {
            System.err.println("RSVPLogger: could not read log: " + e.getMessage());
        }

        return lines;
    }

    // ─────────────────────────────────────────────────────────────
    //  UTILITY — return the absolute path so users can find the file
    // ─────────────────────────────────────────────────────────────

    /** Returns the full path of the log file (shown in ReportsFrame). */
    public static String getLogFilePath() {
        return LOG_FILE;
    }
}