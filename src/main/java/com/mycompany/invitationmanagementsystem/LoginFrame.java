package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    public LoginFrame(){

        setTitle("Organizer Login");
        setSize(700,500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(UITheme.BACKGROUND);

        JPanel card = UITheme.createCard(450,350);

        JLabel title = new JLabel("Organizer Login", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        JTextField username = new JTextField();
        username.setBorder(BorderFactory.createTitledBorder("Username"));

        JPasswordField password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JButton login = new JButton("Login");
        UITheme.styleButton(login);

        JButton back = new JButton("Back");
        UITheme.styleButton(back);

        card.add(title);
        card.add(username);
        card.add(password);
        card.add(login);
        card.add(back);

        main.add(card);
        add(main);
    }
}