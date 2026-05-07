package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

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

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        JLabel title = new JLabel("Guest List", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(UITheme.TEXT);

        eventBox = new JComboBox<>();
        eventBox.setFont(new Font("Serif", Font.PLAIN, 16));
        eventBox.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(180, 100, 130), 1),
                "Select Event"));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        top.add(title, BorderLayout.NORTH);
        top.add(eventBox, BorderLayout.SOUTH);

        model = new DefaultTableModel(new String[]{"Name", "Email"}, 0);
        JTable table = new JTable(model);
        table.setFont(new Font("Serif", Font.PLAIN, 16));
        table.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(table);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton delete = new JButton("Delete");
        JButton sendInvitations = new JButton("Send Invitations");
        JButton back = new JButton("Back");

        UITheme.styleButton(add);
        UITheme.styleButton(edit);
        UITheme.styleButton(delete);
        UITheme.styleButton(sendInvitations);
        UITheme.styleButton(back);

        buttons.add(add);
        buttons.add(edit);
        buttons.add(delete);
        buttons.add(sendInvitations);
        buttons.add(back);

        main.add(top, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);
        add(main);

        loadEvents();

        eventBox.addActionListener(e -> loadGuests());

        add.addActionListener(e -> {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }

            String name = JOptionPane.showInputDialog(this, "Guest Name:");
            String email = JOptionPane.showInputDialog(this, "Guest Email:");

            if (name == null || name.trim().isEmpty()) return;

            if (email == null || email.trim().isEmpty() || !email.contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter valid email");
                return;
            }

            new Thread(() -> {
                try {
                    Connection conn = DBConnection.connect();

                    PreparedStatement ps = conn.prepareStatement(
                            "INSERT INTO guests(name, phone, event_id) VALUES (?,?,?)");

                    ps.setString(1, name);
                    ps.setString(2, email);
                    ps.setInt(3, selectedEventId);
                    ps.executeUpdate();

                    java.io.FileWriter fw = new java.io.FileWriter("guests.txt", true);
                    fw.write(name + "," + email + "," + selectedEventId + "\n");
                    fw.close();

                    SwingUtilities.invokeLater(() -> {
                        model.addRow(new Object[]{name, email});
                        JOptionPane.showMessageDialog(this, "Guest Added!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "Database/File Error!"));
                }
            }).start();
        });

        edit.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row");
                return;
            }

            String oldName = model.getValueAt(row, 0).toString();

            String newName = JOptionPane.showInputDialog(this, "Edit Name:", oldName);
            String newEmail = JOptionPane.showInputDialog(this, "Edit Email:",
                    model.getValueAt(row, 1));

            if (newName == null || newName.trim().isEmpty()) return;

            if (newEmail == null || newEmail.trim().isEmpty() || !newEmail.contains("@")) {
                JOptionPane.showMessageDialog(this, "Enter valid email");
                return;
            }

            new Thread(() -> {
                try {
                    Connection conn = DBConnection.connect();

                    PreparedStatement ps = conn.prepareStatement(
                            "UPDATE guests SET name=?, phone=? WHERE name=? AND event_id=?");

                    ps.setString(1, newName);
                    ps.setString(2, newEmail);
                    ps.setString(3, oldName);
                    ps.setInt(4, selectedEventId);
                    ps.executeUpdate();

                    SwingUtilities.invokeLater(() -> {
                        model.setValueAt(newName, row, 0);
                        model.setValueAt(newEmail, row, 1);
                        JOptionPane.showMessageDialog(this, "Guest updated!");
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this, "Database Error!"));
                }
            }).start();
        });

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
                        "DELETE FROM guests WHERE name=? AND event_id=?");

                ps.setString(1, name);
                ps.setInt(2, selectedEventId);
                ps.executeUpdate();

                model.removeRow(row);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error!");
            }
        });

        sendInvitations.addActionListener(e -> {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }

            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No guests to send invitations");
                return;
            }

            String eventName = getSelectedEventName();
            String encodedEventName = URLEncoder.encode(eventName, StandardCharsets.UTF_8);
            String inviteLink = "www.invite.com/" + encodedEventName;

            sendInvitations.setEnabled(false);

            new Thread(() -> {
                int sentCount = 0;

                try {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String guestName = model.getValueAt(i, 0).toString();
                        String guestEmail = model.getValueAt(i, 1).toString();

                        EmailClient.sendInvitationRequest(guestEmail, guestName, inviteLink);
                        sentCount++;
                    }

                    int finalSentCount = sentCount;

                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this,
                                    "Invitations sent successfully to " + finalSentCount + " guests!"));

                } catch (Exception ex) {
                    ex.printStackTrace();

                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(this,
                                    "Error sending invitations.\nMake sure EmailServer is running."));
                } finally {
                    SwingUtilities.invokeLater(() -> sendInvitations.setEnabled(true));
                }
            }).start();
        });

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }

    private void loadEvents() {
        try {
            Connection conn = DBConnection.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM events");

            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadGuests();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadGuests() {
        model.setRowCount(0);

        if (eventBox.getSelectedItem() == null) return;

        String selected = eventBox.getSelectedItem().toString();
        selectedEventId = Integer.parseInt(selected.split(":")[0].trim());

        try {
            Connection conn = DBConnection.connect();

            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM guests WHERE event_id=?");

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
    }

    private String getSelectedEventName() {
        String selected = eventBox.getSelectedItem().toString();
        return selected.substring(selected.indexOf(":") + 1).trim();
    }
}