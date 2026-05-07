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

        JPanel card = UITheme.createCard(500, 380);
        card.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Kindly Confirm Your Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.gridx  = 0;
        gbc.weightx = 1;

        JComboBox<String> response = new JComboBox<>(
            new String[]{"Accept with Pleasure", "Regretfully Decline"});
        response.setFont(new Font("Serif", Font.PLAIN, 16));
        response.setPreferredSize(new Dimension(250, 35));
        gbc.gridy = 0;
        centerPanel.add(response, gbc);

        JTextField guests = new JTextField();
        guests.setFont(new Font("Serif", Font.PLAIN, 16));
        guests.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 100, 130), 1),
            "Number of Guests"));
        guests.setPreferredSize(new Dimension(250, 40));
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

        card.add(title, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        response.addActionListener(e -> {
            if (response.getSelectedItem().toString().contains("Decline")) {
                guests.setText("0");
                guests.setEnabled(false);
            } else {
                guests.setEnabled(true);
                guests.setText("");
            }
        });

        // ================= SUBMIT =================
        submit.addActionListener(e -> {
            String status = response.getSelectedItem().toString();
            String input  = guests.getText().trim();
            int guestCount = 0;

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

                    SwingUtilities.invokeLater(
                        () -> JOptionPane.showMessageDialog(this, "Response saved & sent!"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(
                        () -> JOptionPane.showMessageDialog(this, "Database Error!"));
                }
            }).start();
        });

        back.addActionListener(e -> {
            invitationFrame.setVisible(true);
            dispose();
        });
    }
}
