package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.FileWriter;

public class ManageGuestsFrame extends JFrame {

    DefaultTableModel model;
    JComboBox<String> eventBox;
    int selectedEventId = -1;
    DashboardFrame dashboard;

    public ManageGuestsFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Manage Guests");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UITheme.BACKGROUND);

        JLabel title = new JLabel("Wedding Guest List", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);

        eventBox = new JComboBox<>();
        eventBox.setFont(new Font("Serif", Font.PLAIN, 16));
        eventBox.setBorder(BorderFactory.createTitledBorder("Select Event"));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(UITheme.BACKGROUND);
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        top.add(title, BorderLayout.NORTH);
        top.add(eventBox, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"Name", "Phone"}, 0);
        JTable table = new JTable(model);

        JScrollPane scroll = new JScrollPane(table);

        JPanel buttons = new JPanel();
        buttons.setBackground(UITheme.BACKGROUND);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton back = new JButton("Back");

        UITheme.styleButton(add);
        UITheme.styleButton(edit);
        UITheme.styleButton(delete);
        UITheme.styleButton(back);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(back);

        main.add(top, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);

        // ================= LOAD EVENTS =================
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM events");

            while (rs.next()) {
                eventBox.addItem(
                        rs.getInt("id") + " : " + rs.getString("name")
                );
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // ================= EVENT SELECT =================
        eventBox.addActionListener(e -> {

            model.setRowCount(0);

            if (eventBox.getSelectedItem() == null) {
                return;
            }

            String selected = eventBox.getSelectedItem().toString();
            selectedEventId = Integer.parseInt(selected.split(":")[0].trim());

            try {
                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement(
                        "SELECT * FROM guests WHERE event_id=?"
                );
                ps.setInt(1, selectedEventId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getString("name"),
                        rs.getString("phone")
                    });
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ================= ADD (DB + FILE IO) =================
        add.addActionListener(e -> {

            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Guest Name:");
            String phone = JOptionPane.showInputDialog(this, "Phone:");

            if (name == null || name.trim().isEmpty()) {
                return;
            }

            new Thread(() -> {
                try {
                    Connection conn = DBConnection.connect();

                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO guests(name, phone, event_id) VALUES (?,?,?)"
                    );

                    ps.setString(1, name);
                    ps.setString(2, phone);
                    ps.setInt(3, selectedEventId);

                    ps.executeUpdate();

                    java.io.FileWriter fw = new java.io.FileWriter("guests.txt", true);
                    fw.write(name + "," + phone + "," + selectedEventId + "\n");
                    fw.close();

                    SwingUtilities.invokeLater(() -> {
                        model.addRow(new Object[]{name, phone});
                        JOptionPane.showMessageDialog(this, "Guest Added!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Database/File Error!")
                    );
                }
            }).start();

        });

        // ================= EDIT =================
        edit.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row");
                return;
            }

            String oldName = model.getValueAt(row, 0).toString();

            String newName = JOptionPane.showInputDialog(this, "Edit Name:", oldName);
            String newPhone = JOptionPane.showInputDialog(this, "Edit Phone:",
                    model.getValueAt(row, 1));

            if (newName == null || newName.trim().isEmpty()) {
                return;
            }

            // THREAD STARTS HERE 
            new Thread(() -> {
                try {
                    Connection conn = DBConnection.connect();

                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE guests SET name=?, phone=? WHERE name=?"
                    );

                    ps.setString(1, newName);
                    ps.setString(2, newPhone);
                    ps.setString(3, oldName);

                    ps.executeUpdate();

                    SwingUtilities.invokeLater(() -> {
                        model.setValueAt(newName, row, 0);
                        model.setValueAt(newPhone, row, 1);
                        JOptionPane.showMessageDialog(this, "Guest updated!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();

                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "Database Error!")
                    );
                }
            }).start();

        });

        // ================= DELETE =================
        delete.addActionListener(e -> {

            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row");
                return;
            }

            String name = model.getValueAt(row, 0).toString();

            try {
                Connection conn = DBConnection.connect();

                PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM guests WHERE name=?"
                );

                ps.setString(1, name);
                ps.executeUpdate();

                model.removeRow(row);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // ================= BACK =================
        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }
}
