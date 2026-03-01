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

public class CreateEventFrame extends JFrame {

    public CreateEventFrame() {
        setTitle("Create Event");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Event Title:");
        JTextField titleField = new JTextField();

        JLabel dateLabel = new JLabel("Date:");
        JTextField dateField = new JTextField();

        JLabel locationLabel = new JLabel("Location:");
        JTextField locationField = new JTextField();

        JLabel capacityLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField();

        JButton saveBtn = new JButton("Save");
        JButton backBtn = new JButton("Back");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(locationLabel);
        panel.add(locationField);
        panel.add(capacityLabel);
        panel.add(capacityField);
        panel.add(saveBtn);
        panel.add(backBtn);

        add(panel);
    }
}