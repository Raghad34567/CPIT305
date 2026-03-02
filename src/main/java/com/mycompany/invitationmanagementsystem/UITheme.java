/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;

public class UITheme {

    // Wedding Luxury Colors
    public static Color PRIMARY = new Color(128, 0, 32); // Gold
    public static Color BACKGROUND = new Color(250, 247, 240); // Cream
    public static Color CARD = Color.WHITE;
    public static Color TEXT = new Color(60, 50, 40);

    public static void styleButton(JButton button){

        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12,25,12,25));

        // Hover glow effect
        button.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt){
                button.setBackground(PRIMARY.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt){
                button.setBackground(PRIMARY);
            }
        });
    }

    public static JPanel createCard(int width, int height){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(width,height));
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY,2),
                BorderFactory.createEmptyBorder(30,30,30,30)
        ));
        panel.setLayout(new GridLayout(0,1,18,18));
        return panel;
    }
}