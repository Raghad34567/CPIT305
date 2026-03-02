/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class ManageGuestsFrame extends JFrame {

    public ManageGuestsFrame(){

        setTitle("Manage Guests");
        setSize(900,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        JLabel title = new JLabel("Wedding Guest List", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));

        JTable table = new JTable(
                new Object[][]{},
                new String[]{"Name","Phone",}
        );

        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        JPanel buttons = new JPanel();
        buttons.setBackground(UITheme.BACKGROUND);

        JButton add = new JButton("Add Guest");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Remove");

        UITheme.styleButton(add);
        UITheme.styleButton(edit);
        UITheme.styleButton(delete);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);

        main.add(title, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);
    }
}