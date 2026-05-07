package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class RoleSelectionFrame extends JFrame {

    public RoleSelectionFrame() {
        setTitle("Welcome");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(450, 300);

        JLabel title = new JLabel("Select Your Role", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);

        JButton organizer = new JButton("Organizer");
        JButton guest     = new JButton("Guest");
        UITheme.styleButton(organizer);
        UITheme.styleButton(guest);

        card.add(title);
        card.add(organizer);
        card.add(guest);

        main.add(card);
        add(main);

        organizer.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
        guest.addActionListener(e -> {
            new InvitationLinkFrame().setVisible(true);
            dispose();
        });
    }
}
