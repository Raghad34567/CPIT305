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

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard - Invitation Management System");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100));

        JButton createEventBtn = new JButton("Create Event");
        JButton manageGuestsBtn = new JButton("Manage Guests");
        JButton sendInvBtn = new JButton("Send Invitations");
        JButton viewResponsesBtn = new JButton("View Responses");
        JButton reportsBtn = new JButton("Reports");
        JButton logoutBtn = new JButton("Logout");

        panel.add(createEventBtn);
        panel.add(manageGuestsBtn);
        panel.add(sendInvBtn);
        panel.add(viewResponsesBtn);
        panel.add(reportsBtn);
        panel.add(logoutBtn);

        add(panel);
    }
}
