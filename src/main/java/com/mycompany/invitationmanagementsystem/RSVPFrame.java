package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * This frame allows guests to accept or decline invitations.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class RSVPFrame extends JFrame {

    GuestInvitationFrame invitationFrame;
    String guestEmail;
    int eventId;

    // Constructor: builds the main window and prepares all GUI components.
    public RSVPFrame(GuestInvitationFrame invitationFrame, String guestEmail, int eventId) {
        this.invitationFrame = invitationFrame;
        this.guestEmail      = cleanEmail(guestEmail);
        this.eventId         = eventId;

        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("RSVP");
        // Set frame size
        // Set the size of the window.
        setSize(700, 550);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(500, 380);
        card.setLayout(new BorderLayout(20, 20));

        // Create a label to display text for the user.
        JLabel title = new JLabel("Kindly Confirm Your Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        // Create a panel to organize the components on the screen.
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(10, 20, 10, 20);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.gridx   = 0;
        gbc.weightx = 1;

        
        JComboBox<String> response = UITheme.createComboBox("Response");
        response.addItem("Accept with pleasure");
        response.addItem("Regretfully Decline");
        gbc.gridy = 0;
        centerPanel.add(response, gbc);

        // Create a text field so the user can type data.
        JTextField guests = new JTextField();
        guests.setFont(new Font("Serif", Font.PLAIN, 16));
        guests.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                " Number of Guests ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.PLAIN, 11),
                UITheme.TEXT_LIGHT));
        gbc.gridy = 1;
        centerPanel.add(guests, gbc);

        // Create a button that the user can click.
        JButton submit = new JButton("Submit Response");
        // Create a button that the user can click.
        JButton back   = new JButton("Back");
        UITheme.styleButton(submit);
        UITheme.styleButton(back);

        // Create a panel to organize the components on the screen.
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(submit);
        bottomPanel.add(back);

        card.add(title,       BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        
        // This action runs when the user clicks this button.
        response.addActionListener(e -> {
            if (response.getSelectedItem() != null &&
                response.getSelectedItem().toString().equals("Regretfully Decline")) {
                guests.setText("0");
                guests.setEnabled(false);
            } else {
                guests.setEnabled(true);
                guests.setText("");
            }
        });

        // This action runs when the user clicks this button.
        submit.addActionListener(e -> {
            if (response.getSelectedItem() == null) return;
            String status     = response.getSelectedItem().toString();
            int    guestCount = 0;

            
            if (status.equals("Accept with pleasure")) {
                try {
                    guestCount = Integer.parseInt(guests.getText().trim());
                } catch (Exception ex) {
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this, "Enter valid number");
                    return;
                }
            }

            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.getInstance().getConnection();
                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE guests SET response=?, guest_count=? " +
                        "WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");
                ps.setString(1, status);
                ps.setInt(2, guestCount);
                ps.setString(3, guestEmail);
                // Execute an SQL command that changes data or creates tables.
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    String eventName     = "Event";
                    String eventDate     = "Date";
                    String eventLocation = "Location";
                    String guestName     = guestEmail; // a fallback 

                    
                    // Prepare the SQL statement to send it safely to the database.
                    PreparedStatement namePs = conn.prepareStatement(
                            "SELECT name FROM guests WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");
                    namePs.setString(1, guestEmail);
                    // Execute an SQL query that reads data from the database.
                    ResultSet nameRs = namePs.executeQuery();
                    if (nameRs.next()) {
                        guestName = nameRs.getString("name");
                    }

                    // Prepare the SQL statement to send it safely to the database.
                    PreparedStatement eventPs = conn.prepareStatement(
                            "SELECT name, date, location FROM events WHERE id=?");
                    eventPs.setInt(1, eventId);
                    // Execute an SQL query that reads data from the database.
                    ResultSet rs = eventPs.executeQuery();
                    if (rs.next()) {
                        eventName     = rs.getString("name");
                        eventDate     = rs.getString("date");
                        eventLocation = rs.getString("location");
                    }
                    // Do not close conn because it is the shared Singleton connection.

                    // Write this RSVP response to the log file (IOStream - FileWriter)
                    RSVPLogger.logResponse(guestName, eventName, status, guestCount);

                    new ThankYouFrame(
                            status.equals("Accept with pleasure"),
                            guestName,
                            eventName,
                            eventDate,
                            eventLocation
                    // Show the selected window to the user.
                    ).setVisible(true);
                    // Close the current window.
                    dispose();
                } else {
                    // Do not close conn because it is the shared Singleton connection.
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this,
                            "Guest not found!\nEmail used: " + guestEmail);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            invitationFrame.setVisible(true);
            // Close the current window.
            dispose();
        });
    }

    private String cleanEmail(String text) {
        try {
            if (text == null) return "";
            text = text.trim();
            if (text.contains("guestEmail=")) {
                text = text.substring(text.indexOf("guestEmail=") + "guestEmail=".length());
                if (text.contains("&")) text = text.substring(0, text.indexOf("&"));
            }
            text = URLDecoder.decode(text, StandardCharsets.UTF_8);
            return text.trim().toLowerCase();
        } catch (Exception e) {
            return text.trim().toLowerCase();
        }
    }
}