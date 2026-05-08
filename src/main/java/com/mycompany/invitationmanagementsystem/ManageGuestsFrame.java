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
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout(0, 0));

        // ── Header ───────────────────────────────────
        main.add(UITheme.createHeader("Guest List"), BorderLayout.NORTH);

        // ── ComboBox (Select Event) ───────────────────
        eventBox = UITheme.createComboBox("Select Event");

        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);

        main.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);

        // نضع الـ header والـ combobox معاً في panel شمالي
        JPanel northWrapper = new JPanel(new BorderLayout(0, 0));
        northWrapper.setOpaque(false);
        northWrapper.add(UITheme.createHeader("Guest List"), BorderLayout.NORTH);
        northWrapper.add(topPanel, BorderLayout.SOUTH);
        main.add(northWrapper, BorderLayout.NORTH);

        // ── Table ─────────────────────────────────────
        model = new DefaultTableModel(new String[]{"Guest Name", "Email"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        UITheme.styleTable(table);

        JScrollPane scroll = UITheme.createStyledScroll(table);
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(4, 24, 4, 24),
            BorderFactory.createLineBorder(UITheme.BORDER, 1, true)));

        main.add(scroll, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────
        JButton add             = new JButton("Add");
        JButton edit            = new JButton("Edit");
        JButton delete          = new JButton("Delete");
        JButton sendInvitations = new JButton("Send Invitations");
        JButton back            = new JButton("Back");

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 0, 16, 0));
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

        main.add(buttons, BorderLayout.SOUTH);
        add(main);

        // ── Load data ─────────────────────────────────
        loadEvents();
        eventBox.addActionListener(e -> loadGuests());

        // ── ADD ───────────────────────────────────────
        add.addActionListener(e -> {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Select event first");
                return;
            }
            String name  = JOptionPane.showInputDialog(this, "Guest Name:");
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

        // ── EDIT ──────────────────────────────────────
        edit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row"); return; }
            String oldName  = model.getValueAt(row, 0).toString();
            String newName  = JOptionPane.showInputDialog(this, "Edit Name:", oldName);
            String newEmail = JOptionPane.showInputDialog(this, "Edit Email:", model.getValueAt(row, 1));
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

        // ── DELETE ────────────────────────────────────
        delete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "Select a row"); return; }
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

        // ── SEND INVITATIONS ──────────────────────────
        sendInvitations.addActionListener(e -> {
            if (selectedEventId == -1) { JOptionPane.showMessageDialog(this, "Select event first"); return; }
            if (model.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "No guests to send invitations"); return; }
            String eventName        = getSelectedEventName();
            String encodedEventName = URLEncoder.encode(eventName, StandardCharsets.UTF_8);
            sendInvitations.setEnabled(false);
            new Thread(() -> {
                int sentCount = 0;
                try {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String guestName        = model.getValueAt(i, 0).toString();
                        String guestEmail       = model.getValueAt(i, 1).toString();
                        String encodedGuestEmail = URLEncoder.encode(guestEmail, StandardCharsets.UTF_8);
                        String inviteLink       = "www.invite.com/" + encodedEventName + "?guestEmail=" + encodedGuestEmail;
                        EmailClient.sendInvitationRequest(guestEmail, guestName, inviteLink);
                        sentCount++;
                    }
                    int finalCount = sentCount;
                    SwingUtilities.invokeLater(() ->
                        JOptionPane.showMessageDialog(this,
                            "Invitations sent successfully to " + finalCount + " guests!"));
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

        back.addActionListener(e -> { dashboard.setVisible(true); dispose(); });
    }

    private void loadEvents() {
        try {
            Connection conn = DBConnection.connect();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery("SELECT * FROM events");
            while (rs.next())
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadGuests();
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void loadGuests() {
        model.setRowCount(0);
        if (eventBox.getSelectedItem() == null) return;
        String selected = eventBox.getSelectedItem().toString();
        selectedEventId = Integer.parseInt(selected.split(":")[0].trim());
        try {
            Connection conn       = DBConnection.connect();
            PreparedStatement ps  = conn.prepareStatement(
                "SELECT * FROM guests WHERE event_id=?");
            ps.setInt(1, selectedEventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                model.addRow(new Object[]{rs.getString("name"), rs.getString("phone")});
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private String getSelectedEventName() {
        String selected = eventBox.getSelectedItem().toString();
        return selected.substring(selected.indexOf(":") + 1).trim();
    }
}