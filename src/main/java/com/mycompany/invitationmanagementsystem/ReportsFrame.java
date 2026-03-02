/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class ReportsFrame extends JFrame {

    public ReportsFrame(){

        setTitle("Wedding Reports & Statistics");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        JLabel title = new JLabel("Wedding Reports Overview", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        JPanel statsPanel = new JPanel(new GridLayout(2,2,30,30));
        statsPanel.setBackground(UITheme.BACKGROUND);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(40,80,40,80));

        statsPanel.add(createStatCard("Total Guests", "250"));
        statsPanel.add(createStatCard("Accepted", "180"));
        statsPanel.add(createStatCard("Declined", "40"));
        statsPanel.add(createStatCard("Checked In", "120"));

        main.add(title, BorderLayout.NORTH);
        main.add(statsPanel, BorderLayout.CENTER);

        add(main);
    }

    private JPanel createStatCard(String title, String value){
        JPanel card = UITheme.createCard(200,150);

        JLabel t = new JLabel(title, SwingConstants.CENTER);
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