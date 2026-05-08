/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class ThankYouFrame extends JFrame {

    private final boolean accepted;
    private final String guestName;
    private final String eventName;
    private final String eventDate;
    private final String eventLocation;

    public ThankYouFrame(boolean accepted,
                         String guestName,
                         String eventName,
                         String eventDate,
                         String eventLocation) {

        this.accepted = accepted;
        this.guestName = guestName;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;

        setTitle(accepted ? "Thank You — Invitation Confirmed" : "Thank You");
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

        JLabel titleLbl;
        if (accepted) {
            titleLbl = label("Thank You, " + guestName + "!", Font.BOLD, 26, UITheme.PRIMARY);
        } else {
            titleLbl = label("We Will Miss You,", Font.BOLD, 24, UITheme.PRIMARY);
        }
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub;
        if (accepted) {
            sub = label("Your attendance has been confirmed.", Font.ITALIC, 15, UITheme.TEXT_LIGHT);
        } else {
            sub = label("Thank you for letting us know.", Font.ITALIC, 15, UITheme.TEXT_LIGHT);
        }
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
            center.add(styledRow("Event", eventName));
            center.add(Box.createVerticalStrut(8));
            center.add(styledRow("Date", eventDate));
            center.add(Box.createVerticalStrut(8));
            center.add(styledRow("Venue", eventLocation));
            center.add(Box.createVerticalStrut(12));

            JLabel wish = label("We look forward to celebrating with you!", Font.ITALIC, 14, UITheme.TEXT_LIGHT);
            wish.setAlignmentX(CENTER_ALIGNMENT);
            center.add(wish);
        } else {
            center.add(Box.createVerticalStrut(10));

            JLabel msg1 = label("We completely understand,", Font.PLAIN, 15, UITheme.TEXT);
            JLabel msg2 = label("and we appreciate you taking the time", Font.PLAIN, 15, UITheme.TEXT);
            JLabel msg3 = label("to let us know.", Font.PLAIN, 15, UITheme.TEXT);

            JLabel[] messages = {msg1, msg2, msg3};

            for (JLabel l : messages) {
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
        close.addActionListener(e -> {
            new RoleSelectionFrame().setVisible(true);
            dispose();
        });
        btnPanel.add(close);

        card.add(top, BorderLayout.NORTH);
        card.add(center, BorderLayout.CENTER);
        card.add(btnPanel, BorderLayout.SOUTH);

        main.add(card);
        add(main);
    }

    private void savePdf(JButton btn) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Invitation PDF");
        chooser.setSelectedFile(new File("Invitation_" + guestName.replace(" ", "_") + ".pdf"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();

        if (!target.getName().toLowerCase().endsWith(".pdf")) {
            target = new File(target.getAbsolutePath() + ".pdf");
        }

        btn.setEnabled(false);
        File finalTarget = target;

        new Thread(() -> {
            try {
                writePdf(finalTarget);

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Invitation saved!\n" + finalTarget.getAbsolutePath(),
                            "Saved",
                            JOptionPane.INFORMATION_MESSAGE);
                    btn.setEnabled(true);
                });

            } catch (Exception ex) {
                ex.printStackTrace();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Could not save PDF:\n" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    btn.setEnabled(true);
                });
            }
        }).start();
    }

    private void writePdf(File file) throws IOException {

        String[] lines = {
                "Event Invitation",
                "",
                "Dear " + guestName + ",",
                "",
                "You are cordially invited to",
                eventName,
                "",
                "Date :  " + eventDate,
                "Location :  " + eventLocation,
                "",
                "We look forward to celebrating this",
                "special occasion with you.",
                "",
                "With love and joy,"
        };

        StringBuilder cs = new StringBuilder();

        cs.append("BT\n");
        cs.append("/F1 20 Tf\n");
        cs.append("50 750 Td\n");
        cs.append("(Event Invitation) Tj\n");
        cs.append("/F1 12 Tf\n");
        cs.append("0 -30 Td\n");

        for (int i = 1; i < lines.length; i++) {
            String ln = lines[i]
                    .replace("\\", "\\\\")
                    .replace("(", "\\(")
                    .replace(")", "\\)");

            int dy = ln.isEmpty() ? -10 : -20;

            cs.append("0 ").append(dy).append(" Td\n");
            cs.append("(").append(ln).append(") Tj\n");
        }

        cs.append("ET\n");

        byte[] csBytes = cs.toString().getBytes("ISO-8859-1");

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int[] offsets = new int[10];

        buf.write("%PDF-1.4\n".getBytes("ISO-8859-1"));

        offsets[1] = buf.size();
        buf.write(("1 0 obj\n" +
                "<< /Type /Catalog /Pages 2 0 R >>\n" +
                "endobj\n").getBytes("ISO-8859-1"));

        offsets[2] = buf.size();
        buf.write(("2 0 obj\n" +
                "<< /Type /Pages /Kids [4 0 R] /Count 1 >>\n" +
                "endobj\n").getBytes("ISO-8859-1"));

        offsets[3] = buf.size();
        buf.write(("3 0 obj\n" +
                "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\n" +
                "endobj\n").getBytes("ISO-8859-1"));

        offsets[4] = buf.size();
        buf.write(("4 0 obj\n" +
                "<< /Type /Page /Parent 2 0 R\n" +
                "   /MediaBox [0 0 595 842]\n" +
                "   /Contents 5 0 R\n" +
                "   /Resources << /Font << /F1 3 0 R >> >> >>\n" +
                "endobj\n").getBytes("ISO-8859-1"));

        offsets[5] = buf.size();

        String streamHeader =
                "5 0 obj\n" +
                "<< /Length " + csBytes.length + " >>\n" +
                "stream\n";

        buf.write(streamHeader.getBytes("ISO-8859-1"));
        buf.write(csBytes);
        buf.write("\nendstream\nendobj\n".getBytes("ISO-8859-1"));

        int xrefOffset = buf.size();

        StringBuilder xref = new StringBuilder();
        xref.append("xref\n");
        xref.append("0 6\n");
        xref.append(String.format("%010d 65535 f \n", 0));

        for (int i = 1; i <= 5; i++) {
            xref.append(String.format("%010d 00000 n \n", offsets[i]));
        }

        xref.append("trailer\n");
        xref.append("<< /Size 6 /Root 1 0 R >>\n");
        xref.append("startxref\n");
        xref.append(xrefOffset).append("\n");
        xref.append("%%EOF\n");

        buf.write(xref.toString().getBytes("ISO-8859-1"));

        Files.write(file.toPath(), buf.toByteArray());
    }

    private JPanel buildCard() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

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

        row.add(k);
        row.add(v);

        return row;
    }
}
