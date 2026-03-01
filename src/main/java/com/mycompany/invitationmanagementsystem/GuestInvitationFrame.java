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

public class GuestInvitationFrame extends JFrame {

    public GuestInvitationFrame() {
        setTitle("Event Invitation");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("You're Invited!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JTextArea invitationDetails = new JTextArea();
        invitationDetails.setText(
                "Event Name: Graduation Party\n\n" +
                "Date: 20 May 2026\n" +
                "Location: Jeddah Hall\n\n" +
                "We are delighted to invite you to our special event!"
        );
        invitationDetails.setEditable(false);
        invitationDetails.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton rsvpButton = new JButton("RSVP");

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(invitationDetails, BorderLayout.CENTER);
        panel.add(rsvpButton, BorderLayout.SOUTH);

        add(panel);
    }

   
}