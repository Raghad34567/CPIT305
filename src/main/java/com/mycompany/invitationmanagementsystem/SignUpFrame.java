package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/*
 * This frame allows organizers to create a new account.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class SignUpFrame extends JFrame {

    // Constructor: builds the main window and prepares all GUI components.
    public SignUpFrame() {
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Create Organizer Account");
        // Set frame size
        // Set the size of the window.
        setSize(720, 640);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        main.add(UITheme.createHeader("Create New Account"), BorderLayout.NORTH);

        // Create a panel to organize the components on the screen.
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel card = UITheme.createCard(480, 380);
        card.setLayout(new GridLayout(5, 1, 14, 14));

        JTextField     fullName  = UITheme.createField("Full Name");
        JTextField     username  = UITheme.createField("Username");
        JTextField     email     = UITheme.createField("Email");
        JPasswordField password  = UITheme.createPassField("Password");
        JPasswordField password2 = UITheme.createPassField("Confirm Password");

        card.add(fullName);
        card.add(username);
        card.add(email);
        card.add(password);
        card.add(password2);

        center.add(card);
        main.add(center, BorderLayout.CENTER);

        // Create a button that the user can click.
        JButton signUp = new JButton("Sign Up");
        // Create a button that the user can click.
        JButton back   = new JButton("Back to Login");
        main.add(UITheme.createButtonBar(signUp, back), BorderLayout.SOUTH);
        add(main);

        // Sign Up logic
        // This action runs when the user clicks this button.
        signUp.addActionListener(e -> {
            String fn  = fullName.getText().trim();
            String usr = username.getText().trim();
            String em  = email.getText().trim();
            String pw  = new String(password.getPassword());
            String pw2 = new String(password2.getPassword());

            // ── Validation ────────────────────────────────────────────────
            if (fn.isEmpty() || usr.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Username: 6–20 characters
            if (usr.length() < 6 || usr.length() > 20) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Username must be between 6 and 20 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Email: must match a valid email pattern
            if (!em.matches("^[\\w.+\\-]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,}$")) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address\n(e.g. example@domain.com)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Password: 6–20 characters
            if (pw.length() < 6 || pw.length() > 20) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Password must be between 6 and 20 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pw.equals(pw2)) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.connect();

                // username
                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM organizers WHERE username = ?");
                check.setString(1, usr);
                // Execute an SQL query that reads data from the database.
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this,
                        "Username already taken, choose another",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO organizers(full_name, username, password, email) " +
                    "VALUES (?, ?, ?, ?)");
                ps.setString(1, fn);
                ps.setString(2, usr);
                ps.setString(3, pw);
                ps.setString(4, em);
                // Execute an SQL command that changes data or creates tables.
                ps.executeUpdate();

                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                // Show the selected window to the user.
                new LoginFrame().setVisible(true);
                // Close the current window.
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            new LoginFrame().setVisible(true);
            // Close the current window.
            dispose();
        });
    }
}
