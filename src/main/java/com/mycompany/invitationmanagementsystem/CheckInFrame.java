package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class CheckInFrame extends JFrame {

    DashboardFrame dashboard;

    public CheckInFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Wedding Guest Check-In");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(500, 350);
        card.setLayout(new BorderLayout(20, 20));

        JLabel title = new JLabel("Guest Check-In System", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.PRIMARY);

        JTextField qrField = new JTextField();
        qrField.setBorder(BorderFactory.createTitledBorder("Scan or Enter QR Code"));
        qrField.setFont(new Font("Serif", Font.PLAIN, 18));

        JButton checkInBtn = new JButton("Confirm Check-In");
        UITheme.styleButton(checkInBtn);

        JButton back = new JButton("Back");
        UITheme.styleButton(back);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.CARD);
        bottomPanel.add(checkInBtn);
        bottomPanel.add(back);

        card.add(title, BorderLayout.NORTH);
        card.add(qrField, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        checkInBtn.addActionListener(e -> {
            String code = qrField.getText();

            if (code.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter or scan a QR code",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Guest Checked In Successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                qrField.setText(""); // clear field after check-in
            }
        });

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }
}
