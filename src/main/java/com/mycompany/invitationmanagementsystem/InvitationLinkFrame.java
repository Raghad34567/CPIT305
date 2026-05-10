package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/*
 * This frame allows guests to open invitation links.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class InvitationLinkFrame extends JFrame {

    // Constructor: builds the main window and prepares all GUI components.
    public InvitationLinkFrame() {
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Enter Invitation Link");
        // Set frame size
        // Set the size of the window.
        setSize(600, 420);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        // Create a label to display text for the user.
        JLabel title = new JLabel("Enter Your Invitation Link", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        JPanel card = UITheme.createCard(480, 180);
        card.setLayout(new BorderLayout(20, 20));

        // Create a text field so the user can type data.
        JTextField linkField = new JTextField();
        linkField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        linkField.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));

        // Create a button that the user can click.
        JButton openButton = new JButton("Open Invitation");
        // Create a button that the user can click.
        JButton logout = new JButton("Logout");

        UITheme.styleButton(openButton);
        UITheme.styleButton(logout);

        // Create a panel to organize the components on the screen.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(openButton);
        buttonPanel.add(logout);

        card.add(linkField, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        // Create a label to display text for the user.
        JLabel instruction = new JLabel("Paste the link sent to you via Email", SwingConstants.CENTER);
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instruction.setForeground(UITheme.TEXT);
        instruction.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        main.add(title, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        main.add(instruction, BorderLayout.SOUTH);

        add(main);

        // This action runs when the user clicks this button.
        openButton.addActionListener(e -> {
            String link = linkField.getText().trim();

            if (link.isEmpty()) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Enter invitation link");
                return;
            }

            String guestEmail = extractGuestEmail(link);

            if (guestEmail.isEmpty()) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Invalid link! Email not found.");
                return;
            }

            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.connect();

                if (conn == null) {
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this, "Database connection failed!");
                    return;
                }

                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT event_id FROM guests WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))"
                );

                ps.setString(1, guestEmail);

                // Execute an SQL query that reads data from the database.
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    int eventId = rs.getInt("event_id");
                    // Show the selected window to the user.
                    new GuestInvitationFrame(link, eventId).setVisible(true);
                    // Close the current window.
                    dispose();
                } else {
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this,
                            "Guest not found!\nEmail used: " + guestEmail);
                }

                // Close this resource after finishing to avoid connection problems.
                conn.close();

            } catch (Exception ex) {
                ex.printStackTrace();
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                        "Database Error!\nCheck if guests table has email column.");
            }
        });

        // This action runs when the user clicks this button.
        logout.addActionListener(e -> {
            // Show the selected window to the user.
            new RoleSelectionFrame().setVisible(true);
            // Close the current window.
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
