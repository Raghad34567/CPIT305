/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class GuestLinkFrame extends JFrame {

    public GuestLinkFrame(){

        setTitle("Generate Guest Invitation Link");
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(550,350);
        card.setLayout(new BorderLayout(20,20));

        JLabel title = new JLabel("Generate Personalized Invitation Link", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);

        JTextField guestName = new JTextField();
        guestName.setBorder(BorderFactory.createTitledBorder("Guest Name"));

        JTextField linkField = new JTextField("Generated link will appear here...");
        linkField.setEditable(false);

        JButton generateBtn = new JButton("Generate Link");
        UITheme.styleButton(generateBtn);

        JPanel centerPanel = new JPanel(new GridLayout(2,1,15,15));
        centerPanel.setBackground(UITheme.CARD);
        centerPanel.add(guestName);
        centerPanel.add(linkField);

        card.add(title, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(generateBtn, BorderLayout.SOUTH);

        main.add(card);
        add(main);
    }
}