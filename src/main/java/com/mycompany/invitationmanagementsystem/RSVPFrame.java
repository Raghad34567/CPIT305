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
        setTitle("RSVP");

        // Set frame size
        setSize(700, 550);

        // Open frame in center of screen
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

        // Create buttons that the user can click.
        JButton submit = new JButton("Submit Response");
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

        // This action runs when the user changes the RSVP response.
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

        // This action runs when the user clicks Submit Response.
        submit.addActionListener(e -> {
            if (response.getSelectedItem() == null) {
                return;
            }

            String status = response.getSelectedItem().toString();
            int guestCount;

            /*
             * Strategy Pattern:
             * The RSVP behavior is selected based on the guest response.
             * AcceptRSVPStrategy validates and reads the guest count.
             * DeclineRSVPStrategy returns 0 automatically.
             */
            try {
                RSVPStrategy strategy = RSVPStrategyFactory.getStrategy(status);
                guestCount = strategy.getGuestCount(guests);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }

            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.connect();

                // Prepare the SQL statement to update the RSVP response safely.
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE guests SET response=?, guest_count=? " +
                                "WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");

                ps.setString(1, status);
                ps.setInt(2, guestCount);
                ps.setString(3, guestEmail);

                // Execute an SQL command that changes data.
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    String eventName     = "Event";
                    String eventDate     = "Date";
                    String eventLocation = "Location";
                    String guestName     = guestEmail; // fallback

                    // Get the guest name from the database.
                    PreparedStatement namePs = conn.prepareStatement(
                            "SELECT name FROM guests WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");

                    namePs.setString(1, guestEmail);

                    ResultSet nameRs = namePs.executeQuery();

                    if (nameRs.next()) {
                        guestName = nameRs.getString("name");
                    }

                    // Get event details from the database.
                    PreparedStatement eventPs = conn.prepareStatement(
                            "SELECT name, date, location FROM events WHERE id=?");

                    eventPs.setInt(1, eventId);

                    ResultSet rs = eventPs.executeQuery();

                    if (rs.next()) {
                        eventName     = rs.getString("name");
                        eventDate     = rs.getString("date");
                        eventLocation = rs.getString("location");
                    }

                    // Close this resource after finishing to avoid connection problems.
                    conn.close();

                    // Write this RSVP response to the log file (IOStream - FileWriter).
                    RSVPLogger.logResponse(guestName, eventName, status, guestCount);

                    // Show the thank you window.
                    new ThankYouFrame(
                            status.equals("Accept with pleasure"),
                            guestName,
                            eventName,
                            eventDate,
                            eventLocation
                    ).setVisible(true);

                    // Close the current window.
                    dispose();

                } else {
                    // Close this resource after finishing to avoid connection problems.
                    conn.close();

                    // Show a message if the guest email was not found.
                    JOptionPane.showMessageDialog(this,
                            "Guest not found!\nEmail used: " + guestEmail);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        // This action runs when the user clicks Back.
        back.addActionListener(e -> {
            invitationFrame.setVisible(true);
            dispose();
        });
    }

    private String cleanEmail(String text) {
        try {
            if (text == null) {
                return "";
            }

            text = text.trim();

            if (text.contains("guestEmail=")) {
                text = text.substring(text.indexOf("guestEmail=") + "guestEmail=".length());

                if (text.contains("&")) {
                    text = text.substring(0, text.indexOf("&"));
                }
            }

            text = URLDecoder.decode(text, StandardCharsets.UTF_8);
            return text.trim().toLowerCase();

        } catch (Exception e) {
            return text.trim().toLowerCase();
        }
    }
}