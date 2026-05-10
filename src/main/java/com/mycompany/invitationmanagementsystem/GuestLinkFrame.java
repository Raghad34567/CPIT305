package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*
 * This frame generates and sends invitation links to guests.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class GuestLinkFrame extends JFrame {

    DashboardFrame dashboard;

    // Constructor: builds the main window and prepares all GUI components.
    public GuestLinkFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Generate Link");
        // Set frame size
        // Set the size of the window.
        setSize(750, 550);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(500, 380);
        card.setLayout(new GridLayout(5, 1, 10, 10));

        // Create a text field so the user can type data.
        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Guest Name"));
        name.setFont(new Font("Serif", Font.PLAIN, 18));

        // Create a text field so the user can type data.
        JTextField email = new JTextField();
        email.setBorder(BorderFactory.createTitledBorder("Guest Email"));
        email.setFont(new Font("Serif", Font.PLAIN, 18));

        // Create a text field so the user can type data.
        JTextField link = new JTextField();
        link.setEditable(false);
        link.setBorder(BorderFactory.createTitledBorder("Generated Link"));
        link.setFont(new Font("Serif", Font.PLAIN, 18));

        // Create a button that the user can click.
        JButton generate = new JButton("Generate & Send Email");
        // Create a button that the user can click.
        JButton back = new JButton("Back");

        UITheme.styleButton(generate);
        UITheme.styleButton(back);

        card.add(name);
        card.add(email);
        card.add(link);
        card.add(generate);
        card.add(back);

        main.add(card);
        add(main);

        // This action runs when the user clicks this button.
        generate.addActionListener(e -> {
            String guestName = name.getText().trim();
            String guestEmail = email.getText().trim();

            if (guestName.isEmpty()) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Enter guest name");
                return;
            }

            if (guestEmail.isEmpty() || !guestEmail.contains("@")) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Enter valid guest email");
                return;
            }

            String encodedName = URLEncoder.encode(guestName, StandardCharsets.UTF_8);
            String inviteLink = "www.invite.com/" + encodedName;
            link.setText(inviteLink);

            generate.setEnabled(false);

            // Use a thread so this work can run separately from the main screen.
            new Thread(() -> {
                try {
                    EmailClient.sendInvitationRequest(guestEmail, guestName, inviteLink);

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() -> {
                        // Show a message box to tell the user the result.
                        JOptionPane.showMessageDialog(this, "Invitation email sent successfully!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() -> {
                        // Show a message box to tell the user the result.
                        JOptionPane.showMessageDialog(this,
                                "Cannot connect to Email Server.\nPlease run EmailServer first.");
                    });

                } finally {
                    // Run the GUI code on the Swing event thread.
                    SwingUtilities.invokeLater(() -> generate.setEnabled(true));
                }
            }).start();
        });

        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            dashboard.setVisible(true);
            // Close the current window.
            dispose();
        });
    }
}
