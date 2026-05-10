package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

/*
 * This frame allows organizers to create new events.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class CreateEventFrame extends JFrame {

    DashboardFrame dashboard;

    // Constructor: builds the main window and prepares all GUI components.
    public CreateEventFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Create Event");
        // Set frame size
        // Set the size of the window.
        setSize(750, 550);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCard(520, 420);
        card.setLayout(new BorderLayout(1, 1));

        // Create a label to display text for the user.
        JLabel title = new JLabel("Create New Event", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT);

        // Create a panel to organize the components on the screen.
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

        // Create a button that the user can click.
        JButton save = new JButton("Save Event");
        // Create a button that the user can click.
        JButton back = new JButton("Back");
        UITheme.styleButton(save);
        UITheme.styleButton(back);

        // Create a panel to organize the components on the screen.
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
        // This action runs when the user clicks this button.
        save.addActionListener(e -> {
            try {
                String eventName     = name.getText().trim();
                String eventDate     = date.getText().trim();
                String eventLocation = location.getText().trim();
                String eventCapacity = capacity.getText().trim();

                if (eventName.isEmpty() || eventDate.isEmpty()
                        || eventLocation.isEmpty() || eventCapacity.isEmpty()) {
                    // Show a message box to tell the user the result.
                    JOptionPane.showMessageDialog(this, "Fill all fields");
                    return;
                }

                // Connect to the database before running the SQL query.
                Connection conn = DBConnection.connect();
                // Prepare the SQL statement to send it safely to the database.
                PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO events(name, date, location, capacity) VALUES (?,?,?,?)");

                ps.setString(1, eventName);
                ps.setString(2, eventDate);
                ps.setString(3, eventLocation);
                ps.setInt(4, Integer.parseInt(eventCapacity));
                // Execute an SQL command that changes data or creates tables.
                ps.executeUpdate();

                // Close database tools after saving the event
                // Close this resource after finishing to avoid connection problems.
                ps.close();
                // Close this resource after finishing to avoid connection problems.
                conn.close();

                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Event Created & Saved!");
                name.setText("");
                date.setText("");
                location.setText("");
                capacity.setText("");

            } catch (NumberFormatException ex) {
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Capacity must be a number");
            } catch (Exception ex) {
                ex.printStackTrace();
                // Show a message box to tell the user the result.
                JOptionPane.showMessageDialog(this, "Error!");
            }
        });

        // ================= BACK =================
        // This action runs when the user clicks this button.
        back.addActionListener(e -> {
            // Show the selected window to the user.
            dashboard.setVisible(true);
            // Close the current window.
            dispose();
        });
    }

    // This method creates a text field with the same style used in the project.
    private JTextField createField(String label) {
        // Create a text field so the user can type data.
        JTextField field = new JTextField();
        field.setFont(new Font("Serif", Font.PLAIN, 18));
        field.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 100, 130), 1), label));
        return field;
    }
}
