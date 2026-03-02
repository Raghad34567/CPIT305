/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class CheckInFrame extends JFrame {

    public CheckInFrame(){

        setTitle("Wedding Guest Check-In");
        setSize(800,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(500,350);
        card.setLayout(new BorderLayout(20,20));

        JLabel title = new JLabel("Guest Check-In System", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.PRIMARY);

        JTextField qrField = new JTextField();
        qrField.setBorder(BorderFactory.createTitledBorder("Scan or Enter QR Code"));

        JButton checkInBtn = new JButton("Confirm Check-In");
        UITheme.styleButton(checkInBtn);

        card.add(title, BorderLayout.NORTH);
        card.add(qrField, BorderLayout.CENTER);
        card.add(checkInBtn, BorderLayout.SOUTH);

        main.add(card);
        add(main);
    }
}
