package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

/*
 * This frame shows event statistics and generates reports.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class ReportsFrame extends JFrame {

    DashboardFrame dashboard;
    JComboBox<String> eventBox;
    int selectedEventId = -1;

    JLabel totalVal, acceptedVal, declinedVal;

    public ReportsFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;

        // Set frame title
        setTitle("Event Reports & Statistics");

        // Set frame size
        setSize(900, 560);

        // Open frame in center of screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        // North: header + event selector
        JPanel northWrapper = new JPanel(new BorderLayout(0, 0));
        northWrapper.setOpaque(false);
        northWrapper.add(UITheme.createHeader("Event Reports Overview"), BorderLayout.NORTH);

        eventBox = UITheme.createComboBox("Select Event");

        // Show more events in the combo box list
        eventBox.setMaximumRowCount(20);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);

        northWrapper.add(topPanel, BorderLayout.SOUTH);
        main.add(northWrapper, BorderLayout.NORTH);

        // Center: 3 stat cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        totalVal = new JLabel("—", SwingConstants.CENTER);
        acceptedVal = new JLabel("—", SwingConstants.CENTER);
        declinedVal = new JLabel("—", SwingConstants.CENTER);

        statsPanel.add(createStatCard("Total Guests", totalVal, new Color(139, 20, 60)));
        statsPanel.add(createStatCard("Accepted", acceptedVal, new Color(34, 139, 34)));
        statsPanel.add(createStatCard("Declined", declinedVal, new Color(180, 50, 50)));

        main.add(statsPanel, BorderLayout.CENTER);

        // South: buttons
        JButton refresh = new JButton("Refresh");
        JButton exportPdf = new JButton("Export as PDF");
        JButton back = new JButton("Back");

        UITheme.styleButton(refresh);
        UITheme.styleButton(exportPdf);
        UITheme.styleButton(back);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        bottomPanel.add(refresh);
        bottomPanel.add(exportPdf);
        bottomPanel.add(back);

        main.add(bottomPanel, BorderLayout.SOUTH);
        add(main);

        loadEvents();

        eventBox.addActionListener(e -> loadStats());

        refresh.addActionListener(e -> loadStats());

        back.addActionListener(e -> {
            dashboard.setVisible(true);
            dispose();
        });

        exportPdf.addActionListener(e -> {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Please select an event first.");
                return;
            }

            doExportPdf(exportPdf);
        });
    }

    private void loadEvents() {
        try {
            Connection conn = DBConnection.connect();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(
                    "SELECT id, name FROM events ORDER BY id ASC");

            eventBox.removeAllItems();

            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            }

            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadStats();
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading events!");
        }
    }

    private void loadStats() {
        if (eventBox.getSelectedItem() == null) {
            return;
        }

        selectedEventId = Integer.parseInt(
                eventBox.getSelectedItem().toString().split(":")[0].trim());

        try {
            Connection conn = DBConnection.connect();

            PreparedStatement ps1 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM guests WHERE event_id=?");
            ps1.setInt(1, selectedEventId);
            ResultSet rs1 = ps1.executeQuery();
            int total = rs1.next() ? rs1.getInt(1) : 0;

            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM guests WHERE event_id=? AND response LIKE '%Accept%'");
            ps2.setInt(1, selectedEventId);
            ResultSet rs2 = ps2.executeQuery();
            int accepted = rs2.next() ? rs2.getInt(1) : 0;

            PreparedStatement ps3 = conn.prepareStatement(
                    "SELECT COUNT(*) FROM guests WHERE event_id=? AND response LIKE '%Decline%'");
            ps3.setInt(1, selectedEventId);
            ResultSet rs3 = ps3.executeQuery();
            int declined = rs3.next() ? rs3.getInt(1) : 0;

            rs1.close();
            rs2.close();
            rs3.close();

            ps1.close();
            ps2.close();
            ps3.close();

            conn.close();

            totalVal.setText(String.valueOf(total));
            acceptedVal.setText(String.valueOf(accepted));
            declinedVal.setText(String.valueOf(declined));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading stats!");
        }
    }

    private JPanel createStatCard(String label, JLabel valueLabel, Color accentColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.setColor(new Color(155, 75, 105, 30));
                g2.fillRoundRect(5, 7, w - 7, h - 7, 32, 32);

                g2.setColor(new Color(255, 252, 250));
                g2.fillRoundRect(0, 0, w - 5, h - 5, 30, 30);

                // colored top bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, w - 5, 10, 30, 30);
                g2.fillRect(0, 5, w - 5, 8);

                g2.setColor(UITheme.BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w - 6, h - 6, 30, 30);

                g2.dispose();
            }
        };

        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(28, 20, 20, 20));

        JLabel titleLbl = new JLabel(label, SwingConstants.CENTER);
        titleLbl.setFont(new Font("Serif", Font.BOLD, 18));
        titleLbl.setForeground(UITheme.TEXT);
        titleLbl.setAlignmentX(CENTER_ALIGNMENT);

        valueLabel.setFont(new Font("Serif", Font.BOLD, 46));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalStrut(8));
        card.add(titleLbl);
        card.add(Box.createVerticalStrut(14));
        card.add(valueLabel);

        return card;
    }

    // Export PDF
    private void doExportPdf(JButton btn) {
        String eventName = "";
        String eventDate = "";
        String eventLoc = "";

        java.util.List<String[]> guests = new java.util.ArrayList<>();

        try {
            Connection conn = DBConnection.connect();

            PreparedStatement ep = conn.prepareStatement(
                    "SELECT name, date, location FROM events WHERE id=?");
            ep.setInt(1, selectedEventId);
            ResultSet er = ep.executeQuery();

            if (er.next()) {
                eventName = er.getString("name");
                eventDate = er.getString("date");
                eventLoc = er.getString("location");
            }

            PreparedStatement gp = conn.prepareStatement(
                    "SELECT name, response, guest_count FROM guests WHERE event_id=?");
            gp.setInt(1, selectedEventId);
            ResultSet gr = gp.executeQuery();

            while (gr.next()) {
                String resp = gr.getString("response");

                if (resp == null || resp.trim().isEmpty()) {
                    resp = "No Response";
                }

                guests.add(new String[]{
                    gr.getString("name"),
                    resp,
                    String.valueOf(gr.getInt("guest_count"))
                });
            }

            er.close();
            gr.close();
            ep.close();
            gp.close();
            conn.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Report PDF");
        chooser.setSelectedFile(new File("Report_" + eventName.replace(" ", "_") + ".pdf"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();

        if (!target.getName().toLowerCase().endsWith(".pdf")) {
            target = new File(target.getAbsolutePath() + ".pdf");
        }

        btn.setEnabled(false);

        final File ft = target;
        final String fn = eventName;
        final String fd = eventDate;
        final String fl = eventLoc;
        final String tot = totalVal.getText();
        final String acc = acceptedVal.getText();
        final String dec = declinedVal.getText();

        new Thread(() -> {
            try {
                writePdf(ft, fn, fd, fl, tot, acc, dec, guests);

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this,
                            "Report saved!\n" + ft.getAbsolutePath(),
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

    private void writePdf(File file,
                          String evName,
                          String evDate,
                          String evLoc,
                          String total,
                          String accepted,
                          String declined,
                          java.util.List<String[]> guestList) throws IOException {

        StringBuilder cs = new StringBuilder();

        cs.append("1.5 w\n");
        cs.append("0.776 0.608 0.314 RG\n");
        cs.append("30 40 535 762 re S\n");
        cs.append("36 46 523 750 re S\n");

        cs.append("0.545 0.078 0.235 rg\n");
        cs.append("30 760 535 42 re f\n");

        cs.append("BT\n/F2 11 Tf\n1 1 1 rg\n");
        appendCentered(cs, "- Event Statistics Report -", 595, 786, 11);
        cs.append("ET\n");

        cs.append("BT\n/F1 26 Tf\n0.545 0.078 0.235 rg\n");
        appendCentered(cs, "Event Report", 595, 718, 26);
        cs.append("ET\n");

        cs.append("1 w\n0.776 0.608 0.314 RG\n");
        cs.append("60 703 m 535 703 l S\n");

        cs.append("BT\n/F1 13 Tf\n0.545 0.078 0.235 rg\n60 683 Td\n");
        appendLine(cs, "Event Details");
        cs.append("ET\n");

        cs.append("0.4 w\n0.824 0.635 0.698 RG\n60 678 m 535 678 l S\n");

        cs.append("BT\n/F2 12 Tf\n0.216 0.098 0.149 rg\n60 660 Td\n");
        appendLine(cs, "Event  :   " + evName);
        cs.append("0 -20 Td\n");
        appendLine(cs, "Date   :   " + evDate);
        cs.append("0 -20 Td\n");
        appendLine(cs, "Location  :   " + evLoc);
        cs.append("ET\n");

        cs.append("BT\n/F1 13 Tf\n0.545 0.078 0.235 rg\n60 593 Td\n");
        appendLine(cs, "Statistics");
        cs.append("ET\n");

        cs.append("0.4 w\n0.824 0.635 0.698 RG\n60 588 m 535 588 l S\n");

        drawStatBox(cs, 60, 530, "Total Guests", total, "0.545 0.078 0.235");
        drawStatBox(cs, 220, 530, "Accepted", accepted, "0.133 0.545 0.133");
        drawStatBox(cs, 380, 530, "Declined", declined, "0.706 0.196 0.196");

        cs.append("BT\n/F1 13 Tf\n0.545 0.078 0.235 rg\n60 430 Td\n");
        appendLine(cs, "Guest Details");
        cs.append("ET\n");

        cs.append("0.4 w\n0.824 0.635 0.698 RG\n60 425 m 535 425 l S\n");

        cs.append("0.545 0.078 0.235 rg\n60 400 475 22 re f\n");

        cs.append("BT\n/F1 11 Tf\n1 1 1 rg\n");
        cs.append("68 408 Td\n");
        appendLine(cs, "Name");
        cs.append("280 408 Td\n");
        appendLine(cs, "Response");
        cs.append("420 408 Td\n");
        appendLine(cs, "Companions");
        cs.append("ET\n");

        int y = 398;
        int rowIdx = 0;

        for (String[] row : guestList) {
            y -= 22;

            if (y < 65) {
                break;
            }

            if (rowIdx % 2 == 0) {
                cs.append("0.996 0.973 0.980 rg\n");
            } else {
                cs.append("0.988 0.945 0.957 rg\n");
            }

            cs.append("60 ").append(y - 4).append(" 475 22 re f\n");

            String resp = (row[1] != null && row[1].contains("Accept")) ? "Accepted" : "Declined";
            String respColor = resp.equals("Accepted") ? "0.133 0.545 0.133" : "0.706 0.196 0.196";
            String companions = row[2];

            cs.append("BT\n/F2 10 Tf\n0.216 0.098 0.149 rg\n");
            cs.append("68 ").append(y + 3).append(" Td\n");
            appendLine(cs, truncate(row[0], 28));
            cs.append("ET\n");

            cs.append("BT\n/F1 10 Tf\n").append(respColor).append(" rg\n");
            cs.append("280 ").append(y + 3).append(" Td\n");
            appendLine(cs, resp);
            cs.append("ET\n");

            cs.append("BT\n/F2 10 Tf\n0.216 0.098 0.149 rg\n");
            cs.append("420 ").append(y + 3).append(" Td\n");
            appendLine(cs, companions);
            cs.append("ET\n");

            cs.append("0.2 w\n0.824 0.635 0.698 RG\n");
            cs.append("60 ").append(y - 4).append(" m 535 ").append(y - 4).append(" l S\n");

            rowIdx++;
        }

        cs.append("0.545 0.078 0.235 rg\n30 40 535 18 re f\n");

        cs.append("BT\n/F2 9 Tf\n1 1 1 rg\n");
        appendCentered(cs, "Invitation Management System  |  Generated automatically", 595, 52, 9);
        cs.append("ET\n");

        byte[] csBytes = cs.toString().getBytes("ISO-8859-1");
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int[] off = new int[10];

        buf.write("%PDF-1.4\n".getBytes("ISO-8859-1"));

        off[1] = buf.size();
        write(buf, "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

        off[2] = buf.size();
        write(buf, "2 0 obj\n<< /Type /Pages /Kids [4 0 R] /Count 1 >>\nendobj\n");

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

        StringBuilder xref = new StringBuilder();
        xref.append("xref\n0 7\n").append(String.format("%010d 65535 f \n", 0));

        for (int i = 1; i <= 6; i++) {
            xref.append(String.format("%010d 00000 n \n", off[i]));
        }

        xref.append("trailer\n<< /Size 7 /Root 1 0 R >>\nstartxref\n")
                .append(xOff).append("\n%%EOF\n");

        buf.write(xref.toString().getBytes("ISO-8859-1"));

        Files.write(file.toPath(), buf.toByteArray());
    }

    private void drawStatBox(StringBuilder cs, int x, int y, String label, String value, String rgbColor) {
        cs.append("0.988 0.945 0.957 rg\n");
        cs.append(x).append(" ").append(y - 52).append(" 140 60 re f\n");

        cs.append(rgbColor).append(" rg\n");
        cs.append(x).append(" ").append(y + 5).append(" 140 6 re f\n");

        cs.append("0.3 w\n").append(rgbColor).append(" RG\n");
        cs.append(x).append(" ").append(y - 52).append(" 140 60 re S\n");

        cs.append("BT\n/F2 10 Tf\n0.216 0.098 0.149 rg\n");

        double labelX = x + (140 - label.length() * 5.5) / 2.0;
        cs.append((int) labelX).append(" ").append(y - 12).append(" Td\n");
        appendLine(cs, label);
        cs.append("ET\n");

        cs.append("BT\n/F1 22 Tf\n").append(rgbColor).append(" rg\n");

        double numX = x + (140 - value.length() * 13.0) / 2.0;
        cs.append((int) numX).append(" ").append(y - 40).append(" Td\n");
        appendLine(cs, value);
        cs.append("ET\n");
    }

    private void appendLine(StringBuilder cs, String text) {
        String s = text == null ? "" : text
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");

        cs.append("(").append(s).append(") Tj\n");
    }

    private void appendCentered(StringBuilder cs, String text, int pageW, int y, int fontSize) {
        int x = (int) ((pageW - text.length() * fontSize * 0.52) / 2.0);

        if (x < 40) {
            x = 40;
        }

        cs.append(x).append(" ").append(y).append(" Td\n");
        appendLine(cs, text);
    }

    private void write(ByteArrayOutputStream buf, String s) throws IOException {
        buf.write(s.getBytes("ISO-8859-1"));
    }

    private String truncate(String s, int max) {
        if (s == null) {
            return "";
        }

        return s.length() > max ? s.substring(0, max - 1) + "." : s;
    }
}