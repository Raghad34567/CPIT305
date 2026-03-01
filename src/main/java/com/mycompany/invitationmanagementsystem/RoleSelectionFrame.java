/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionFrame extends JFrame {

    private JButton organizerButton;
    private JButton guestButton;

    public RoleSelectionFrame() {
        setTitle("Role Selection");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel rolePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        rolePanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel chooseLabel = new JLabel("Select Your Role", SwingConstants.CENTER);
        chooseLabel.setFont(new Font("Arial", Font.BOLD, 20));

        organizerButton = new JButton("Organizer");
        guestButton = new JButton("Guest");

        rolePanel.add(chooseLabel);
        rolePanel.add(organizerButton);
        rolePanel.add(guestButton);

        add(rolePanel);

        // Organizer button → placeholder
        organizerButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Organizer login will be implemented in Phase 2.",
                        "Coming Soon",
                        JOptionPane.INFORMATION_MESSAGE)
        );

        // Guest button → placeholder
        guestButton.addActionListener(e ->
                JOptionPane.showMessageDialog(this,
                        "Guests cannot login directly.\nPlease use the invitation link sent to you.",
                        "Access Denied",
                        JOptionPane.WARNING_MESSAGE)
        );
    }
}