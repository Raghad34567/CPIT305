/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class CreateEventFrame extends JFrame {

    public CreateEventFrame(){

        setTitle("Create Wedding Event");
        setSize(750,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(500,400);

        JLabel title = new JLabel("Create New Wedding Event", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Event Name"));

        JTextField date = new JTextField();
        date.setBorder(BorderFactory.createTitledBorder("Date"));

        JTextField location = new JTextField();
        location.setBorder(BorderFactory.createTitledBorder("Location"));

        JTextField capacity = new JTextField();
        capacity.setBorder(BorderFactory.createTitledBorder("Capacity"));

        JButton save = new JButton("Save Event");
        UITheme.styleButton(save);

        card.add(title);
        card.add(name);
        card.add(date);
        card.add(location);
        card.add(capacity);
        card.add(save);

        main.add(card);
        add(main);
    }
}