package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GuestLinkFrame extends JFrame {

    DashboardFrame dashboard;

    public GuestLinkFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Generate Link");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(500, 380);
        card.setLayout(new GridLayout(5, 1, 10, 10));

        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Guest Name"));
        name.setFont(new Font("Serif", Font.PLAIN, 18));

        JTextField email = new JTextField();
        email.setBorder(BorderFactory.createTitledBorder("Guest Email"));
        email.setFont(new Font("Serif", Font.PLAIN, 18));

        JTextField link = new JTextField();
        link.setEditable(false);
        link.setBorder(BorderFactory.createTitledBorder("Generated Link"));
        link.setFont(new Font("Serif", Font.PLAIN, 18));

        JButton generate = new JButton("Generate & Send Email");
        JButton back = new JButton("Back");

        UITheme.styleButton(generate);
        UITheme.styleButton(back);

        card.add(name);
        card.add(email);
        card.add(link);
        card.add(generate);
        card.add(back);

        main.add(card);
        add(main);

        generate.addActionListener(e -> {
            String guestName = name.getText().trim();
            String guestEmail = email.getText().trim();

            if (guestName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter guest name");
                return;
            }

            if (guestEmail.isEmpty() || !guestEmail.contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter valid guest email");
                return;
            }

            String encodedName = URLEncoder.encode(guestName, StandardCharsets.UTF_8);
            String inviteLink = "www.invite.com/" + encodedName;
            link.setText(inviteLink);

            generate.setEnabled(false);

            new Thread(() -> {
                try {
                    EmailClient.sendInvitationRequest(guestEmail, guestName, inviteLink);

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Invitation email sent successfully!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this,
                                "Cannot connect to Email Server.\nPlease run EmailServer first.");
                    });

                } finally {
                    SwingUtilities.invokeLater(() -> generate.setEnabled(true));
                }
            }).start();
        });

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }
}