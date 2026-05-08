package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class ThankYouFrame extends JFrame {

    private final boolean accepted;
    private final String  guestName;
    private final String  eventName;
    private final String  eventDate;
    private final String  eventLocation;

    public ThankYouFrame(boolean accepted,
                         String guestName,
                         String eventName,
                         String eventDate,
                         String eventLocation) {

        this.accepted      = accepted;
        this.guestName     = guestName;
        this.eventName     = eventName;
        this.eventDate     = eventDate;
        this.eventLocation = eventLocation;

        setTitle(accepted ? "Invitation Confirmed" : "Thank You");
        setSize(660, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new GridBagLayout());

        JPanel card = buildCard();
        card.setPreferredSize(new Dimension(520, 370));
        card.setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(36, 50, 6, 50));

        JLabel divTop = goldDivider();
        divTop.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLbl = accepted
            ? label("Thank You", Font.BOLD, 26, UITheme.PRIMARY)
            : label("We Will Miss You,", Font.BOLD, 24, UITheme.PRIMARY);
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = accepted
            ? label("Your attendance has been confirmed.", Font.ITALIC, 15, UITheme.TEXT_LIGHT)
            : label("Thank you for letting us know.", Font.ITALIC, 15, UITheme.TEXT_LIGHT);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        top.add(divTop);
        top.add(Box.createVerticalStrut(8));
        top.add(titleLbl);
        top.add(Box.createVerticalStrut(6));
        top.add(sub);

        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(14, 55, 6, 55));

        if (accepted) {
            center.add(styledRow("Event",    eventName));
            center.add(Box.createVerticalStrut(8));
            center.add(styledRow("Date",     eventDate));
            center.add(Box.createVerticalStrut(8));
            center.add(styledRow("Location", eventLocation));
            center.add(Box.createVerticalStrut(12));
            JLabel wish = label("We look forward to celebrating with you!", Font.ITALIC, 14, UITheme.TEXT_LIGHT);
            wish.setAlignmentX(CENTER_ALIGNMENT);
            center.add(wish);
        } else {
            center.add(Box.createVerticalStrut(10));
            for (String msg : new String[]{
                "We completely understand,",
                "and we appreciate you taking the time",
                "to let us know."}) {
                JLabel l = label(msg, Font.PLAIN, 15, UITheme.TEXT);
                l.setAlignmentX(CENTER_ALIGNMENT);
                center.add(l);
                center.add(Box.createVerticalStrut(5));
            }
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 18, 14));
        btnPanel.setOpaque(false);

        if (accepted) {
            JButton downloadPdf = new JButton("Download Invitation as PDF");
            UITheme.styleButton(downloadPdf);
            downloadPdf.addActionListener(e -> savePdf(downloadPdf));
            btnPanel.add(downloadPdf);
        }

        JButton close = new JButton("Close");
        UITheme.styleButton(close);
        close.addActionListener(e -> { new RoleSelectionFrame().setVisible(true); dispose(); });
        btnPanel.add(close);

        card.add(top,      BorderLayout.NORTH);
        card.add(center,   BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);
    }

    // =========================================================
    //  Save PDF
    // =========================================================
    private void savePdf(JButton btn) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Invitation PDF");
        chooser.setSelectedFile(new File("Invitation_" + guestName.replace(" ", "_") + ".pdf"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File target = chooser.getSelectedFile();
        if (!target.getName().toLowerCase().endsWith(".pdf"))
            target = new File(target.getAbsolutePath() + ".pdf");

        btn.setEnabled(false);
        final File finalTarget = target;

        new Thread(() -> {
            try {
                writePdf(finalTarget);
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Invitation saved!\n" + finalTarget.getAbsolutePath(),
                        "Saved", JOptionPane.INFORMATION_MESSAGE);
                    btn.setEnabled(true);
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                        "Could not save PDF:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                    btn.setEnabled(true);
                });
            }
        }).start();
    }

    // =========================================================
    //  PDF Writer — styled invitation card
    // =========================================================
    private void writePdf(File file) throws IOException {

        StringBuilder cs = new StringBuilder();

        // ── Page dimensions: A4 = 595 x 842 pt ──────────────────────────
        // Outer gold border
        cs.append("1.5 w\n");                                  // line width
        cs.append("0.776 0.608 0.314 RG\n");                   // gold stroke
        cs.append("30 40 535 762 re S\n");                     // outer rect
        cs.append("0.949 0.988 0.965 RG\n");
        cs.append("36 46 523 750 re S\n");                     // inner rect

        // ── Top colored band ──────────────────────────────────────────────
        cs.append("0.545 0.078 0.235 rg\n");                   // dark rose fill
        cs.append("30 760 535 42 re f\n");

        // ── Decorative tag line ──────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 11 Tf\n");
        cs.append("1 1 1 rg\n");                               // white text
        appendCenteredText(cs, "- Event Invitation -", 595, 786, 11);
        cs.append("ET\n");

        // ── Main title ────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F1 26 Tf\n");
        cs.append("0.545 0.078 0.235 rg\n");
        appendCenteredText(cs, "You Are Cordially Invited", 595, 720, 26);
        cs.append("ET\n");

        // ── Gold divider line ────────────────────────────────────────────
        cs.append("1 w\n");
        cs.append("0.776 0.608 0.314 RG\n");
        cs.append("80 705 435 705 m 515 705 l S\n");

        // ── Dear guest ────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 13 Tf\n");
        cs.append("0.216 0.098 0.149 rg\n");
        cs.append("80 680 Td\n");
        appendLine(cs, "Dear " + guestName + ",");
        cs.append("ET\n");

        // ── Invitation phrase ─────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 12 Tf\n");
        cs.append("0.216 0.098 0.149 rg\n");
        cs.append("80 655 Td\n");
        appendLine(cs, "We are delighted to invite you to attend");
        cs.append("ET\n");

        // ── Event name ────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F1 18 Tf\n");
        cs.append("0.627 0.392 0.157 rg\n");
        appendCenteredText(cs, eventName, 595, 625, 18);
        cs.append("ET\n");

        // ── Second gold divider ───────────────────────────────────────────
        cs.append("0.5 w\n");
        cs.append("0.776 0.608 0.314 RG\n");
        cs.append("150 612 m 445 612 l S\n");

        // ── Event details ─────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F1 12 Tf\n");
        cs.append("0.216 0.098 0.149 rg\n");
        cs.append("80 590 Td\n");
        appendLine(cs, "Date     :   " + eventDate);
        cs.append("0 -22 Td\n");
        appendLine(cs, "Venue    :   " + eventLocation);
        cs.append("ET\n");

        // ── Thin separator ───────────────────────────────────────────────
        cs.append("0.3 w\n");
        cs.append("0.824 0.635 0.698 RG\n");
        cs.append("80 555 m 515 555 l S\n");

        // ── RSVP note ─────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 11 Tf\n");
        cs.append("0.549 0.333 0.424 rg\n");
        appendCenteredText(cs, "Please confirm your attendance using RSVP.", 595, 535, 11);
        cs.append("ET\n");

        // ── Wish line ─────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 11 Tf\n");
        cs.append("0.549 0.333 0.424 rg\n");
        appendCenteredText(cs, "We look forward to celebrating with you!", 595, 515, 11);
        cs.append("ET\n");

        // ── Closing ───────────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F1 12 Tf\n");
        cs.append("0.216 0.098 0.149 rg\n");
        cs.append("80 480 Td\n");
        appendLine(cs, "With love and joy,");
        cs.append("0 -20 Td\n");
        cs.append("/F1 13 Tf\n");
        appendLine(cs, "Invitation Management System");
        cs.append("ET\n");

        // ── Bottom band ───────────────────────────────────────────────────
        cs.append("0.545 0.078 0.235 rg\n");
        cs.append("30 40 535 18 re f\n");

        // ── Footer text ───────────────────────────────────────────────────
        cs.append("BT\n");
        cs.append("/F2 9 Tf\n");
        cs.append("1 1 1 rg\n");
        appendCenteredText(cs, "Invitation Management System  |  Generated automatically", 595, 52, 9);
        cs.append("ET\n");

        // ── Build PDF binary ──────────────────────────────────────────────
        byte[] csBytes = cs.toString().getBytes("ISO-8859-1");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int[] off = new int[10];

        buf.write("%PDF-1.4\n".getBytes("ISO-8859-1"));

        off[1] = buf.size();
        write(buf, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        off[2] = buf.size();
        write(buf, "2 0 obj\n<< /Type /Pages /Kids [4 0 R] /Count 1 >>\nendobj\n");

        // Two fonts: F1 = Helvetica-Bold, F2 = Helvetica
        off[3] = buf.size();
        write(buf, "3 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");

        off[6] = buf.size();
        write(buf, "6 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

        off[4] = buf.size();
        write(buf, "4 0 obj\n<< /Type /Page /Parent 2 0 R\n"
            + "   /MediaBox [0 0 595 842]\n   /Contents 5 0 R\n"
            + "   /Resources << /Font << /F1 3 0 R /F2 6 0 R >> >> >>\nendobj\n");

        off[5] = buf.size();
        write(buf, "5 0 obj\n<< /Length " + csBytes.length + " >>\nstream\n");
        buf.write(csBytes);
        write(buf, "\nendstream\nendobj\n");

        int xOff = buf.size();
        // xref for objects 0-6
        StringBuilder xref = new StringBuilder();
        xref.append("xref\n0 7\n").append(String.format("%010d 65535 f \n", 0));
        for (int i = 1; i <= 6; i++) xref.append(String.format("%010d 00000 n \n", off[i]));
        xref.append("trailer\n<< /Size 7 /Root 1 0 R >>\nstartxref\n")
            .append(xOff).append("\n%%EOF\n");
        buf.write(xref.toString().getBytes("ISO-8859-1"));

        Files.write(file.toPath(), buf.toByteArray());
    }

    /** Append a simple text-show operator */
    private void appendLine(StringBuilder cs, String text) {
        cs.append("(").append(escapePdf(text)).append(") Tj\n");
    }

    /**
     * Approximate center-alignment: measure string width roughly
     * (each glyph ≈ fontSize * 0.55 pt for Helvetica) and offset Td.
     */
    private void appendCenteredText(StringBuilder cs, String text, int pageW, int y, int fontSize) {
        double approxWidth = text.length() * fontSize * 0.52;
        int x = (int) ((pageW - approxWidth) / 2.0);
        if (x < 40) x = 40;
        cs.append(x).append(" ").append(y).append(" Td\n");
        cs.append("(").append(escapePdf(text)).append(") Tj\n");
    }

    private String escapePdf(String s) {
        if (s == null) s = "";
        return s.replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

    private void write(ByteArrayOutputStream buf, String s) throws IOException {
        buf.write(s.getBytes("ISO-8859-1"));
    }

    // =========================================================
    //  UI helpers
    // =========================================================
    private JPanel buildCard() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(120, 40, 70, 45));
                g2.fillRoundRect(6, 8, w - 8, h - 8, 34, 34);
                g2.setColor(new Color(255, 252, 246));
                g2.fillRoundRect(0, 0, w - 6, h - 6, 32, 32);
                g2.setColor(new Color(198, 155, 80));
                g2.setStroke(new BasicStroke(2f));
                g2.drawRoundRect(4, 4, w - 14, h - 14, 28, 28);
                g2.setColor(new Color(220, 185, 110, 110));
                g2.setStroke(new BasicStroke(0.9f));
                g2.drawRoundRect(10, 10, w - 26, h - 26, 22, 22);
                g2.dispose();
            }
        };
    }

    private JLabel label(String text, int style, int size, Color color) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Serif", style, size));
        l.setForeground(color);
        return l;
    }

    private JLabel goldDivider() {
        JLabel l = new JLabel("✦  ─────────────────────────────  ✦", SwingConstants.CENTER);
        l.setFont(new Font("Serif", Font.PLAIN, 13));
        l.setForeground(new Color(198, 155, 80));
        return l;
    }

    private JPanel styledRow(String lbl, String val) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        row.setOpaque(false);
        JLabel k = new JLabel(lbl + " : ");
        k.setFont(new Font("Serif", Font.BOLD, 14));
        k.setForeground(UITheme.TEXT);
        JLabel v = new JLabel(val);
        v.setFont(new Font("Serif", Font.PLAIN, 14));
        v.setForeground(new Color(160, 100, 40));
        row.add(k); row.add(v);
        return row;
    }
}
