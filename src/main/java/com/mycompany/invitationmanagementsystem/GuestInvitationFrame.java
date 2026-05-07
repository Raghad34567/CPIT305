package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class GuestInvitationFrame extends JFrame {

    String guestName;
    int    eventId;

    public GuestInvitationFrame(String guestName, int eventId) {
        this.guestName = guestName;
        this.eventId   = eventId;

        setTitle("Wedding Invitation");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(600, 450);
        card.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("You Are Cordially Invited", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UITheme.PRIMARY);

        JTextArea details = new JTextArea(
            "Together with their families\n\n"
            + "Bride & Groom\n\n"
            + "request the pleasure of your company\n"
            + "at their wedding celebration\n\n"
            + "Date: 20 May 2026\n"
            + "Location: Grand Royal Hall\n\n"
            + "Your presence will make our day truly special."
        );
        details.setFont(new Font("Serif", Font.PLAIN, 20));
        details.setEditable(false);
        details.setOpaque(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setForeground(UITheme.TEXT);

        JButton rsvp = new JButton("RSVP");
        JButton back = new JButton("Back");
        UITheme.styleButton(rsvp);
        UITheme.styleButton(back);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(rsvp);
        buttonPanel.add(back);

        card.add(title, BorderLayout.NORTH);
        card.add(details, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        rsvp.addActionListener(e -> {
            new RSVPFrame(this, guestName, eventId).setVisible(true);
            setVisible(false);
        });

        back.addActionListener(e -> {
            new InvitationLinkFrame().setVisible(true);
            dispose();
        });
    }
}
