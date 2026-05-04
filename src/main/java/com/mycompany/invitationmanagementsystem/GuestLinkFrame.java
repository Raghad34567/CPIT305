package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GuestLinkFrame extends JFrame {

    DashboardFrame dashboard;

    public GuestLinkFrame(DashboardFrame dashboard) {

        this.dashboard = dashboard;

        setTitle("Generate Link");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(450, 300);
        card.setLayout(new GridLayout(4, 1, 10, 10));

        JTextField name = new JTextField();
        name.setBorder(BorderFactory.createTitledBorder("Guest Name"));
        name.setFont(new Font("Serif", Font.PLAIN, 18));

        JTextField link = new JTextField();
        link.setEditable(false);
        link.setFont(new Font("Serif", Font.PLAIN, 18));

        JButton generate = new JButton("Generate");
        JButton back = new JButton("Back");

        UITheme.styleButton(generate);
        UITheme.styleButton(back);

        card.add(name);
        card.add(link);
        card.add(generate);
        card.add(back);

        main.add(card);
        add(main);

        // ================= GENERATE + NETWORK (GET) =================
        generate.addActionListener(e -> {

            String guestName = name.getText().trim();

            if (guestName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter guest name");
                return;
            }

            // show link immediately (UI)
            String inviteLink = "www.invite.com/" + guestName;
            link.setText(inviteLink);

            // run networking in background
            new Thread(() -> {
                try {

                    String encodedName = URLEncoder.encode(guestName, StandardCharsets.UTF_8);

                    String urlStr = "https://postman-echo.com/get?guest=" + encodedName;

                    URL url = new URL(urlStr);
                    URLConnection conn = url.openConnection();

                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                    Scanner in = new Scanner(conn.getInputStream());

                    while (in.hasNextLine()) {
                        System.out.println(in.nextLine()); // print server response
                    }

                    // show success 
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Invitation Generated & Sent!")
                    );

                } catch (Exception ex) {
                    ex.printStackTrace();

                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Network Error!")
                    );
                }
            }).start();
        });

        // ================= BACK =================
        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }
}
