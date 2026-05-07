package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

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

        // card
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 25));
                g2.fillRoundRect(4, 6, getWidth() - 6, getHeight() - 6, 26, 26);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 24, 24);
                g2.setColor(new Color(200, 150, 160));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 24, 24);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setLayout(new BorderLayout(20, 20));

        JTextField linkField = new JTextField();
        linkField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        linkField.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));
        linkField.setPreferredSize(new Dimension(400, 50));

        JButton openButton = new JButton("Open Invitation");
        JButton logout     = new JButton("Logout");
        UITheme.styleButton(openButton);
        UITheme.styleButton(logout);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.add(openButton);
        buttonPanel.add(logout);

        card.add(linkField, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        JLabel instruction = new JLabel(
            "Paste the link sent to you via SMS or Email",
            SwingConstants.CENTER);
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
            try {
                String guestName = link.substring(link.lastIndexOf("/") + 1);
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT event_id FROM guests WHERE name=?");
                ps.setString(1, guestName);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int eventId = rs.getInt("event_id");
                    new GuestInvitationFrame(guestName, eventId).setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Guest not found!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid link!");
            }
        });

        logout.addActionListener(e -> {
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });
    }
}
