package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * This frame is used to add, edit, and manage guests.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class ManageGuestsFrame extends JFrame {

    DefaultTableModel model;
    JComboBox<String> eventBox;
    int selectedEventId = -1;
    DashboardFrame dashboard;

    // Constructor: builds the main window and prepares all GUI components.
    public ManageGuestsFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Manage Guests");

        // Set frame size
        // Set the size of the window.
        setSize(900, 620);

        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);

        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout(0, 0));

        eventBox = UITheme.createComboBox("Select Event");

        // Show more events in combo box
        eventBox.setMaximumRowCount(20);

        // Create a panel to organize the components on the screen.
        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);

        // Create a panel to organize the components on the screen.
        JPanel northWrapper = new JPanel(new BorderLayout(0, 0));
        northWrapper.setOpaque(false);
        northWrapper.add(UITheme.createHeader("Guest List"), BorderLayout.NORTH);
        northWrapper.add(topPanel, BorderLayout.SOUTH);

        main.add(northWrapper, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Guest Name", "Email"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        UITheme.styleTable(table);

        JScrollPane scroll = UITheme.createStyledScroll(table);

        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4, 24, 4, 24),
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true)));

        main.add(scroll, BorderLayout.CENTER);

        // Create a button that the user can click.
        JButton add = new JButton("Add");
        // Create a button that the user can click.
        JButton edit = new JButton("Edit");
        // Create a button that the user can click.
        JButton delete = new JButton("Delete");
        // Create a button that the user can click.
        JButton sendInvitations = new JButton("Send Invitations");
        // Create a button that the user can click.
        JButton back = new JButton("Back");

        // Create a panel to organize the components on the screen.
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));

        buttons.setOpaque(false);

        buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));

        UITheme.styleButton(add);
        UITheme.styleButton(edit);
        UITheme.styleButton(delete);
        UITheme.styleButton(sendInvitations);
        UITheme.styleButton(back);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(sendInvitations);
        buttons.add(back);

        main.add(buttons, BorderLayout.SOUTH);

        add(main);

        loadEvents();

        // This action runs when the user clicks this button.
        eventBox.addActionListener(e -> loadGuests());

        // ================= ADD GUEST =================
        // This action runs when the user clicks this button.
        add.addActionListener(e -> {

            if (selectedEventId == -1) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Guest Name:");
            String email = JOptionPane.showInputDialog(this, "Guest Email:");

            if (name == null || name.trim().isEmpty()) {
                return;
            }

            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Enter valid email");
                return;
            }

            // Use a thread so this work can run separately from the main screen.
            new Thread(() -> {
                try {
                    // Connect to the database before running the SQL query.
                    Connection conn = DBConnection.connect();

                    // Prepare the SQL statement to send it safely to the database.
                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO guests(name, email, event_id) VALUES (?,?,?)");

                    ps.setString(1, name.trim());
                    ps.setString(2, email.trim().toLowerCase());
                    ps.setInt(3, selectedEventId);

                    // Execute an SQL command that changes data or creates tables.
                    ps.executeUpdate();

                    // Close this resource after finishing to avoid connection problems.
                    ps.close();
                    // Close this resource after finishing to avoid connection problems.
                    conn.close();

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() -> {
                        model.addRow(new Object[]{
                                name.trim(),
                                email.trim().toLowerCase()
                        });

                        // Show a message box to tell the user the result.
                        JOptionPane.showMessageDialog(this, "Guest Added!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() ->
                            // Show a message box to tell the user the result.
                            JOptionPane.showMessageDialog(this, "Database/File Error!"));
                }

            }).start();
        });

        // ================= EDIT =================
        // This action runs when the user clicks this button.
        edit.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Select a row");
                return;
            }

            Object oldEmailObj = model.getValueAt(row, 1);

            if (oldEmailObj == null) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "This row has empty email");
                return;
            }

            String oldEmail = oldEmailObj.toString();

            String newName = JOptionPane.showInputDialog(this,
                    "Edit Name:",
                    model.getValueAt(row, 0));

            String newEmail = JOptionPane.showInputDialog(this,
                    "Edit Email:",
                    model.getValueAt(row, 1));

            if (newName == null || newName.trim().isEmpty()) {
                return;
            }

            if (newEmail == null || newEmail.trim().isEmpty() || !newEmail.contains("@")) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Enter valid email");
                return;
            }

            // Use a thread so this work can run separately from the main screen.
            new Thread(() -> {

                try {
                    // Connect to the database before running the SQL query.
                    Connection conn = DBConnection.connect();

                    // Prepare the SQL statement to send it safely to the database.
                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE guests SET name=?, email=? WHERE email=? AND event_id=?");

                    ps.setString(1, newName.trim());
                    ps.setString(2, newEmail.trim().toLowerCase());
                    ps.setString(3, oldEmail.trim().toLowerCase());
                    ps.setInt(4, selectedEventId);

                    // Execute an SQL command that changes data or creates tables.
                    ps.executeUpdate();

                    // Close this resource after finishing to avoid connection problems.
                    ps.close();
                    // Close this resource after finishing to avoid connection problems.
                    conn.close();

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() -> {

                        model.setValueAt(newName.trim(), row, 0);
                        model.setValueAt(newEmail.trim().toLowerCase(), row, 1);

                        // Show a message box to tell the user the result.
                        JOptionPane.showMessageDialog(this, "Guest updated!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() ->
                            // Show a message box to tell the user the result.
                            JOptionPane.showMessageDialog(this, "Database Error!"));
                }

            }).start();
        });

        // ================= DELETE =================
        // This action runs when the user clicks this button.
        delete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Select a row");
                return;
            }

            Object emailObj = model.getValueAt(row, 1);

            if (emailObj == null) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "This row has empty email");
                return;
            }

            String email = emailObj.toString();

            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.connect();

                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM guests WHERE email=? AND event_id=?");

                ps.setString(1, email.trim().toLowerCase());
                ps.setInt(2, selectedEventId);

                // Execute an SQL command that changes data or creates tables.
                ps.executeUpdate();

                // Close this resource after finishing to avoid connection problems.
                ps.close();
                // Close this resource after finishing to avoid connection problems.
                conn.close();

                model.removeRow(row);

            } catch (Exception ex) {
                ex.printStackTrace();
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        // ================= SEND INVITATIONS =================
        // This action runs when the user clicks this button.
        sendInvitations.addActionListener(e -> {

            if (selectedEventId == -1) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }

            if (model.getRowCount() == 0) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                        "No guests to send invitations");
                return;
            }

            String eventName = getSelectedEventName();

            String encodedEventName =
                    URLEncoder.encode(eventName, StandardCharsets.UTF_8);

            List<String[]> guests = new ArrayList<>();

            // Loop through the data and process each item.
            for (int i = 0; i < model.getRowCount(); i++) {

                Object nameValue = model.getValueAt(i, 0);
                Object emailValue = model.getValueAt(i, 1);

                if (nameValue == null || emailValue == null) {
                    continue;
                }

                String guestName = nameValue.toString().trim();
                String guestEmail = emailValue.toString()
                        .trim()
                        .toLowerCase();

                if (guestName.isEmpty()
                        || guestEmail.isEmpty()
                        || !guestEmail.contains("@")) {
                    continue;
                }

                guests.add(new String[]{guestName, guestEmail});
            }

            if (guests.isEmpty()) {

                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                        "No valid guests found.");

                return;
            }

            sendInvitations.setEnabled(false);
            sendInvitations.setText("Sending...");

            // Use a thread so this work can run separately from the main screen.
            new Thread(() -> {

                AtomicInteger sentCount = new AtomicInteger(0);
                AtomicInteger failedCount = new AtomicInteger(0);

                int poolSize = Math.min(5, guests.size());

                ExecutorService threadPool =
                        Executors.newFixedThreadPool(poolSize);

                // Loop through the data and process each item.
                for (String[] guest : guests) {

                    threadPool.submit(() -> {

                        String guestName = guest[0];
                        String guestEmail = guest[1];

                        try {

                            String encodedGuestEmail =
                                    URLEncoder.encode(guestEmail,
                                            StandardCharsets.UTF_8);

                            String inviteLink =
                                    "http://www.invite.com/"
                                    + encodedEventName
                                    + "?guestEmail="
                                    + encodedGuestEmail;

                            EmailClient.sendInvitationRequest(
                                    guestEmail,
                                    guestName,
                                    inviteLink);

                            int currentSent =
                                    sentCount.incrementAndGet();

                            // Run the GUI code on the Swing event thread.
                            SwingUtilities.invokeLater(() ->
                                    sendInvitations.setText(
                                            "Sent "
                                            + currentSent
                                            + " / "
                                            + guests.size()));

                        } catch (Exception ex) {

                            failedCount.incrementAndGet();
                            ex.printStackTrace();
                        }
                    });
                }

                threadPool.shutdown();

                try {

                    threadPool.awaitTermination(
                            5,
                            TimeUnit.MINUTES);

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();
                }

                // Run the GUI code on the Swing event thread.
                SwingUtilities.invokeLater(() -> {

                    sendInvitations.setEnabled(true);

                    sendInvitations.setText("Send Invitations");

                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this,
                            "Invitation sending finished!\n"
                            + "Sent: "
                            + sentCount.get()
                            + "\nFailed: "
                            + failedCount.get());
                });

            }).start();
        });

        // ================= BACK =================
        // This action runs when the user clicks this button.
        back.addActionListener(e -> {

            // Show the selected window to the user.
            dashboard.setVisible(true);
            // Close the current window.
            dispose();
        });
    }

    // ================= LOAD EVENTS =================
    private void loadEvents() {

        try {

            // Connect to the database before running the SQL query.
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();

            // Load events ordered by ID
            // Execute an SQL query that reads data from the database.
            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name FROM events ORDER BY id ASC");

            eventBox.removeAllItems();

            // Loop through the data and process each item.
            while (rs.next()) {

                eventBox.addItem(
                        rs.getInt("id")
                        + " : "
                        + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {

                eventBox.setSelectedIndex(0);

                loadGuests();
            }

            // Close this resource after finishing to avoid connection problems.
            rs.close();
            // Close this resource after finishing to avoid connection problems.
            stmt.close();
            // Close this resource after finishing to avoid connection problems.
            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // ================= LOAD GUESTS =================
    private void loadGuests() {

        model.setRowCount(0);

        if (eventBox.getSelectedItem() == null) {
            return;
        }

        String selected =
                eventBox.getSelectedItem().toString();

        selectedEventId =
                Integer.parseInt(
                        selected.split(":")[0].trim());

        try {

            // Connect to the database before running the SQL query.
            Connection conn = DBConnection.connect();

            PreparedStatement ps =
                    // Prepare the SQL statement to send it safely to the database.
                    conn.prepareStatement(
                            "SELECT * FROM guests WHERE event_id=?");

            ps.setInt(1, selectedEventId);

            // Execute an SQL query that reads data from the database.
            ResultSet rs = ps.executeQuery();

            // Loop through the data and process each item.
            while (rs.next()) {

                String name = rs.getString("name");
                String email = rs.getString("email");

                if (name == null) {
                    name = "";
                }

                if (email == null) {
                    email = "";
                }

                model.addRow(new Object[]{
                        name,
                        email
                });
            }

            // Close this resource after finishing to avoid connection problems.
            rs.close();
            // Close this resource after finishing to avoid connection problems.
            ps.close();
            // Close this resource after finishing to avoid connection problems.
            conn.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    // ================= GET EVENT NAME =================
    private String getSelectedEventName() {

        String selected =
                eventBox.getSelectedItem().toString();

        return selected.substring(
                selected.indexOf(":") + 1).trim();
    }
}
