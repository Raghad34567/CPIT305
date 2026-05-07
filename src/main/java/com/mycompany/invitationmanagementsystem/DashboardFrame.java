package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {

        setTitle("Wedding Dashboard");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        // ===== Header =====
        JLabel header = new JLabel("Wedding Management Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 30));
        header.setForeground(UITheme.TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        // ===== Buttons Grid =====
        JPanel buttons = new JPanel(new GridLayout(2, 3, 40, 40));
        buttons.setBackground(UITheme.BACKGROUND);
        buttons.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JButton createEvent = new JButton("Create Event");
        JButton manageGuests = new JButton("Manage Guests");
        JButton sendInvitations = new JButton("Send Invitation");
        JButton viewResponses = new JButton("View Response");
        JButton reports = new JButton("Reports");
        JButton guestCheckIn = new JButton("Guest Check-In");

        UITheme.styleButton(createEvent);
        UITheme.styleButton(manageGuests);
        UITheme.styleButton(sendInvitations);
        UITheme.styleButton(viewResponses);
        UITheme.styleButton(reports);
        UITheme.styleButton(guestCheckIn);

        Dimension btnSize = new Dimension(200, 200);
        createEvent.setPreferredSize(btnSize);
        manageGuests.setPreferredSize(btnSize);
        sendInvitations.setPreferredSize(btnSize);
        viewResponses.setPreferredSize(btnSize);
        reports.setPreferredSize(btnSize);
        guestCheckIn.setPreferredSize(btnSize);

        buttons.add(createEvent);
        buttons.add(manageGuests);
        buttons.add(sendInvitations);
        buttons.add(viewResponses);
        buttons.add(reports);
        buttons.add(guestCheckIn);

        // ===== Logout button =====
        JButton logout = new JButton("Logout");
        UITheme.styleButton(logout);
        logout.setPreferredSize(new Dimension(120, 40));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.BACKGROUND);
        bottomPanel.add(logout);

        main.add(header, BorderLayout.NORTH);
        main.add(buttons, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);

        createEvent.addActionListener(e -> {
            new CreateEventFrame(this).setVisible(true);
            setVisible(false);
        });

        manageGuests.addActionListener(e -> {
            new ManageGuestsFrame(this).setVisible(true);
            setVisible(false);
        });

        sendInvitations.addActionListener(e -> {
            new GuestLinkFrame(this).setVisible(true);
            setVisible(false);
        });

        viewResponses.addActionListener(e -> {
            new ResponsesFrame(this).setVisible(true);
            setVisible(false);
        });

        reports.addActionListener(e -> {
            new ReportsFrame(this).setVisible(true);
            setVisible(false);
        });

        guestCheckIn.addActionListener(e -> {
            new CheckInFrame(this).setVisible(true);
            setVisible(false);
        });

        logout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully");
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });
    }
}
