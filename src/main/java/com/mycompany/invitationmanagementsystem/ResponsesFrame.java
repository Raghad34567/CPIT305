package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

/*
 * This frame displays guest responses for events.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class ResponsesFrame extends JFrame {

    DashboardFrame dashboard;
    JComboBox<String> eventBox;
    DefaultTableModel model;

    // handles loading responses safely across multiple threads
    // Use a thread so this work can run separately from the main screen.
    ThreadSafeResponseLoader responseLoader = new ThreadSafeResponseLoader();

    // Constructor: builds the main window and prepares all GUI components.
    public ResponsesFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Guest Responses");

        // Set frame size
        // Set the size of the window.
        setSize(900, 600);

        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        // North: header + event selector
        eventBox = UITheme.createComboBox("Select Event");

        // Show more events in the combo box list
        eventBox.setMaximumRowCount(20);

        // Create a panel to organize the components on the screen.
        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);

        // Create a panel to organize the components on the screen.
        JPanel northWrapper = new JPanel(new BorderLayout(0, 0));
        northWrapper.setOpaque(false);
        northWrapper.add(UITheme.createHeader("RSVP Responses"), BorderLayout.NORTH);
        northWrapper.add(topPanel, BorderLayout.SOUTH);
        main.add(northWrapper, BorderLayout.NORTH);

        // Center: table that shows guest responses
        model = new DefaultTableModel(
                new String[]{"Guest Name", "Email", "Response", "Guests Count"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scroll = UITheme.createStyledScroll(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(4, 24, 4, 24));
        main.add(scroll, BorderLayout.CENTER);

        // South: buttons
        // Create a button that the user can click.
        JButton refresh = new JButton("Refresh");
        // Create a button that the user can click.
        JButton back = new JButton("Back");

        UITheme.styleButton(refresh);
        UITheme.styleButton(back);

        // Create a panel to organize the components on the screen.
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));
        bottom.add(refresh);
        bottom.add(back);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        loadEvents();

        // This action runs when the user clicks this button.
        eventBox.addActionListener(e -> loadResponses());

        // This action runs when the user clicks this button.
        refresh.addActionListener(e -> loadResponses());

        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            dashboard.setVisible(true);
            // Close the current window.
            dispose();
        });
    }

    // This method handles the loadEvents part of the class logic.
    private void loadEvents() {
        try {
            // Connect to the database before running the SQL query.
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();

            // Execute an SQL query that reads data from the database.
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name FROM events ORDER BY id ASC");

            eventBox.removeAllItems();

            // Loop through the data and process each item.
            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadResponses();
            }

            // Close this resource after finishing to avoid connection problems.
            rs.close();
            // Close this resource after finishing to avoid connection problems.
            stmt.close();
            // Close this resource after finishing to avoid connection problems.
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            // Show a message box to tell the user the result.
            JOptionPane.showMessageDialog(this, "Error loading events!");
        }
    }

    // This method handles the loadResponses part of the class logic.
    private void loadResponses() {
        model.setRowCount(0);

        if (eventBox.getSelectedItem() == null) {
            return;
        }

        String selected = eventBox.getSelectedItem().toString();
        int selectedEventId = Integer.parseInt(selected.split(":")[0].trim());

        // Thread 1: loads data from the database in the background
        // so the GUI does not freeze while waiting
        responseLoader.loadResponses(selectedEventId);

        // Thread 2: waits for Thread 1 to finish, then fills the table
        // Use a thread so this work can run separately from the main screen.
        new Thread(() -> {
            try {
                // this thread sleeps here using wait()
                // it wakes up when Thread 1 calls notifyAll()
                List<Object[]> rows = responseLoader.waitForData();

                // update the table on the main GUI thread
                // Run the GUI code on the Swing event thread.
                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);

                    // Loop through the data and process each item.
                    for (Object[] row : rows) {
                        model.addRow(row);
                    }

                    System.out.println("Loaded " + responseLoader.getRowCount() + " responses");
                });

            } catch (Exception e) {
                e.printStackTrace();

                // Run the GUI code on the Swing event thread.
                SwingUtilities.invokeLater(() ->
                        // Show a message box to tell the user the result.
                        JOptionPane.showMessageDialog(this, "Error loading responses!")
                );
            }
        }).start();
    }
}
