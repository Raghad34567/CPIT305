package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.net.http.*;
import java.net.URI;

public class RSVPFrame extends JFrame {

    GuestInvitationFrame invitationFrame;
    String guestName;
    int    eventId;

    public RSVPFrame(GuestInvitationFrame invitationFrame, String guestName, int eventId) {
        this.invitationFrame = invitationFrame;
        this.guestName       = guestName;
        this.eventId         = eventId;

        setTitle("RSVP");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(500, 400);
        card.setLayout(new BorderLayout(20, 20));

        // ── Title ─────────────────────────────────────
        JLabel title = new JLabel("Kindly Confirm Your Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JLabel divider = new JLabel("✦  ───────────────────────────  ✦", SwingConstants.CENTER);
        divider.setFont(new Font("Serif", Font.PLAIN, 12));
        divider.setForeground(new Color(185, 125, 148));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(title,   BorderLayout.CENTER);
        titlePanel.add(divider, BorderLayout.SOUTH);

        // ── Center Fields ─────────────────────────────
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // ComboBox مع UITheme styling ✅
        JComboBox<String> response = UITheme.createComboBox("Attendance Response");
        response.addItem("Accept with Pleasure");
        response.addItem("Regretfully Decline");
        response.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        response.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(response);
        centerPanel.add(Box.createVerticalStrut(16));

        // حقل عدد الضيوف
        JTextField guests = new JTextField();
        guests.setFont(new Font("Serif", Font.PLAIN, 16));
        guests.setForeground(UITheme.TEXT);
        guests.setBackground(new Color(255, 248, 251));
        guests.setBorder(BorderFactory.createCompoundBorder(
            new javax.swing.border.TitledBorder(
                BorderFactory.createLineBorder(UITheme.BORDER, 1, true),
                " Number of Guests ",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                new Font("SansSerif", Font.PLAIN, 11),
                UITheme.TEXT_LIGHT),
            BorderFactory.createEmptyBorder(4, 10, 6, 10)));
        guests.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        guests.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(guests);

        // ── Buttons ───────────────────────────────────
        JButton submit = new JButton("Submit Response");
        JButton back   = new JButton("Back");
        UITheme.styleButton(submit);
        UITheme.styleButton(back);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 12));
        bottomPanel.setOpaque(false);
        bottomPanel.add(submit);
        bottomPanel.add(back);

        card.add(titlePanel,   BorderLayout.NORTH);
        card.add(centerPanel,  BorderLayout.CENTER);
        card.add(bottomPanel,  BorderLayout.SOUTH);

        main.add(card);
        add(main);

        // ── Logic ─────────────────────────────────────
        response.addActionListener(e -> {
            if (response.getSelectedItem().toString().contains("Decline")) {
                guests.setText("0");
                guests.setEnabled(false);
            } else {
                guests.setEnabled(true);
                guests.setText("");
            }
        });

        submit.addActionListener(e -> {
            String status     = response.getSelectedItem().toString();
            String input      = guests.getText().trim();
            int    guestCount = 0;

            if (status.contains("Accept")) {
                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Enter number of guests");
                    return;
                }
                try {
                    guestCount = Integer.parseInt(input);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number");
                    return;
                }
            }

            int finalGuestCount = guestCount;
            new Thread(() -> {
                try {
                    Connection conn = DBConnection.connect();
                    PreparedStatement ps = conn.prepareStatement(
                        "UPDATE guests SET response=?, guest_count=? WHERE name=? AND event_id=?");
                    ps.setString(1, status);
                    ps.setInt(2, finalGuestCount);
                    ps.setString(3, guestName);
                    ps.setInt(4, eventId);
                    ps.executeUpdate();

                    try {
                        String data = "name=" + guestName + "&response=" + status;
                        HttpClient client = HttpClient.newHttpClient();
                        HttpRequest request = HttpRequest.newBuilder()
                            .POST(HttpRequest.BodyPublishers.ofString(data))
                            .uri(URI.create("https://postman-echo.com/post"))
                            .setHeader("Content-Type", "application/x-www-form-urlencoded")
                            .build();
                        HttpResponse<String> responseNet = client.send(
                            request, HttpResponse.BodyHandlers.ofString());
                        System.out.println(responseNet.body());
                    } catch (Exception netEx) {
                        netEx.printStackTrace();
                    }

                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Response saved & sent!"));

                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this, "Database Error!"));
                }
            }).start();
        });

        back.addActionListener(e -> {
            invitationFrame.setVisible(true);
            dispose();
        });
    }
}