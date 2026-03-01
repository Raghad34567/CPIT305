/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

/**
 *
 * @author lama
 */
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {

        setTitle("Invitation Management System - Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField();

        JLabel roleLabel = new JLabel("Login As:");

        JRadioButton organizerRadio = new JRadioButton("Organizer");
        JRadioButton guestRadio = new JRadioButton("Guest");

        ButtonGroup group = new ButtonGroup();
        group.add(organizerRadio);
        group.add(guestRadio);

        JButton loginButton = new JButton("Login");
        JButton exitButton = new JButton("Exit");

        // Action for Login Button
        loginButton.addActionListener(e -> {

            if (organizerRadio.isSelected()) {

                new DashboardFrame().setVisible(true);
                dispose();

            } else if (guestRadio.isSelected()) {

                JOptionPane.showMessageDialog(this,
                        "Guests cannot login directly.\nPlease use the invitation link sent to you.",
                        "Access Denied",
                        JOptionPane.WARNING_MESSAGE);

            } else {

                JOptionPane.showMessageDialog(this,
                        "Please select your role.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Exit button
        exitButton.addActionListener(e -> System.exit(0));

        panel.add(titleLabel);
        panel.add(new JLabel(""));

        panel.add(userLabel);
        panel.add(userField);

        panel.add(passLabel);
        panel.add(passField);

        panel.add(roleLabel);
        panel.add(new JLabel(""));

        panel.add(organizerRadio);
        panel.add(guestRadio);

        panel.add(loginButton);
        panel.add(exitButton);

        add(panel);
    }

}