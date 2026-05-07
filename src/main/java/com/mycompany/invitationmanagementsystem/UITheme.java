package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class UITheme {

    // Wedding Luxury Colors
    public static Color PRIMARY    = new Color(139, 20, 60);   // Deep Rose
    public static Color BACKGROUND = new Color(255, 248, 248); // Soft blush
    public static Color CARD       = new Color(255, 255, 255);
    public static Color TEXT       = new Color(60, 30, 40);

    // ──────────────────────────────────────────────
    //  BUTTON STYLE  (visible on macOS & Windows)
    // ──────────────────────────────────────────────
    public static void styleButton(JButton button) {
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));

        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                int w = b.getWidth(), h = b.getHeight();
                int arc = h; // true pill shape

                Object hover   = b.getClientProperty("hovered");
                Object pressed = b.getClientProperty("pressed");
                Color bg = Boolean.TRUE.equals(pressed)
                    ? new Color(110, 10, 45)
                    : Boolean.TRUE.equals(hover)
                        ? new Color(170, 30, 75)
                        : PRIMARY;

                // shadow
                g2.setColor(new Color(0, 0, 0, 35));
                g2.fillRoundRect(2, 4, w - 3, h - 3, arc, arc);
                // fill
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, w - 2, h - 2, arc, arc);
                // border
                g2.setColor(new Color(100, 10, 40));
                g2.setStroke(new BasicStroke(1.6f));
                g2.drawRoundRect(0, 0, w - 3, h - 3, arc, arc);
                // text
                FontMetrics fm = g2.getFontMetrics(b.getFont());
                g2.setFont(b.getFont());
                g2.setColor(Color.WHITE);
                String text = b.getText();
                int tx = (w - fm.stringWidth(text)) / 2;
                int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        });

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.putClientProperty("hovered", Boolean.TRUE);
                button.repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.putClientProperty("hovered", Boolean.FALSE);
                button.putClientProperty("pressed", Boolean.FALSE);
                button.repaint();
            }
            public void mousePressed(java.awt.event.MouseEvent e) {
                button.putClientProperty("pressed", Boolean.TRUE);
                button.repaint();
            }
            public void mouseReleased(java.awt.event.MouseEvent e) {
                button.putClientProperty("pressed", Boolean.FALSE);
                button.repaint();
            }
        });
    }

    // ──────────────────────────────────────────────
    //  CARD PANEL
    // ──────────────────────────────────────────────
    public static JPanel createCard(int width, int height) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // subtle card shadow
                g2.setColor(new Color(180, 100, 120, 40));
                g2.fillRoundRect(4, 6, getWidth() - 6, getHeight() - 6, 30, 30);
                // card body
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 28, 28);
                // border
                g2.setColor(new Color(200, 150, 160));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 28, 28);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width, height));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setLayout(new GridLayout(0, 1, 18, 18));
        return panel;
    }

    // ──────────────────────────────────────────────
    //  ROSE BACKGROUND PANEL  (all static, no animation)
    // ──────────────────────────────────────────────
    public static JPanel createRoseBackground() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient background
                GradientPaint grad = new GradientPaint(
                    0, 0, new Color(255, 240, 244),
                    getWidth(), getHeight(), new Color(250, 225, 235)
                );
                g2.setPaint(grad);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Draw static roses at fixed positions
                int[][] positions = {
                    {30, 30}, {getWidth()-80, 20}, {15, getHeight()-80},
                    {getWidth()-70, getHeight()-80}, {getWidth()/2 - 60, 15},
                    {getWidth()/2 + 40, getHeight()-70}, {60, getHeight()/2 - 40},
                    {getWidth()-90, getHeight()/2 + 20}
                };
                int[] sizes  = {52, 46, 50, 44, 48, 46, 44, 50};
                float[] alphas= {0.28f, 0.22f, 0.25f, 0.20f, 0.24f, 0.22f, 0.20f, 0.26f};

                for (int i = 0; i < positions.length; i++) {
                    drawRose(g2, positions[i][0], positions[i][1], sizes[i], alphas[i]);
                }

                // Small accent roses
                int[][] small = {
                    {getWidth()/4, 8}, {3*getWidth()/4, getHeight()-40},
                    {10, getHeight()/3}, {getWidth()-30, 2*getHeight()/3}
                };
                for (int[] p : small) {
                    drawRose(g2, p[0], p[1], 28, 0.16f);
                }

                g2.dispose();
            }

            private void drawRose(Graphics2D g2, int cx, int cy, int r, float alpha) {
                g2 = (Graphics2D) g2.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                int petals = 5;
                double petalW = Math.toRadians(38);
                Color petal1 = new Color(210, 60, 100);
                Color petal2 = new Color(240, 120, 150);

                // outer petals
                for (int p = 0; p < petals; p++) {
                    double angle = Math.toRadians(p * (360.0 / petals)) - Math.PI / 2;
                    int px = (int) (cx + (r * 0.55) * Math.cos(angle));
                    int py = (int) (cy + (r * 0.55) * Math.sin(angle));
                    int pw = (int) (r * 0.75);
                    int ph = (int) (r * 0.55);
                    GradientPaint gp = new GradientPaint(px, py, petal1,
                                                         px + pw / 2, py + ph / 2, petal2);
                    g2.setPaint(gp);
                    Shape petal = createPetal(px - pw / 2, py - ph / 2, pw, ph, angle);
                    g2.fill(petal);
                }

                // inner petals
                for (int p = 0; p < petals; p++) {
                    double angle = Math.toRadians(p * (360.0 / petals));
                    int px = (int) (cx + (r * 0.28) * Math.cos(angle));
                    int py = (int) (cy + (r * 0.28) * Math.sin(angle));
                    int pw = (int) (r * 0.50);
                    int ph = (int) (r * 0.38);
                    g2.setColor(new Color(220, 80, 115));
                    Shape petal = createPetal(px - pw / 2, py - ph / 2, pw, ph, angle);
                    g2.fill(petal);
                }

                // centre
                g2.setColor(new Color(180, 40, 80));
                int cr = (int)(r * 0.22);
                g2.fillOval(cx - cr, cy - cr, cr * 2, cr * 2);

                g2.dispose();
            }

            private Shape createPetal(int x, int y, int w, int h, double angle) {
                Ellipse2D.Double e = new Ellipse2D.Double(x, y, w, h);
                AffineTransform at = AffineTransform.getRotateInstance(
                    angle, x + w / 2.0, y + h / 2.0);
                return at.createTransformedShape(e);
            }
        };
    }
}
