/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class ResponsesFrame extends JFrame {

    public ResponsesFrame(){

        setTitle("Guest Responses");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        JLabel title = new JLabel("RSVP Responses", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        JTable table = new JTable(
                new Object[][]{},
                new String[]{"Guest Name","Response","Guests Count"}
        );

        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(table);

        main.add(title, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);

        add(main);
    }
}