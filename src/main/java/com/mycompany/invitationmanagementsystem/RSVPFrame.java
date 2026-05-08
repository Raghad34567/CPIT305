package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RSVPFrame extends JFrame {

    GuestInvitationFrame invitationFrame;
    String guestEmail;
    int eventId;

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

        // ── Styled ComboBox ──
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

        response.addActionListener(e -> {
            if (response.getSelectedItem() != null &&
                response.getSelectedItem().toString().equals("Decline")) {
                guests.setText("0");
                guests.setEnabled(false);
            } else {
                guests.setEnabled(true);
                guests.setText("");
            }
        });

        submit.addActionListener(e -> {
            if (response.getSelectedItem() == null) return;
            String status     = response.getSelectedItem().toString();
            int    guestCount = 0;
            if (status.equals("Accept")) {
                try {
                    guestCount = Integer.parseInt(guests.getText().trim());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Enter valid number");
                    return;
                }
            }
            try {
                Connection conn = DBConnection.connect();
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
                    PreparedStatement eventPs = conn.prepareStatement(
                            "SELECT name, date, location FROM events WHERE id=?");
                    eventPs.setInt(1, eventId);
                    ResultSet rs = eventPs.executeQuery();
                    if (rs.next()) {
                        eventName     = rs.getString("name");
                        eventDate     = rs.getString("date");
                        eventLocation = rs.getString("location");
                    }
                    conn.close();
                    new ThankYouFrame(
                            status.equals("Accept"),
                            guestEmail,
                            eventName,
                            eventDate,
                            eventLocation
                    ).setVisible(true);
                    dispose();
                } else {
                    conn.close();
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