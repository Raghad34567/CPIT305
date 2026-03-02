/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class GuestInvitationFrame extends JFrame {

    public GuestInvitationFrame(){

        setTitle("Wedding Invitation");
        setSize(800,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(600,450);
        card.setLayout(new BorderLayout(20,20));

        // ===== TITLE =====
        JLabel title = new JLabel("You Are Cordially Invited", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UITheme.PRIMARY);

        // ===== INVITATION TEXT =====
        JTextArea details = new JTextArea(
                "Together with their families\n\n"
                        + "Bride & Groom\n\n"
                        + "request the pleasure of your company\n"
                        + "at their wedding celebration\n\n"
                        + "Date: 20 May 2026\n"
                        + "Location: Grand Royal Hall\n\n"
                        + "Your presence will make our day truly special."
        );

        details.setFont(new Font("Serif", Font.PLAIN, 20));
        details.setEditable(false);
        details.setOpaque(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setForeground(UITheme.TEXT);

        // ===== SMALL RSVP BUTTON =====
        JButton rsvp = new JButton("RSVP");
        rsvp.setPreferredSize(new Dimension(120,40)); // smaller size
        UITheme.styleButton(rsvp);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(UITheme.CARD);
        buttonPanel.add(rsvp);

        card.add(title, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);
    }
}