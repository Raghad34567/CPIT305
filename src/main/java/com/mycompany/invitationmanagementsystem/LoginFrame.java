package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Organizer Login");
        setSize(680, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(430, 420);

        JLabel title = new JLabel("Organizer Login", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        JLabel div = new JLabel("✦  ───────────────────  ✦", SwingConstants.CENTER);
        div.setFont(new Font("Serif", Font.PLAIN, 13));
        div.setForeground(new Color(185, 125, 148));

        JTextField     username = UITheme.createField("Username");
        JPasswordField password = UITheme.createPassField("Password");

        JButton login = new JButton("Login");
        JButton back  = new JButton("Back");
        UITheme.styleButton(login);
        UITheme.styleButton(back);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 0));
        btns.setOpaque(false);
        btns.add(login);
        btns.add(back);

        JLabel signUpLink = new JLabel(
            "Don't have an account?  Sign Up", SwingConstants.CENTER);
        signUpLink.setFont(new Font("Serif", Font.ITALIC, 13));
        signUpLink.setForeground(UITheme.PRIMARY);
        signUpLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        card.add(title);
        card.add(div);
        card.add(username);
        card.add(password);
        card.add(btns);
        card.add(signUpLink);

        main.add(card);
        add(main);

        // ── Login: يتحقق فقط عند الضغط ──────────────
        login.addActionListener(e -> {
            String usr = username.getText().trim();
            String pw  = new String(password.getPassword());

            if (usr.isEmpty() || pw.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter username and password",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean loggedIn = false;
            try {
                Connection conn = DBConnection.connect();
                if (conn != null) {
                    PreparedStatement ps = conn.prepareStatement(
                        "SELECT id FROM organizers WHERE username=? AND password=?");
                    ps.setString(1, usr);
                    ps.setString(2, pw);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) loggedIn = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (loggedIn) {
                new DashboardFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        back.addActionListener(e -> {
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });

        // ── Sign Up link ──────────────────────────────
        signUpLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new SignUpFrame().setVisible(true);
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                signUpLink.setText("<html><u>Don't have an account?  Sign Up</u></html>");
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                signUpLink.setText("Don't have an account?  Sign Up");
            }
        });
    }
}