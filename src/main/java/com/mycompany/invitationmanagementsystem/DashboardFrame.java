package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * This frame is the main dashboard for the organizer.
 *
 * Factory Method Pattern:
 * Before, this class created frames directly using "new".
 * Now, it uses factory classes to create the needed frame.
 *
 * This makes the dashboard cleaner because it does not need
 * to know the details of how each frame is created.
 */
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

        /*
         * Each menu option has its own factory.
         * The key is the button/card name.
         * The value is the factory that creates the correct frame.
         */
        Map<String, FrameFactory> factories = new LinkedHashMap<>();
        factories.put("Create Event", new CreateEventFactory());
        factories.put("Manage Guests", new ManageGuestsFactory());
        factories.put("View Response", new ResponsesFactory());
        factories.put("Reports", new ReportsFactory());

        /*
         * This loop creates one card for each factory.
         * When the user clicks a card, we ask its factory to create the frame.
         */
        for (Map.Entry<String, FrameFactory> entry : factories.entrySet()) {

            String label = entry.getKey();
            FrameFactory factory = entry.getValue();

            MenuCard card = new MenuCard(label);

            card.onClick(e -> {

                // The factory creates the frame instead of the dashboard.
                JFrame frame = factory.createFrame(this);

                // Show the new frame.
                frame.setVisible(true);

                // Hide the dashboard while the selected frame is open.
                setVisible(false);
            });

            grid.add(card);
        }

        main.add(grid, BorderLayout.CENTER);

        JButton logout = new JButton("Logout");
        main.add(UITheme.createButtonBar(logout), BorderLayout.SOUTH);

        add(main);

        logout.addActionListener(e -> {

            JOptionPane.showMessageDialog(this, "Logged out successfully");

            new RoleSelectionFrame().setVisible(true);

            dispose();
        });
    }

    /*
     * MenuCard is a custom clickable card used in the dashboard.
     * It is not part of the Factory Method Pattern.
     * Its job is only to draw the card and handle mouse clicks.
     */
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

                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }

                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    pressed = false;
                    repaint();
                }

                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();

                    if (listener != null && contains(e.getPoint())) {
                        listener.actionPerformed(
                                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text)
                        );
                    }
                }
            });
        }

        void onClick(ActionListener l) {
            this.listener = l;
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();

            Color fill = pressed ? UITheme.PRIMARY_DK
                    : hovered ? UITheme.PRIMARY_LT
                    : UITheme.PRIMARY;

            g2.setColor(new Color(80, 0, 25, 55));
            g2.fillRoundRect(4, 6, w - 5, h - 5, 26, 26);

            g2.setColor(fill);
            g2.fillRoundRect(0, 0, w - 3, h - 3, 24, 24);

            g2.setColor(new Color(255, 255, 255, 22));
            g2.fillRoundRect(6, 3, w - 16, h / 3, 20, 20);

            g2.setColor(new Color(198, 155, 80, 170));
            g2.setStroke(new BasicStroke(1.6f));
            g2.drawRoundRect(1, 1, w - 5, h - 5, 24, 24);

            g2.setFont(new Font("Serif", Font.BOLD, 18));
            g2.setColor(Color.WHITE);

            FontMetrics fm = g2.getFontMetrics();

            g2.drawString(
                    text,
                    (w - fm.stringWidth(text)) / 2,
                    (h - fm.getHeight()) / 2 + fm.getAscent()
            );

            g2.dispose();
        }
    }
}