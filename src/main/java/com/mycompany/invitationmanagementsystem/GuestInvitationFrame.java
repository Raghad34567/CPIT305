package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class GuestInvitationFrame extends JFrame {

    String guestName;
    int    eventId;

    public GuestInvitationFrame(String guestName, int eventId) {
        this.guestName = guestName;
        this.eventId   = eventId;

        setTitle("Wedding Invitation");
        setSize(820, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        // ── Luxury invitation card ──────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();

                // outer shadow
                g2.setColor(new Color(120, 40, 70, 50));
                g2.fillRoundRect(7, 9, w - 9, h - 9, 36, 36);

                // ivory parchment body
                g2.setColor(new Color(255, 252, 246));
                g2.fillRoundRect(0, 0, w - 7, h - 7, 34, 34);

                // gold outer border
                g2.setColor(new Color(198, 155, 80));
                g2.setStroke(new BasicStroke(2.4f));
                g2.drawRoundRect(4, 4, w - 15, h - 15, 30, 30);

                // gold inner border
                g2.setColor(new Color(220, 185, 110, 130));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(11, 11, w - 29, h - 29, 24, 24);

                // corner ornaments (4 corners)
                drawCorner(g2, 20, 20, 0);
                drawCorner(g2, w - 28, 20, 90);
                drawCorner(g2, w - 28, h - 28, 180);
                drawCorner(g2, 20, h - 28, 270);

                // subtle center divider watermark
                g2.setColor(new Color(198, 155, 80, 22));
                g2.setStroke(new BasicStroke(0.8f));
                int mid = h / 2;
                g2.drawLine(55, mid, w - 62, mid);

                g2.dispose();
            }

            private void drawCorner(Graphics2D g2, int x, int y, int deg) {
                Graphics2D g = (Graphics2D) g2.create();
                g.setColor(new Color(198, 155, 80, 220));
                g.setStroke(new BasicStroke(1.8f));
                g.translate(x, y);
                g.rotate(Math.toRadians(deg));
                g.drawLine(0, 0, 22, 0);
                g.drawLine(0, 0, 0, 22);
                // small diamond dot
                int[] xs = {0, 5, 0, -5}, ys = {-5, 0, 5, 0};
                g.setColor(new Color(198, 155, 80));
                g.fillPolygon(xs, ys, 4);
                g.dispose();
            }
        };
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(630, 560));
        card.setLayout(new BorderLayout());

        // ── TOP SECTION ─────────────────────────────
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 6, 50));

        JLabel tag = new JLabel("— Wedding Invitation —", SwingConstants.CENTER);
        tag.setFont(new Font("Serif", Font.ITALIC, 13));
        tag.setForeground(new Color(180, 135, 75));
        tag.setAlignmentX(CENTER_ALIGNMENT);

        JLabel mainTitle = new JLabel("You Are Cordially Invited", SwingConstants.CENTER);
        mainTitle.setFont(new Font("Serif", Font.BOLD, 30));
        mainTitle.setForeground(UITheme.PRIMARY);
        mainTitle.setAlignmentX(CENTER_ALIGNMENT);
        mainTitle.setBorder(BorderFactory.createEmptyBorder(7, 0, 4, 0));

        JLabel goldDiv = new JLabel("✦  ─────────────────────────  ✦", SwingConstants.CENTER);
        goldDiv.setFont(new Font("Serif", Font.PLAIN, 14));
        goldDiv.setForeground(new Color(198, 155, 80));
        goldDiv.setAlignmentX(CENTER_ALIGNMENT);

        topPanel.add(tag);
        topPanel.add(mainTitle);
        topPanel.add(goldDiv);

        // ── CENTER CONTENT ───────────────────────────
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(16, 65, 10, 65));

        Object[][] lines = {
            // {text, style}  style: 0=plain, 1=names, 2=bold, 3=italic-light, 4=space
            {"Together with their families",                   0},
            {"",                                               4},
            {"Bride  &  Groom",                                1},
            {"",                                               4},
            {"request the pleasure of your company",           0},
            {"at their wedding celebration",                   0},
            {"",                                               4},
            {"Date :   20 May 2026",                           2},
            {"Venue :  Grand Royal Hall",                      2},
            {"",                                               4},
            {"Your presence will make our day truly special.", 3},
        };

        for (Object[] row : lines) {
            String txt   = (String) row[0];
            int    style = (int)    row[1];

            if (style == 4) {
                centerPanel.add(Box.createVerticalStrut(6));
                continue;
            }

            JLabel l = new JLabel(txt, SwingConstants.CENTER);
            l.setAlignmentX(CENTER_ALIGNMENT);

            switch (style) {
                case 1: // Bride & Groom
                    l.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
                    l.setForeground(UITheme.PRIMARY);
                    l.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
                    break;
                case 2: // date / venue bold
                    l.setFont(new Font("Serif", Font.BOLD, 15));
                    l.setForeground(UITheme.TEXT);
                    break;
                case 3: // italic light
                    l.setFont(new Font("Serif", Font.ITALIC, 14));
                    l.setForeground(UITheme.TEXT_LIGHT);
                    break;
                default: // plain
                    l.setFont(new Font("Serif", Font.PLAIN, 15));
                    l.setForeground(UITheme.TEXT);
            }
            centerPanel.add(l);
        }

        // ── BUTTONS ──────────────────────────────────
        JButton rsvp = new JButton("RSVP");
        JButton back = new JButton("Back");
        UITheme.styleButton(rsvp);
        UITheme.styleButton(back);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 16));
        btnPanel.setOpaque(false);
        btnPanel.add(rsvp);
        btnPanel.add(back);

        card.add(topPanel,    BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(btnPanel,    BorderLayout.SOUTH);

        main.add(card);
        add(main);

        // ── UNCHANGED LOGIC ──────────────────────────
        rsvp.addActionListener(e -> {
            new RSVPFrame(this, guestName, eventId).setVisible(true);
            setVisible(false);
        });

        back.addActionListener(e -> {
            new InvitationLinkFrame().setVisible(true);
            dispose();
        });
    }
}