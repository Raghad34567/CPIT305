package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * This frame displays the invitation details for guests.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class GuestInvitationFrame extends JFrame {

    String invitationLink;
    String guestEmail;
    int eventId;

    public GuestInvitationFrame(String invitationLink, int eventId) {
        this.invitationLink = invitationLink;
        this.guestEmail = extractGuestEmail(invitationLink);
        this.eventId = eventId;

        // Set frame title
        setTitle("Invitation");
        // Set frame size
        setSize(820, 700);
        // Open frame in center of screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(120, 40, 70, 50));
                g2.fillRoundRect(7, 9, w - 9, h - 9, 36, 36);

                g2.setColor(new Color(255, 252, 246));
                g2.fillRoundRect(0, 0, w - 7, h - 7, 34, 34);

                g2.setColor(new Color(198, 155, 80));
                g2.setStroke(new BasicStroke(2.4f));
                g2.drawRoundRect(4, 4, w - 15, h - 15, 30, 30);

                g2.setColor(new Color(220, 185, 110, 130));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(11, 11, w - 29, h - 29, 24, 24);

                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setPreferredSize(new Dimension(630, 560));
        card.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 6, 50));

        JLabel tag = new JLabel("✦ A Special Invitation ✦", SwingConstants.CENTER);
        tag.setFont(new Font("Serif", Font.ITALIC, 13));
        tag.setForeground(new Color(180, 135, 75));
        tag.setAlignmentX(CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel("Join Us In Celebration", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Serif", Font.BOLD, 30));
        mainTitle.setForeground(UITheme.PRIMARY);
        mainTitle.setAlignmentX(CENTER_ALIGNMENT);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(7, 0, 4, 0));

        JLabel goldDiv = new JLabel("✦  ─────────────────────────  ✦", SwingConstants.CENTER);
        goldDiv.setFont(new Font("Serif", Font.PLAIN, 14));
        goldDiv.setForeground(new Color(198, 155, 80));
        goldDiv.setAlignmentX(CENTER_ALIGNMENT);

        topPanel.add(tag);
        topPanel.add(mainTitle);
        topPanel.add(goldDiv);

        String eventName = "Event";
        String eventDate = "Date";
        String eventLocation = "Location";

        try {
            java.sql.Connection conn = DBConnection.connect();
            java.sql.PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, date, location FROM events WHERE id = ?");
            ps.setInt(1, eventId);

            java.sql.ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                eventName = rs.getString("name");
                eventDate = rs.getString("date");
                eventLocation = rs.getString("location");
            }

            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final String fEventName = eventName;
        final String fEventDate = eventDate;
        final String fEventLocation = eventLocation;

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(25, 65, 20, 65));

        Object[][] lines = {
            {"We are delighted to invite you to", 3},
            {"", 4},
            {"celebrate this special occasion", 0},
            {"", 4},
            {fEventName, 5},
            {"", 4},
            {"An unforgettable event awaits you.", 0},
            {"", 4},
            {"✦  Event Details  ✦", 1},
            {"", 4},
            {"📅 Date :   " + fEventDate, 2},
            {"📍 Venue :  " + fEventLocation, 2},
            {"", 4},
            {"Your presence would make this", 3},
            {"celebration even more meaningful.", 3},
            {"", 4},
            {"Kindly confirm your attendance through RSVP.", 3},};

        for (Object[] row : lines) {
            String txt = (String) row[0];
            int style = (int) row[1];

            if (style == 4) {
                centerPanel.add(Box.createVerticalStrut(6));
                continue;
            }

            JLabel l = new JLabel(txt, SwingConstants.CENTER);
            l.setAlignmentX(CENTER_ALIGNMENT);

            if (style == 2) {
                l.setFont(new Font("Serif", Font.BOLD, 15));
                l.setForeground(UITheme.TEXT);
            } else if (style == 3) {
                l.setFont(new Font("Serif", Font.ITALIC, 14));
                l.setForeground(UITheme.TEXT_LIGHT);
            } else if (style == 5) {
                l.setFont(new Font("Serif", Font.BOLD, 18));
                l.setForeground(new Color(160, 100, 40));
            } else {
                l.setFont(new Font("Serif", Font.PLAIN, 15));
                l.setForeground(UITheme.TEXT);
            }

            centerPanel.add(l);
        }

        JButton rsvp = new JButton("RSVP");
        JButton back = new JButton("Back");

        UITheme.styleButton(rsvp);
        UITheme.styleButton(back);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 16));
        btnPanel.setOpaque(false);
        btnPanel.add(rsvp);
        btnPanel.add(back);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        rsvp.addActionListener(e -> {
            new RSVPFrame(this, guestEmail, eventId).setVisible(true);
            setVisible(false);
        });

        back.addActionListener(e -> {
            new InvitationLinkFrame().setVisible(true);
            dispose();
        });
    }

    private String extractGuestEmail(String link) {
        try {
            if (link == null) {
                return "";
            }

            link = link.trim();

            if (link.contains("guestEmail=")) {
                String email = link.substring(link.indexOf("guestEmail=") + "guestEmail=".length());

                if (email.contains("&")) {
                    email = email.substring(0, email.indexOf("&"));
                }

                return URLDecoder.decode(email, StandardCharsets.UTF_8).trim().toLowerCase();
            }

            return link.trim().toLowerCase();

        } catch (Exception e) {
            return "";
        }
    }
}
