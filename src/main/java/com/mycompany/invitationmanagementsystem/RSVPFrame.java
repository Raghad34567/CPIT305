/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class RSVPFrame extends JFrame {

    public RSVPFrame(){

        setTitle("RSVP");
        setSize(650,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(450,300);

        JLabel title = new JLabel("Kindly Confirm Your Attendance", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(UITheme.TEXT);

        JComboBox<String> response = new JComboBox<>(new String[]{"Accept with Pleasure","Regretfully Decline"});

        JTextField guests = new JTextField();
        guests.setBorder(BorderFactory.createTitledBorder("Number of Guests"));

        JButton submit = new JButton("Submit Response");
        UITheme.styleButton(submit);

        card.add(title);
        card.add(response);
        card.add(guests);
        card.add(submit);

        main.add(card);
        add(main);
    }
}