package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.FileWriter;

public class CreateEventFrame extends JFrame {

    DashboardFrame dashboard;

    public CreateEventFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        setTitle("Create Wedding Event");
        setSize(750, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(520, 420);
        card.setLayout(new BorderLayout(1, 1));

        JLabel title = new JLabel("Create New Wedding Event", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        JPanel fields = new JPanel(new GridLayout(4, 1, 15, 15));
        fields.setOpaque(false);
        fields.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField name     = createField("Event Name");
        JTextField date     = createField("Date");
        JTextField location = createField("Location");
        JTextField capacity = createField("Capacity");

        fields.add(name);
        fields.add(date);
        fields.add(location);
        fields.add(capacity);

        JButton save = new JButton("Save Event");
        JButton back = new JButton("Back");
        UITheme.styleButton(save);
        UITheme.styleButton(back);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(save);
        buttons.add(back);

        card.add(title, BorderLayout.NORTH);
        card.add(fields, BorderLayout.CENTER);
        card.add(buttons, BorderLayout.SOUTH);

        main.add(card);
        add(main);

        // ================= SAVE =================
        save.addActionListener(e -> {
            try {
                String eventName     = name.getText().trim();
                String eventDate     = date.getText().trim();
                String eventLocation = location.getText().trim();
                String eventCapacity = capacity.getText().trim();

                if (eventName.isEmpty() || eventDate.isEmpty()
                        || eventLocation.isEmpty() || eventCapacity.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fill all fields");
                    return;
                }

                Connection conn = DBConnection.connect();
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO events(name, date, location, capacity) VALUES (?,?,?,?)");
                ps.setString(1, eventName);
                ps.setString(2, eventDate);
                ps.setString(3, eventLocation);
                ps.setInt(4, Integer.parseInt(eventCapacity));
                ps.executeUpdate();

                FileWriter fw = new FileWriter("events.txt", true);
                fw.write(eventName + "," + eventDate + "," + eventLocation + "," + eventCapacity + "\n");
                fw.close();

                JOptionPane.showMessageDialog(this, "Event Created & Saved!");
                name.setText("");
                date.setText("");
                location.setText("");
                capacity.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Capacity must be a number");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error!");
            }
        });

        // ================= BACK =================
        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });
    }

    private JTextField createField(String label) {
        JTextField field = new JTextField();
        field.setFont(new Font("Serif", Font.PLAIN, 18));
        field.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 100, 130), 1), label));
        return field;
    }
}
