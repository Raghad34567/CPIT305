package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionFrame extends JFrame {

    public RoleSelectionFrame() {
        setTitle("Welcome");
        setSize(680, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(420, 300);

        JLabel title = new JLabel("Welcome", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);

        JLabel sub = new JLabel("Please select your role to continue", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.ITALIC, 14));
        sub.setForeground(UITheme.TEXT_LIGHT);

        JLabel divider = new JLabel("✦  ───────────────────  ✦", SwingConstants.CENTER);
        divider.setFont(new Font("Serif", Font.PLAIN, 13));
        divider.setForeground(new Color(185, 125, 148));

        JButton organizer = new JButton("Organizer");
        JButton guest     = new JButton("Guest");
        UITheme.styleButton(organizer);
        UITheme.styleButton(guest);

        card.add(title);
        card.add(sub);
        card.add(divider);
        card.add(organizer);
        card.add(guest);

        main.add(card);
        add(main);

        organizer.addActionListener(e -> {
            CreateEventDatabase.setup(); 
            new LoginFrame().setVisible(true);
            dispose();
        });

        guest.addActionListener(e -> {
            new InvitationLinkFrame().setVisible(true);
            dispose();
        });
    }
}