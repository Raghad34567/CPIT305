package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

/*
 * This frame lets the user choose between organizer and guest roles.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class RoleSelectionFrame extends JFrame {

    // Constructor: builds the main window and prepares all GUI components.
    public RoleSelectionFrame() {
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Welcome");
        // Set frame size
        // Set the size of the window.
        setSize(680, 480);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(420, 300);

        // Create a label to display text for the user.
        JLabel title = new JLabel("Welcome", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);

        // Create a label to display text for the user.
        JLabel sub = new JLabel("Please select your role to continue", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.ITALIC, 14));
        sub.setForeground(UITheme.TEXT_LIGHT);

        // Create a label to display text for the user.
        JLabel divider = new JLabel("✦  ───────────────────  ✦", SwingConstants.CENTER);
        divider.setFont(new Font("Serif", Font.PLAIN, 13));
        divider.setForeground(new Color(185, 125, 148));

        // Create a button that the user can click.
        JButton organizer = new JButton("Organizer");
        // Create a button that the user can click.
        JButton guest     = new JButton("Guest");
        UITheme.styleButton(organizer);
        UITheme.styleButton(guest);

        card.add(title);
        card.add(sub);
        card.add(divider);
        card.add(organizer);
        card.add(guest);

        main.add(card);
        add(main);

        // This action runs when the user clicks this button.
        organizer.addActionListener(e -> {
            CreateEventDatabase.setup(); 
            // Show the selected window to the user.
            new LoginFrame().setVisible(true);
            // Close the current window.
            dispose();
        });

        // This action runs when the user clicks this button.
        guest.addActionListener(e -> {
            // Show the selected window to the user.
            new InvitationLinkFrame().setVisible(true);
            // Close the current window.
            dispose();
        });
    }
}
