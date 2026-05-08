package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashboardFrame extends JFrame {

    public DashboardFrame() {
        setTitle("Dashboard");
        setSize(950, 660);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());
        main.add(UITheme.createHeader("Event Management Dashboard"), BorderLayout.NORTH);

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

        JButton logout = new JButton("Logout");
        main.add(UITheme.createButtonBar(logout), BorderLayout.SOUTH);

        add(main);

        createEvent.onClick(e  -> { new CreateEventFrame(this).setVisible(true);  setVisible(false); });
        manageGuests.onClick(e -> { new ManageGuestsFrame(this).setVisible(true); setVisible(false); });
        viewResponses.onClick(e -> { new ResponsesFrame(this).setVisible(true);   setVisible(false); });
        reports.onClick(e      -> { new ReportsFrame(this).setVisible(true);      setVisible(false); });

        logout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logged out successfully");
            new RoleSelectionFrame().setVisible(true);
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
                public void mouseEntered(MouseEvent e)  { hovered = true;  repaint(); }
                public void mouseExited (MouseEvent e)  { hovered = false; pressed = false; repaint(); }
                public void mousePressed(MouseEvent e)  { pressed = true;  repaint(); }
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
            g2.dispose();
        }
    }
}