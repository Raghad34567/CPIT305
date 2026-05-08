package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class InvitationLinkFrame extends JFrame {

    public InvitationLinkFrame() {
        setTitle("Enter Invitation Link");
        setSize(600, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        JLabel title = new JLabel("Enter Your Invitation Link", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        JPanel card = UITheme.createCard(480, 180);
        card.setLayout(new BorderLayout(20, 20));

        JTextField linkField = new JTextField();
        linkField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        linkField.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));

        JButton openButton = new JButton("Open Invitation");
        JButton logout = new JButton("Logout");

        UITheme.styleButton(openButton);
        UITheme.styleButton(logout);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(openButton);
        buttonPanel.add(logout);

        card.add(linkField, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        JLabel instruction = new JLabel("Paste the link sent to you via Email", SwingConstants.CENTER);
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instruction.setForeground(UITheme.TEXT);
        instruction.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        main.add(title, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        main.add(instruction, BorderLayout.SOUTH);

        add(main);

        openButton.addActionListener(e -> {
            String link = linkField.getText().trim();

            if (link.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter invitation link");
                return;
            }

            String guestEmail = extractGuestEmail(link);

            if (guestEmail.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Invalid link! Email not found.");
                return;
            }

            try {
                Connection conn = DBConnection.connect();

                if (conn == null) {
                    JOptionPane.showMessageDialog(this, "Database connection failed!");
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT event_id FROM guests WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))"
                );

                ps.setString(1, guestEmail);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int eventId = rs.getInt("event_id");
                    new GuestInvitationFrame(link, eventId).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Guest not found!\nEmail used: " + guestEmail);
                }

                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Database Error!\nCheck if guests table has email column.");
            }
        });

        logout.addActionListener(e -> {
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });
    }

    private String extractGuestEmail(String link) {
        try {
            if (link == null) return "";

            link = link.trim();

            if (!link.contains("guestEmail=")) {
                return "";
            }

            String email = link.substring(link.indexOf("guestEmail=") + 11);

            if (email.contains("&")) {
                email = email.substring(0, email.indexOf("&"));
            }

            return URLDecoder.decode(email, StandardCharsets.UTF_8)
                    .trim()
                    .toLowerCase();

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}