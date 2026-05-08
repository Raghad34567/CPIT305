package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResponsesFrame extends JFrame {

    DashboardFrame dashboard;
    JComboBox<String> eventBox;
    DefaultTableModel model;
    JTable table;
    int selectedEventId = -1;

    public ResponsesFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Guest Responses");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        JLabel title = new JLabel("RSVP Responses", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UITheme.TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        eventBox = new JComboBox<>();
        eventBox.setFont(new Font("Serif", Font.PLAIN, 16));
        eventBox.setBorder(BorderFactory.createTitledBorder("Select Event"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        topPanel.add(title, BorderLayout.NORTH);
        topPanel.add(eventBox, BorderLayout.SOUTH);

        model = new DefaultTableModel(
                new String[]{"Guest Name", "Email", "Response", "Guests Count"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Serif", Font.BOLD, 16));
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        JButton refresh = new JButton("Refresh");
        JButton back = new JButton("Back");

        UITheme.styleButton(refresh);
        UITheme.styleButton(back);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        bottom.add(refresh);
        bottom.add(back);

        main.add(topPanel, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);

        loadEvents();

        eventBox.addActionListener(e -> loadResponses());

        refresh.addActionListener(e -> loadResponses());

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void loadEvents() {
        try {
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM events");

            eventBox.removeAllItems();

            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadResponses();
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events!");
        }
    }

    private void loadResponses() {
        model.setRowCount(0);

        if (eventBox.getSelectedItem() == null) {
            return;
        }

        try {
            String selected = eventBox.getSelectedItem().toString();
            selectedEventId = Integer.parseInt(selected.split(":")[0].trim());

            Connection conn = DBConnection.connect();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT name, phone, response, guest_count " +
                    "FROM guests " +
                    "WHERE event_id=?"
            );

            ps.setInt(1, selectedEventId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String response = rs.getString("response");

                if (response == null || response.trim().isEmpty()) {
                    response = "No Response Yet";
                }

                model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("phone"),
                        response,
                        rs.getInt("guest_count")
                });
            }

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading responses!");
        }
    }
}