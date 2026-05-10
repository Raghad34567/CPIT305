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
    ThreadSafeResponseLoader responseLoader = new ThreadSafeResponseLoader();

    public ResponsesFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        // Set frame title
        setTitle("Guest Responses");

        // Set frame size
        setSize(900, 600);

        // Open frame in center of screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        // North: header + event selector
        eventBox = UITheme.createComboBox("Select Event");

        // Show more events in the combo box list
        eventBox.setMaximumRowCount(20);

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);

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
        JButton refresh = new JButton("Refresh");
        JButton back = new JButton("Back");

        UITheme.styleButton(refresh);
        UITheme.styleButton(back);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));
        bottom.add(refresh);
        bottom.add(back);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        loadEvents();

        eventBox.addActionListener(e -> loadResponses());

        refresh.addActionListener(e -> loadResponses());

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void loadEvents() {
        try {
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name FROM events ORDER BY id ASC");

            eventBox.removeAllItems();

            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadResponses();
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events!");
        }
    }

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
        new Thread(() -> {
            try {
                // this thread sleeps here using wait()
                // it wakes up when Thread 1 calls notifyAll()
                List<Object[]> rows = responseLoader.waitForData();

                // update the table on the main GUI thread
                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);

                    for (Object[] row : rows) {
                        model.addRow(row);
                    }

                    System.out.println("Loaded " + responseLoader.getRowCount() + " responses");
                });

            } catch (Exception e) {
                e.printStackTrace();

                SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Error loading responses!")
                );
            }
        }).start();
    }
}