package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResponsesFrame extends JFrame {

    DashboardFrame dashboard;

    public ResponsesFrame(DashboardFrame dashboard) {

        this.dashboard = dashboard;

        setTitle("Guest Responses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        JLabel title = new JLabel("RSVP Responses", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // ===== TABLE MODEL =====
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Guest Name", "Response", "Guests Count"}, 0
        );

        JTable table = new JTable(model);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(table);

        // ===== BACK BUTTON =====
        JButton back = new JButton("Back");
        UITheme.styleButton(back);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(UITheme.BACKGROUND);
        bottomPanel.add(back);

        main.add(title, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(bottomPanel, BorderLayout.SOUTH);

        add(main);

        // ================= LOAD RESPONSES =================
        new Thread(() -> {
            try {
                Connection conn = DBConnection.connect();

                PreparedStatement ps = conn.prepareStatement(
                        "SELECT name, response, guest_count FROM guests WHERE response IS NOT NULL"
                );

                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    String name = rs.getString("name");
                    String response = rs.getString("response");
                    int count = rs.getInt("guest_count");

                    SwingUtilities.invokeLater(()
                            -> model.addRow(new Object[]{name, response, count})
                    );
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

        // ===== BACK =====
        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }
}
