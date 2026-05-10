package com.mycompany.invitationmanagementsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ThreadSafeResponseLoader
 *
 * Shows three threading concepts:
 *  - Thread      : loads data in the background so the GUI does not freeze
 *  - synchronized: only one thread can read or change the shared data at a time
 *  - wait/notify : a waiting thread sleeps until the data is ready, then wakes up
 */
/*
 * This class loads guest responses safely using threads without freezing the GUI.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class ThreadSafeResponseLoader {

    // shared data between threads
    private List<Object[]> loadedRows = new ArrayList<>();

    // true when the loader thread has finished
    private boolean isReady = false;

    // stores any error message if something goes wrong
    private String errorMessage = null;

    /**
     * Starts a new background thread that fetches guest data from the database.
     * When done, it wakes up any thread that is waiting for the data.
     */
    public synchronized void loadResponses(int eventId) {

        // reset before starting
        loadedRows.clear();
        isReady = false;
        errorMessage = null;

        Thread loaderThread = new Thread(() -> {

            List<Object[]> tempRows = new ArrayList<>();

            try {
                Connection conn = DBConnection.connect();

                PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, email, response, guest_count " +
                    "FROM guests WHERE event_id = ?"
                );
                ps.setInt(1, eventId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    String response = rs.getString("response");
                    if (response == null || response.trim().isEmpty()) {
                        response = "No Response Yet";
                    }

                    tempRows.add(new Object[]{
                        rs.getString("name"),
                        rs.getString("email"),
                        response,
                        rs.getInt("guest_count")
                    });
                }

                conn.close();

            } catch (Exception e) {
                setError("Database Error: " + e.getMessage());
                return;
            }

            // lock the object, save the data, then wake up waiting threads
            synchronized (this) {
                loadedRows = tempRows;
                isReady = true;
                notifyAll(); // wake up any thread sleeping in waitForData()
            }

        });

        loaderThread.setName("ResponseLoaderThread-Event-" + eventId);
        loaderThread.start();

        System.out.println("Thread started: " + loaderThread.getName());
    }

    /**
     * Blocks the calling thread until the data is ready.
     * Uses wait() to sleep and releases the lock while sleeping.
     */
    public synchronized List<Object[]> waitForData() throws InterruptedException {

        while (!isReady && errorMessage == null) {
            wait(); // sleep until notifyAll() is called
        }

        if (errorMessage != null) {
            throw new RuntimeException(errorMessage);
        }

        return new ArrayList<>(loadedRows);
    }

    /**
     * Returns the number of loaded rows.
     * synchronized so no other thread can change the list at the same time.
     */
    public synchronized int getRowCount() {
        return loadedRows.size();
    }

    // saves the error message and wakes up waiting threads
    private synchronized void setError(String message) {
        this.errorMessage = message;
        this.isReady = false;
        notifyAll();
    }
}