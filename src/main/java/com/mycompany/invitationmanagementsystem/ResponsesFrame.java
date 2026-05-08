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
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout(0, 0));
        main.add(UITheme.createHeader("RSVP Responses"), BorderLayout.NORTH);

        // ── Table ────────────────────────────────────
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Guest Name", "Response", "Guests Count"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        UITheme.styleTable(table);

        // Center-align "Response" and "Guests Count" columns
        javax.swing.table.DefaultTableCellRenderer centerRenderer =
            new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        JScrollPane scroll = UITheme.createStyledScroll(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 24, 4, 24),
            BorderFactory.createLineBorder(UITheme.BORDER, 1, true)));

        main.add(scroll, BorderLayout.CENTER);

        // ── Back button ───────────────────────────────
        JButton back = new JButton("Back");
        main.add(UITheme.createButtonBar(back), BorderLayout.SOUTH);
        add(main);

        // ── Load data ─────────────────────────────────
        new Thread(() -> {
            try {
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, response, guest_count FROM guests WHERE response IS NOT NULL");
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String name     = rs.getString("name");
                    String response = rs.getString("response");
                    int    count    = rs.getInt("guest_count");
                    SwingUtilities.invokeLater(() ->
                        model.addRow(new Object[]{name, response, count}));
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }).start();

        back.addActionListener(e -> { dashboard.setVisible(true); dispose(); });
    }
}