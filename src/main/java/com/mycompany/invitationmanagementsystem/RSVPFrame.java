package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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

    // Observer Pattern: list of all observers subscribed to RSVP events.
    private static List<RSVPObserver> observers = new ArrayList<>();

    // Constructor: builds the main window and prepares all GUI components.
    public RSVPFrame(GuestInvitationFrame invitationFrame, String guestEmail, int eventId) {
        this.invitationFrame = invitationFrame;
        this.guestEmail      = cleanEmail(guestEmail);
        this.eventId         = eventId;

        setTitle("RSVP");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(500, 380);
        card.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Kindly Confirm Your Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

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

        JButton submit = new JButton("Submit Response");
        JButton back   = new JButton("Back");

        UITheme.styleButton(submit);
        UITheme.styleButton(back);

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
                /*
                 * Singleton Pattern:
                 * We get the shared database connection from DBConnection.
                 * We do not close it here because it is shared by the whole project.
                 */
                Connection conn = DBConnection.getInstance().getConnection();

                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE guests SET response=?, guest_count=? " +
                                "WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");

                ps.setString(1, status);
                ps.setInt(2, guestCount);
                ps.setString(3, guestEmail);

                int rows = ps.executeUpdate();

                if (rows > 0) {
                    String eventName     = "Event";
                    String eventDate     = "Date";
                    String eventLocation = "Location";
                    String guestName     = guestEmail; // fallback

                    PreparedStatement namePs = conn.prepareStatement(
                            "SELECT name FROM guests WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))");

                    namePs.setString(1, guestEmail);

                    ResultSet nameRs = namePs.executeQuery();

                    if (nameRs.next()) {
                        guestName = nameRs.getString("name");
                    }

                    PreparedStatement eventPs = conn.prepareStatement(
                            "SELECT name, date, location FROM events WHERE id=?");

                    eventPs.setInt(1, eventId);

                    ResultSet rs = eventPs.executeQuery();

                    if (rs.next()) {
                        eventName     = rs.getString("name");
                        eventDate     = rs.getString("date");
                        eventLocation = rs.getString("location");
                    }

                    // Write this RSVP response to the log file.
                    RSVPLogger.logResponse(guestName, eventName, status, guestCount);

                    // Observer Pattern: notify all subscribed observers about the new RSVP.
                    notifyObservers(guestName, status);

                    new ThankYouFrame(
                            status.equals("Accept with pleasure"),
                            guestName,
                            eventName,
                            eventDate,
                            eventLocation
                    ).setVisible(true);

                    dispose();

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Guest not found!\nEmail used: " + guestEmail);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        back.addActionListener(e -> {
            invitationFrame.setVisible(true);
            dispose();
        });
    }

    // Observer Pattern: allows an observer to subscribe to RSVP notifications.
    public static void addObserver(RSVPObserver observer) {
        observers.add(observer);
    }

    // Observer Pattern: notifies all subscribed observers when an RSVP is submitted.
    private static void notifyObservers(String guestName, String response) {
        for (RSVPObserver observer : observers) {
            observer.onRSVPReceived(guestName, response);
        }
    }

    // This method cleans and decodes the guest email from the invitation link.
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