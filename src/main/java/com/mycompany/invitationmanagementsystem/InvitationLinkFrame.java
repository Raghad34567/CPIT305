/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class InvitationLinkFrame extends JFrame {

    public InvitationLinkFrame() {

        setTitle("Enter Invitation Link");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        // ===== Title =====
        JLabel title = new JLabel("Enter Your Invitation Link", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        // ===== Center Card =====
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.PRIMARY, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BorderLayout(20, 20));

        // Input field
        JTextField linkField = new JTextField();
        linkField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        linkField.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY, 2));
        linkField.setPreferredSize(new Dimension(400, 50));

        // Open Invitation button
        JButton openButton = new JButton("Open Invitation");
        UITheme.styleButton(openButton);
        openButton.setPreferredSize(new Dimension(350, 50));

        // Panel to center the button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(openButton);

        // Add input and button to card
        card.add(linkField, BorderLayout.NORTH);
        card.add(buttonPanel, BorderLayout.CENTER);

        // ===== Bottom Instruction =====
        JLabel instruction = new JLabel(
                "Paste the link sent to you via SMS or Email",
                SwingConstants.CENTER
        );
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 16));
        instruction.setForeground(UITheme.TEXT);
        instruction.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Add components to main panel
        main.add(title, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        main.add(instruction, BorderLayout.SOUTH);

        add(main);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InvitationLinkFrame().setVisible(true);
        });
    }
}