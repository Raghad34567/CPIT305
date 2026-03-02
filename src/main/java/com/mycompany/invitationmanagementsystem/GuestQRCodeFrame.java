/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class GuestQRCodeFrame extends JFrame {

    public GuestQRCodeFrame() {

        setTitle("Your Entry Pass");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel bg = new JPanel(new BorderLayout());
        bg.setBackground(UITheme.BACKGROUND);

        // ===== Title =====
        JLabel title = new JLabel("🎟 Your Digital Entry Pass", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28)); // replaced UITheme.TITLE_FONT
        title.setForeground(UITheme.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

        // ===== QR Card =====
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE); // or UITheme.CARD_BG if you have it
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.PRIMARY, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BorderLayout(10, 10));

        JLabel qrPlaceholder = new JLabel("QR CODE", SwingConstants.CENTER);
        qrPlaceholder.setFont(new Font("SansSerif", Font.BOLD, 26));
        qrPlaceholder.setForeground(UITheme.PRIMARY);
        qrPlaceholder.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 3)); // gold color
        qrPlaceholder.setPreferredSize(new Dimension(250, 250));

        JLabel instruction = new JLabel(
                "Please present this QR at the entrance",
                SwingConstants.CENTER
        );
        instruction.setFont(new Font("SansSerif", Font.PLAIN, 16)); // replaced UITheme.NORMAL_FONT
        instruction.setForeground(UITheme.TEXT);

        card.add(qrPlaceholder, BorderLayout.CENTER);
        card.add(instruction, BorderLayout.SOUTH);

        bg.add(title, BorderLayout.NORTH);
        bg.add(card, BorderLayout.CENTER);

        add(bg);
    }
}