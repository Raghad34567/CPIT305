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

public class RSVPFrame extends JFrame {

    public RSVPFrame() {
        setTitle("RSVP Response");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel responseLabel = new JLabel("Response:");
        String[] options = {"Accept", "Decline"};
        JComboBox<String> responseBox = new JComboBox<>(options);

        JLabel plusOneLabel = new JLabel("Number of Guests:");
        JTextField plusOneField = new JTextField();

        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");

        panel.add(responseLabel);
        panel.add(responseBox);
        panel.add(plusOneLabel);
        panel.add(plusOneField);
        panel.add(submitButton);
        panel.add(backButton);

        add(panel);
    }

   
}
