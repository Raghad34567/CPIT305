package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/*
 * This frame allows organizers to log into the system.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class LoginFrame extends JFrame {

    // Constructor: builds the main window and prepares all GUI components.
    public LoginFrame() {
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Organizer Login");
        // Set frame size
        // Set the size of the window.
        setSize(680, 560);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(430, 420);

        // Create a label to display text for the user.
        JLabel title = new JLabel("Organizer Login", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        // Create a label to display text for the user.
        JLabel div = new JLabel("✦  ───────────────────  ✦", SwingConstants.CENTER);
        div.setFont(new Font("Serif", Font.PLAIN, 13));
        div.setForeground(new Color(185, 125, 148));

        JTextField     username = UITheme.createField("Username");
        JPasswordField password = UITheme.createPassField("Password");

        // Create a button that the user can click.
        JButton login = new JButton("Login");
        // Create a button that the user can click.
        JButton back  = new JButton("Back");
        UITheme.styleButton(login);
        UITheme.styleButton(back);

        // Create a panel to organize the components on the screen.
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btns.setOpaque(false);
        btns.add(login);
        btns.add(back);

        // Create a label to display text for the user.
        JLabel signUpLink = new JLabel(
            "Don't have an account?  Sign Up", SwingConstants.CENTER);
        signUpLink.setFont(new Font("Serif", Font.ITALIC, 13));
        signUpLink.setForeground(UITheme.PRIMARY);
        signUpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(title);
        card.add(div);
        card.add(username);
        card.add(password);
        card.add(btns);
        card.add(signUpLink);

        main.add(card);
        add(main);

        
        // This action runs when the user clicks this button.
        login.addActionListener(e -> {
            String usr = username.getText().trim();
            String pw  = new String(password.getPassword());

            if (usr.isEmpty() || pw.isEmpty()) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Please enter username and password",
                    "Error", JOptionPane.ERROR_MESSAGE);
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

            // Password: 6–20 characters
            if (pw.length() < 6 || pw.length() > 20) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Password must be between 6 and 20 characters",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loggedIn = false;
            try {
                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.getInstance().getConnection();
                if (conn != null) {
                    // Prepare the SQL statement to send it safely to the database.
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT id FROM organizers WHERE username=? AND password=?");
                    ps.setString(1, usr);
                    ps.setString(2, pw);
                    // Execute an SQL query that reads data from the database.
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) loggedIn = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (loggedIn) {
                // Show the selected window to the user.
                new DashboardFrame().setVisible(true);
                // Close the current window.
                dispose();
            } else {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            new RoleSelectionFrame().setVisible(true);
            // Close the current window.
            dispose();
        });

        // Sign Up link
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            // This method handles the mouseClicked part of the class logic.
            public void mouseClicked(java.awt.event.MouseEvent e) {
                // Show the selected window to the user.
                new SignUpFrame().setVisible(true);
                // Close the current window.
                dispose();
            }
            // This method handles the mouseEntered part of the class logic.
            public void mouseEntered(java.awt.event.MouseEvent e) {
                signUpLink.setText("<html><u>Don't have an account?  Sign Up</u></html>");
            }
            // This method handles the mouseExited part of the class logic.
            public void mouseExited(java.awt.event.MouseEvent e) {
                signUpLink.setText("Don't have an account?  Sign Up");
            }
        });
    }
}
