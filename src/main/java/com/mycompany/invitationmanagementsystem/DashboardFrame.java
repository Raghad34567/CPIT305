package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*
 * This frame is the main dashboard for managing the system.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class DashboardFrame extends JFrame {

    // Constructor: builds the main window and prepares all GUI components.
    public DashboardFrame() {
        // Set frame title
        // Set the title that appears on the top of the window.
        setTitle("Dashboard");
        // Set frame size
        // Set the size of the window.
        setSize(950, 660);
        // Open frame in center of screen
        // Show the window in the center of the screen.
        setLocationRelativeTo(null);
        // Decide what happens when the user closes this window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());
        main.add(UITheme.createHeader("Event Management Dashboard"), BorderLayout.NORTH);

        // Create a panel to organize the components on the screen.
        JPanel grid = new JPanel(new GridLayout(2, 2, 28, 28));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(18, 52, 18, 52));

        MenuCard createEvent  = new MenuCard("Create Event");
        MenuCard manageGuests = new MenuCard("Manage Guests");
        MenuCard viewResponses = new MenuCard("View Response");
        MenuCard reports      = new MenuCard("Reports");

        grid.add(createEvent);
        grid.add(manageGuests);
        grid.add(viewResponses);
        grid.add(reports);

        main.add(grid, BorderLayout.CENTER);

        // Create a button that the user can click.
        JButton logout = new JButton("Logout");
        main.add(UITheme.createButtonBar(logout), BorderLayout.SOUTH);

        add(main);

        // Show the selected window to the user.
        createEvent.onClick(e  -> { new CreateEventFrame(this).setVisible(true);  setVisible(false); });
        // Show the selected window to the user.
        manageGuests.onClick(e -> { new ManageGuestsFrame(this).setVisible(true); setVisible(false); });
        // Show the selected window to the user.
        viewResponses.onClick(e -> { new ResponsesFrame(this).setVisible(true);   setVisible(false); });
        // Show the selected window to the user.
        reports.onClick(e      -> { new ReportsFrame(this).setVisible(true);      setVisible(false); });

        // This action runs when the user clicks this button.
        logout.addActionListener(e -> {
            // Show a message box to tell the user the result.
            JOptionPane.showMessageDialog(this, "Logged out successfully");
            // Show the selected window to the user.
            new RoleSelectionFrame().setVisible(true);
            // Close the current window.
            dispose();
        });
    }

    // MenuCard 
    static class MenuCard extends JPanel {
        private boolean hovered = false;
        private boolean pressed = false;
        private final String text;
        private ActionListener listener;

        MenuCard(String text) {
            this.text = text;
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                // This method handles the mouseEntered part of the class logic.
                public void mouseEntered(MouseEvent e)  { hovered = true;  repaint(); }
                // This method handles the mouseExited part of the class logic.
                public void mouseExited (MouseEvent e)  { hovered = false; pressed = false; repaint(); }
                // This method handles the mousePressed part of the class logic.
                public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
                // This method handles the mouseReleased part of the class logic.
                public void mouseReleased(MouseEvent e) {
                    pressed = false; repaint();
                    if (listener != null && contains(e.getPoint()))
                        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text));
                }
            });
        }

        void onClick(ActionListener l) { this.listener = l; }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            Color fill = pressed ? UITheme.PRIMARY_DK
                       : hovered ? UITheme.PRIMARY_LT
                       : UITheme.PRIMARY;
            // shadow
            g2.setColor(new Color(80, 0, 25, 55));
            g2.fillRoundRect(4, 6, w - 5, h - 5, 26, 26);
            // body
            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 3, h - 3, 24, 24);
            // top shine
            g2.setColor(new Color(255, 255, 255, 22));
            g2.fillRoundRect(6, 3, w - 16, h / 3, 20, 20);
            // gold border
            g2.setColor(new Color(198, 155, 80, 170));
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(1, 1, w - 5, h - 5, 24, 24);
            // label
            g2.setFont(new Font("Serif", Font.BOLD, 18));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(text,
                (w - fm.stringWidth(text)) / 2,
                (h - fm.getHeight()) / 2 + fm.getAscent());
            // Close the current window.
            g2.dispose();
        }
    }
}
