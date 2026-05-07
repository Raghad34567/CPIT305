package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    DashboardFrame dashboard;

    public ReportsFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        setTitle("Wedding Reports & Statistics");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        JLabel title = new JLabel("Wedding Reports Overview", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        statsPanel.add(createStatCard("Total Guests", "250"));
        statsPanel.add(createStatCard("Accepted",     "180"));
        statsPanel.add(createStatCard("Declined",     "40"));
        statsPanel.add(createStatCard("Checked In",   "120"));

        JButton back = new JButton("Back");
        UITheme.styleButton(back);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(back);

        main.add(title, BorderLayout.NORTH);
        main.add(statsPanel, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);
        add(main);

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }

    private JPanel createStatCard(String label, String value) {
        JPanel card = UITheme.createCard(200, 150);
        JLabel t = new JLabel(label, SwingConstants.CENTER);
        t.setFont(new Font("Serif", Font.BOLD, 18));
        t.setForeground(UITheme.TEXT);
        JLabel v = new JLabel(value, SwingConstants.CENTER);
        v.setFont(new Font("Serif", Font.BOLD, 28));
        v.setForeground(UITheme.PRIMARY);
        card.add(t);
        card.add(v);
        return card;
    }
}
