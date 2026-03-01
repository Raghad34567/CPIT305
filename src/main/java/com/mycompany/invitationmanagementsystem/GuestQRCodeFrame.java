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

public class GuestQRCodeFrame extends JFrame {

    public GuestQRCodeFrame() {
        setTitle("Your QR Code");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Show this QR Code at the Entrance", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel qrPlaceholder = new JLabel("QR CODE HERE", SwingConstants.CENTER);
        qrPlaceholder.setFont(new Font("Arial", Font.BOLD, 20));
        qrPlaceholder.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JButton closeButton = new JButton("Close");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(qrPlaceholder, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        add(panel);
    }

   
}
