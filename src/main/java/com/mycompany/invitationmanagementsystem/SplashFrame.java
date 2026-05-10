package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

/*
 * This frame shows the splash/welcome screen with the logo before the role selection.
 *
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class SplashFrame extends JFrame {

    public SplashFrame() {
        setTitle("Invitation Management System");
        setSize(680, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        // Card panel
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(155, 75, 105, 32));
                g2.fillRoundRect(5, 7, w - 7, h - 7, 32, 32);
                g2.setColor(UITheme.CARD);
                g2.fillRoundRect(0, 0, w - 5, h - 5, 30, 30);
                g2.setColor(UITheme.BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 6, h - 6, 30, 30);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(480, 390));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(8, 32, 14, 32));

        // Logo panel
        LogoPanel logo = new LogoPanel();
        logo.setPreferredSize(new Dimension(420, 165));
        logo.setMaximumSize(new Dimension(420, 165));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setOpaque(false);

        // Title
        JLabel title = new JLabel("Invitation", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 38));
        title.setForeground(UITheme.PRIMARY_DK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel sub = new JLabel("Management System", SwingConstants.CENTER);
        sub.setFont(new Font("Serif", Font.PLAIN, 15));
        sub.setForeground(UITheme.TEXT_LIGHT);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Gold divider
        JLabel divider = new JLabel("\u2756  \u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014  \u2756", SwingConstants.CENTER);
        divider.setFont(new Font("Serif", Font.PLAIN, 12));
        divider.setForeground(UITheme.GOLD);
        divider.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Get Started button
        JButton getStarted = new JButton("Get Started  \u2192");
        UITheme.styleButton(getStarted);
        getStarted.setPreferredSize(new Dimension(220, 46));
        getStarted.setMaximumSize(new Dimension(220, 46));
        getStarted.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline
        JLabel tagline = new JLabel("Every guest. Every moment. Perfectly invited.", SwingConstants.CENTER);
        tagline.setFont(new Font("Serif", Font.ITALIC, 11));
        tagline.setForeground(new Color(176, 112, 144));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Assemble with tight spacing
        card.add(logo);
        card.add(Box.createVerticalStrut(0));
        card.add(title);
        card.add(Box.createVerticalStrut(1));
        card.add(sub);
        card.add(Box.createVerticalStrut(6));
        card.add(divider);
        card.add(Box.createVerticalStrut(10));
        card.add(getStarted);
        card.add(Box.createVerticalStrut(8));
        card.add(tagline);

        main.add(card);
        add(main);

        getStarted.addActionListener(e -> {
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });
    }

    // ═══════════════════════════════════════════════════════════
    //  LOGO PANEL — replicates the SVG logo in Java2D
    //  SVG viewBox: 0 0 680 420
    // ═══════════════════════════════════════════════════════════
    private static class LogoPanel extends JPanel {

        private static final double SVG_W = 680.0;
        private static final double SVG_CROP_Y1 = 42.0;
        private static final double SVG_CROP_H  = 215.0;
        private static final double SVG_H = 420.0;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,      RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            // Scale to panel width; shift up to crop empty space above logo
            double scale = getWidth() / SVG_W;
            double ty = -SVG_CROP_Y1 * scale + (getHeight() - SVG_CROP_H * scale) / 2.0;
            g2.translate(0, ty);
            g2.scale(scale, scale);

            drawLogo(g2);
            g2.dispose();
        }

        private void drawLogo(Graphics2D g) {
            // Background glow
            g.setColor(new Color(248, 232, 239, 120));
            g.fillOval(140, 60, 400, 240);

            // Gold flourishes LEFT
            g.setColor(new Color(198, 155, 80));
            g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 155,175, 135,160, 125,145);
            qCurve(g, 125,145, 115,130, 130,120);
            qCurve(g, 130,120, 145,112, 150,130);
            qCurve(g, 150,130, 148,148, 155,175);
            qCurve(g, 155,175, 140,185, 128,195);
            qCurve(g, 128,195, 115,205, 120,220);
            qCurve(g, 120,220, 128,232, 140,220);
            qCurve(g, 140,220, 150,208, 155,195);
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 130,120, 118,108, 112,95);
            qCurve(g, 112,95,  108,82,  118,78);
            qCurve(g, 118,78,  130,76,  128,90);
            qCurve(g, 120,220, 108,232, 105,248);
            qCurve(g, 105,248, 103,262, 115,264);
            qCurve(g, 115,264, 127,264, 124,250);
            g.setStroke(new BasicStroke(0.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 125,145, 108,140, 102,132);
            qCurve(g, 102,132, 98,122,  106,120);
            qCurve(g, 128,195, 112,198, 106,208);
            qCurve(g, 106,208, 102,218, 110,222);

            // Gold flourishes RIGHT
            g.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 525,175, 545,160, 555,145);
            qCurve(g, 555,145, 565,130, 550,120);
            qCurve(g, 550,120, 535,112, 530,130);
            qCurve(g, 530,130, 532,148, 525,175);
            qCurve(g, 525,175, 540,185, 552,195);
            qCurve(g, 552,195, 565,205, 560,220);
            qCurve(g, 560,220, 552,232, 540,220);
            qCurve(g, 540,220, 530,208, 525,195);
            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 550,120, 562,108, 568,95);
            qCurve(g, 568,95,  572,82,  562,78);
            qCurve(g, 562,78,  550,76,  552,90);
            qCurve(g, 560,220, 572,232, 575,248);
            qCurve(g, 575,248, 577,262, 565,264);
            qCurve(g, 565,264, 553,264, 556,250);
            g.setStroke(new BasicStroke(0.9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            qCurve(g, 555,145, 572,140, 578,132);
            qCurve(g, 578,132, 582,122, 574,120);
            qCurve(g, 552,195, 568,198, 574,208);
            qCurve(g, 574,208, 578,218, 570,222);

            // Roses LEFT
            drawRose(g, 168, 158, 13);
            drawRose(g, 132, 205, 10);
            drawBud(g, 155, 115);
            drawLeaf(g, 148, 178, -40);
            drawLeaf(g, 140, 195,  20);
            drawLeaf(g, 162, 135, -60);
            drawLeaf(g, 130, 225,  15);

            // Roses RIGHT
            drawRose(g, 512, 158, 13);
            drawRose(g, 548, 205, 10);
            drawBud(g, 525, 115);
            drawLeaf(g, 532, 178,  40);
            drawLeaf(g, 540, 195, -20);
            drawLeaf(g, 518, 135,  60);
            drawLeaf(g, 550, 225, -15);

            // Sparkles
            g.setColor(new Color(198, 155, 80, 230));
            sparkle(g, 340, 48, 8);
            g.setColor(new Color(198, 155, 80, 178));
            sparkle(g, 316, 60, 5);
            sparkle(g, 364, 60, 5);
            g.setColor(new Color(198, 155, 80, 128));
            sparkle(g, 330, 52, 4);
            sparkle(g, 350, 52, 4);

            // Envelope body
            g.setColor(new Color(255, 240, 245));
            g.fillRoundRect(218, 78, 244, 158, 10, 10);
            g.setColor(new Color(139, 20, 60));
            g.setStroke(new BasicStroke(1.8f));
            g.drawRoundRect(218, 78, 244, 158, 10, 10);

            // Envelope flap
            Polygon flap = new Polygon(new int[]{218, 340, 462}, new int[]{78, 150, 78}, 3);
            g.setColor(new Color(252, 224, 236));
            g.fillPolygon(flap);
            g.setColor(new Color(139, 20, 60));
            g.setStroke(new BasicStroke(1.8f));
            g.drawPolygon(flap);

            // Inner flap
            Polygon iflap = new Polygon(new int[]{218, 340, 462}, new int[]{78, 128, 78}, 3);
            g.setColor(new Color(250, 208, 224));
            g.fillPolygon(iflap);
            g.setColor(new Color(208, 144, 144));
            g.setStroke(new BasicStroke(0.8f));
            g.drawPolygon(iflap);

            // Fold lines
            g.setColor(new Color(224, 176, 200));
            g.setStroke(new BasicStroke(0.9f));
            g.drawLine(218, 236, 340, 168);
            g.drawLine(462, 236, 340, 168);

            // Deco lines
            g.setColor(new Color(240, 192, 216));
            g.setStroke(new BasicStroke(0.8f));
            g.drawLine(248, 192, 432, 192);
            g.drawLine(248, 210, 415, 210);

            // Heart seal
            heart(g, 340, 151, 20, new Color(139, 20, 60), true);
            heart(g, 340, 151, 16, new Color(192, 64, 112, 128), true);
            // Gold ring
            g.setColor(new Color(198, 155, 80, 178));
            g.setStroke(new BasicStroke(1.2f));
            heart(g, 340, 151, 23, new Color(198, 155, 80, 178), false);
        }

        // Quadratic bezier curve helper
        private void qCurve(Graphics2D g, double x1, double y1,
                             double cx, double cy,
                             double x2, double y2) {
            GeneralPath p = new GeneralPath();
            p.moveTo(x1, y1);
            p.quadTo(cx, cy, x2, y2);
            g.draw(p);
        }

        private void drawRose(Graphics2D g, int cx, int cy, int r) {
            int[] outAngles = {0, 50, 110, 180, 230, 290};
            Color[] outColors = {
                new Color(192,64,112,178), new Color(160,48,96,178),
                new Color(192,64,112,178), new Color(160,48,96,178),
                new Color(192,64,112,178), new Color(160,48,96,178)
            };
            for (int i = 0; i < 6; i++) {
                double a = Math.toRadians(outAngles[i]);
                int px = (int)(cx + Math.cos(a) * r);
                int py = (int)(cy + Math.sin(a) * r);
                Graphics2D tg = (Graphics2D) g.create();
                tg.setColor(outColors[i]);
                tg.translate(px, py);
                tg.rotate(a);
                tg.fillOval(-(r+2)/2, -(int)(r*0.78)/2, r+2, (int)(r*0.78));
                tg.dispose();
            }
            int[] inAngles = {15, 135, 255};
            int ir = (int)(r * 0.55);
            for (int a : inAngles) {
                double rad = Math.toRadians(a);
                int px = (int)(cx + Math.cos(rad) * ir);
                int py = (int)(cy + Math.sin(rad) * ir);
                Graphics2D tg = (Graphics2D) g.create();
                tg.setColor(new Color(139, 20, 60, 200));
                tg.translate(px, py);
                tg.rotate(rad);
                tg.fillOval(-r/2, -(int)(r*0.36), r, (int)(r*0.72));
                tg.dispose();
            }
            g.setColor(new Color(107, 14, 44));
            g.fillOval(cx - r/3, cy - r/3, r*2/3, r*2/3);
            g.setColor(new Color(198, 155, 80));
            g.fillOval(cx+1, cy-2, 3, 3);
            g.fillOval(cx-3, cy+1, 3, 3);
        }

        private void drawBud(Graphics2D g, int x, int y) {
            g.setColor(new Color(192, 64, 112, 204));
            g.fillOval(x-6, y-8, 12, 16);
            g.setColor(new Color(139, 20, 60));
            g.fillOval(x-4, y-2,  8, 10);
            g.setColor(new Color(90, 138, 58));
            g.fillPolygon(new int[]{x-3,x,x+3}, new int[]{y-8,y-13,y-8}, 3);
            g.fillOval(x-8, y-4, 6, 8);
            g.fillOval(x+2, y-4, 6, 8);
        }

        private void drawLeaf(Graphics2D g, int x, int y, int deg) {
            Graphics2D lg = (Graphics2D) g.create();
            lg.setColor(new Color(90, 138, 58, 216));
            lg.translate(x, y);
            lg.rotate(Math.toRadians(deg));
            lg.fillOval(-12, -5, 24, 10);
            lg.dispose();
        }

        private void sparkle(Graphics2D g, int x, int y, int r) {
            g.fillPolygon(
                new int[]{x, x+r/3, x, x-r/3},
                new int[]{y-r, y, y+r, y}, 4);
        }

        private void heart(Graphics2D g, int x, int y, int size, Color c, boolean fill) {
            g.setColor(c);
            double s = size;
            GeneralPath h = new GeneralPath();
            h.moveTo(x, y + s*0.35);
            h.curveTo(x, y-s*0.25, x-s, y-s*0.25, x-s, y+s*0.1);
            h.curveTo(x-s, y+s*0.6,  x, y+s,       x,   y+s);
            h.curveTo(x,   y+s*0.6, x+s, y+s*0.6, x+s, y+s*0.1);
            h.curveTo(x+s, y-s*0.25, x, y-s*0.25,   x,   y+s*0.35);
            h.closePath();
            if (fill) g.fill(h); else g.draw(h);
        }
    }
}