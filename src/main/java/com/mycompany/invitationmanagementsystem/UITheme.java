package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import java.awt.*;
import java.awt.geom.*;

public class UITheme {

    public static final Color PRIMARY    = new Color(139, 20,  60);
    public static final Color PRIMARY_LT = new Color(168, 40,  80);
    public static final Color PRIMARY_DK = new Color(100,  8,  38);
    public static final Color CARD       = new Color(255, 252, 250);
    public static final Color TEXT       = new Color( 55, 25,  38);
    public static final Color TEXT_LIGHT = new Color(140, 85, 108);
    public static final Color BORDER     = new Color(210, 162, 178);
    public static final Color GOLD       = new Color(198, 155,  80);
    public static Color       BACKGROUND = new Color(255, 248, 248);

    // ═══════════════════════════════════════════
    //  PILL BUTTON
    // ═══════════════════════════════════════════
    public static void styleButton(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Serif", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(11, 34, 11, 34));
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override public void paint(Graphics g, JComponent c) {
                JButton b = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = b.getWidth(), h = b.getHeight(), arc = h;
                boolean hov = Boolean.TRUE.equals(b.getClientProperty("hov"));
                boolean prs = Boolean.TRUE.equals(b.getClientProperty("prs"));
                Color fill = prs ? PRIMARY_DK : hov ? PRIMARY_LT : PRIMARY;
                g2.setColor(new Color(100, 0, 30, 40));
                g2.fillRoundRect(2, 4, w-2, h-2, arc, arc);
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, w-2, h-2, arc, arc);
                g2.setColor(new Color(255,255,255,25));
                g2.fillRoundRect(5, 2, w-12, h/2, arc, arc);
                g2.setColor(PRIMARY_DK);
                g2.setStroke(new BasicStroke(1.3f));
                g2.drawRoundRect(0, 0, w-3, h-3, arc, arc);
                g2.setFont(b.getFont());
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                String txt = b.getText();
                g2.drawString(txt, (w-fm.stringWidth(txt))/2, (h-fm.getHeight())/2+fm.getAscent());
                g2.dispose();
            }
        });
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered (java.awt.event.MouseEvent e) { btn.putClientProperty("hov",true);  btn.repaint(); }
            public void mouseExited  (java.awt.event.MouseEvent e) { btn.putClientProperty("hov",false); btn.putClientProperty("prs",false); btn.repaint(); }
            public void mousePressed (java.awt.event.MouseEvent e) { btn.putClientProperty("prs",true);  btn.repaint(); }
            public void mouseReleased(java.awt.event.MouseEvent e) { btn.putClientProperty("prs",false); btn.repaint(); }
        });
    }

    // ═══════════════════════════════════════════
    //  ROUNDED CARD
    // ═══════════════════════════════════════════
    public static JPanel createCard(int w, int h) {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(155,75,105,32));
                g2.fillRoundRect(5,7,getWidth()-7,getHeight()-7,32,32);
                g2.setColor(CARD);
                g2.fillRoundRect(0,0,getWidth()-5,getHeight()-5,30,30);
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0,0,getWidth()-6,getHeight()-6,30,30);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(w,h));
        p.setBorder(BorderFactory.createEmptyBorder(28,32,28,32));
        p.setLayout(new GridLayout(0,1,16,16));
        return p;
    }

    // ═══════════════════════════════════════════
    //  STYLED INPUT FIELD
    // ═══════════════════════════════════════════
    public static JTextField createField(String label) {
        JTextField f = new JTextField();
        applyFieldStyle(f, label);
        return f;
    }

    public static JPasswordField createPassField(String label) {
        JPasswordField f = new JPasswordField();
        applyFieldStyle(f, label);
        return f;
    }

    private static void applyFieldStyle(JTextField f, String label) {
        f.setFont(new Font("Serif", Font.PLAIN, 16));
        f.setForeground(TEXT);
        f.setBackground(new Color(255,250,252));
        f.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(BorderFactory.createLineBorder(BORDER,1,true),
                " "+label+" ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif",Font.PLAIN,11), TEXT_LIGHT),
            BorderFactory.createEmptyBorder(2,8,4,8)));
    }

    // ═══════════════════════════════════════════
    //  STYLED COMBOBOX — متوافق مع macOS ✅
    // ═══════════════════════════════════════════
    public static JComboBox<String> createComboBox(String label) {
        JComboBox<String> cb = new JComboBox<>();
        styleComboBox(cb, label);
        return cb;
    }

    public static void styleComboBox(JComboBox<String> cb, String label) {
        // إجبار macOS على استخدام BasicUI بدل AquaUI
        cb.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(new Color(255, 245, 248));
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        // رسم سهم لأسفل
                        g2.setColor(PRIMARY);
                        int cx = getWidth() / 2;
                        int cy = getHeight() / 2;
                        int[] xp = {cx - 5, cx + 5, cx};
                        int[] yp = {cy - 3, cy - 3, cy + 3};
                        g2.fillPolygon(xp, yp, 3);
                        g2.dispose();
                    }
                };
                btn.setOpaque(false);
                btn.setContentAreaFilled(false);
                btn.setBorderPainted(false);
                btn.setFocusPainted(false);
                btn.setPreferredSize(new Dimension(30, 30));
                return btn;
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 248, 251));
                g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                g2.dispose();
            }
        });

        cb.setFont(new Font("Serif", Font.PLAIN, 15));
        cb.setForeground(TEXT);
        cb.setBackground(new Color(255, 248, 251));
        cb.setOpaque(true);
        cb.setPreferredSize(new Dimension(200, 42));
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        // Renderer للـ items داخل القائمة
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                lbl.setFont(new Font("Serif", Font.PLAIN, 15));
                lbl.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                if (isSelected) {
                    lbl.setBackground(new Color(220, 160, 180));
                    lbl.setForeground(Color.WHITE);
                } else {
                    lbl.setBackground(Color.WHITE);
                    lbl.setForeground(TEXT);
                }
                return lbl;
            }
        });

        // Border مع عنوان
        cb.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                " " + label + " ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.PLAIN, 11), TEXT_LIGHT),
            BorderFactory.createEmptyBorder(2, 4, 4, 4)));
    }

    // ═══════════════════════════════════════════
    //  STYLED TABLE — موحد ومتناسق ✅
    // ═══════════════════════════════════════════
    public static void styleTable(JTable table) {
        table.setFont(new Font("Serif", Font.PLAIN, 15));
        table.setForeground(TEXT);
        table.setBackground(new Color(255, 252, 254));
        table.setRowHeight(36);
        table.setGridColor(new Color(225, 190, 205));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(190, 90, 130));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0
                        ? new Color(255, 250, 252)
                        : new Color(250, 240, 245));
                    c.setForeground(TEXT);
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });

        // Header styling
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = new JLabel(val != null ? val.toString() : "", SwingConstants.LEFT);
                lbl.setFont(new Font("Serif", Font.BOLD, 14));
                lbl.setForeground(Color.WHITE);
                lbl.setOpaque(true);
                lbl.setBackground(PRIMARY);
                lbl.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
                return lbl;
            }
        });
        table.getTableHeader().setPreferredSize(new Dimension(0, 44));
        table.getTableHeader().setResizingAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static JScrollPane createStyledScroll(JTable table) {
        styleTable(table);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getViewport().setBackground(new Color(255, 252, 254));
        scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(0, 16, 0, 16),
            BorderFactory.createLineBorder(BORDER, 1, true)));

        // Custom scrollbar
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = new Color(190, 120, 150);
                trackColor = new Color(245, 232, 238);
            }
            @Override protected JButton createDecreaseButton(int o) {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
            @Override protected JButton createIncreaseButton(int o) {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0,0)); return b;
            }
        });
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        return scroll;
    }

    // ═══════════════════════════════════════════
    //  PAGE HEADER
    // ═══════════════════════════════════════════
    public static JPanel createHeader(String titleText) {
        JPanel p = new JPanel(new BorderLayout(0,6));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(22,0,10,0));
        JLabel t = new JLabel(titleText, SwingConstants.CENTER);
        t.setFont(new Font("Serif", Font.BOLD, 28));
        t.setForeground(TEXT);
        JLabel div = new JLabel("✦  ─────────────────────────────────────  ✦", SwingConstants.CENTER);
        div.setFont(new Font("Serif", Font.PLAIN, 13));
        div.setForeground(new Color(185,125,148));
        p.add(t,   BorderLayout.CENTER);
        p.add(div, BorderLayout.SOUTH);
        return p;
    }

    // ═══════════════════════════════════════════
    //  BUTTON BAR
    // ═══════════════════════════════════════════
    public static JPanel createButtonBar(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER,18,10));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
        for (JButton b : btns) { styleButton(b); p.add(b); }
        return p;
    }

    // ═══════════════════════════════════════════
    //  ROSE BACKGROUND
    // ═══════════════════════════════════════════
    public static JPanel createRoseBackground() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0,0,new Color(255,242,246),getWidth(),getHeight(),new Color(250,228,238)));
                g2.fillRect(0,0,getWidth(),getHeight());
                int W=getWidth(), H=getHeight();
                int[][] pos={{28,28},{W-75,18},{12,H-75},{W-68,H-75},{W/2-55,12},{W/2+38,H-68},{55,H/2-38},{W-88,H/2+18}};
                int[]   sz ={54,48,52,46,50,48,46,52};
                float[] al ={.28f,.22f,.25f,.20f,.24f,.22f,.20f,.26f};
                for (int i=0;i<pos.length;i++) rose(g2,pos[i][0],pos[i][1],sz[i],al[i]);
                int[][] sm={{W/4,6},{3*W/4,H-38},{8,H/3},{W-28,2*H/3}};
                for (int[] p:sm) rose(g2,p[0],p[1],28,.15f);
                g2.dispose();
            }
            private void rose(Graphics2D g2,int cx,int cy,int r,float a) {
                Graphics2D g=(Graphics2D)g2.create();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,a));
                for (int i=0;i<5;i++) {
                    double ang=Math.toRadians(i*72)-Math.PI/2;
                    int px=(int)(cx+r*.55*Math.cos(ang)),py=(int)(cy+r*.55*Math.sin(ang));
                    int pw=(int)(r*.75),ph=(int)(r*.55);
                    g.setPaint(new GradientPaint(px,py,new Color(208,52,92),px+pw/2,py+ph/2,new Color(236,112,142)));
                    g.fill(AffineTransform.getRotateInstance(ang,px,py).createTransformedShape(new Ellipse2D.Double(px-pw/2.,py-ph/2.,pw,ph)));
                }
                for (int i=0;i<5;i++) {
                    double ang=Math.toRadians(i*72);
                    int px=(int)(cx+r*.28*Math.cos(ang)),py=(int)(cy+r*.28*Math.sin(ang));
                    int pw=(int)(r*.50),ph=(int)(r*.38);
                    g.setColor(new Color(216,72,108));
                    g.fill(AffineTransform.getRotateInstance(ang,px,py).createTransformedShape(new Ellipse2D.Double(px-pw/2.,py-ph/2.,pw,ph)));
                }
                int cr=(int)(r*.22); g.setColor(new Color(172,32,72)); g.fillOval(cx-cr,cy-cr,cr*2,cr*2);
                g.dispose();
            }
        };
    }
}