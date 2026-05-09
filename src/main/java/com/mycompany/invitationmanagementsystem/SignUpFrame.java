package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignUpFrame extends JFrame {

    public SignUpFrame() {
        setTitle("Create Organizer Account");
        setSize(720, 640);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        main.add(UITheme.createHeader("Create New Account"), BorderLayout.NORTH);

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

        JButton signUp = new JButton("Sign Up");
        JButton back   = new JButton("Back to Login");
        main.add(UITheme.createButtonBar(signUp, back), BorderLayout.SOUTH);
        add(main);

        // Sign Up logic
        signUp.addActionListener(e -> {
            String fn  = fullName.getText().trim();
            String usr = username.getText().trim();
            String em  = email.getText().trim();
            String pw  = new String(password.getPassword());
            String pw2 = new String(password2.getPassword());

            // ── Validation ────────────────────────────────────────────────
            if (fn.isEmpty() || usr.isEmpty() || em.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Username: 6–20 characters
            if (usr.length() < 6 || usr.length() > 20) {
                JOptionPane.showMessageDialog(this,
                    "Username must be between 6 and 20 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Email: must match a valid email pattern
            if (!em.matches("^[\\w.+\\-]+@[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a valid email address\n(e.g. example@domain.com)",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Password: 6–20 characters
            if (pw.length() < 6 || pw.length() > 20) {
                JOptionPane.showMessageDialog(this,
                    "Password must be between 6 and 20 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!pw.equals(pw2)) {
                JOptionPane.showMessageDialog(this,
                    "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Connection conn = DBConnection.connect();

                // username
                PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM organizers WHERE username = ?");
                check.setString(1, usr);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this,
                        "Username already taken, choose another",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO organizers(full_name, username, password, email) " +
                    "VALUES (?, ?, ?, ?)");
                ps.setString(1, fn);
                ps.setString(2, usr);
                ps.setString(3, pw);
                ps.setString(4, em);
                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,
                    "Account created successfully!\nYou can now login.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

                new LoginFrame().setVisible(true);
                dispose();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}