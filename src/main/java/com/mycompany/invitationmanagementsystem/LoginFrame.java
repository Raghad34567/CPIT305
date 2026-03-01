package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField userField;
    private JPasswordField passField;
    private JButton loginButton;
    private JButton backButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField();

        loginButton = new JButton("Login");
        backButton = new JButton("Back");

        loginPanel.add(userLabel);
        loginPanel.add(userField);
        loginPanel.add(passLabel);
        loginPanel.add(passField);
        loginPanel.add(loginButton);
        loginPanel.add(backButton);

        add(loginPanel);

        // Login button → placeholder
        loginButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Login successful! (Dashboard to be implemented in Phase 2)",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        // Back button → placeholder
        backButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Back button functionality will be added in Phase 2.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE)
        );
    }
}