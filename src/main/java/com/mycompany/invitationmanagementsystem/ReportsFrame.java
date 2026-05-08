package com.mycompany.invitationmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.sql.*;

public class ReportsFrame extends JFrame {

    DashboardFrame    dashboard;
    JComboBox<String> eventBox;
    int               selectedEventId = -1;

    JLabel totalVal, acceptedVal, declinedVal;

    public ReportsFrame(DashboardFrame dashboard) {
        this.dashboard = dashboard;
        setTitle("Event Reports & Statistics");
        setSize(900, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = UITheme.createRoseBackground();
        main.setLayout(new BorderLayout());

        // ── North: header + event selector
        JPanel northWrapper = new JPanel(new BorderLayout(0, 0));
        northWrapper.setOpaque(false);
        northWrapper.add(UITheme.createHeader("Event Reports Overview"), BorderLayout.NORTH);

        eventBox = UITheme.createComboBox("Select Event");
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 24, 10, 24));
        topPanel.add(eventBox, BorderLayout.CENTER);
        northWrapper.add(topPanel, BorderLayout.SOUTH);
        main.add(northWrapper, BorderLayout.NORTH);

        // ── Center: 3 stat cards
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        totalVal    = new JLabel("—", SwingConstants.CENTER);
        acceptedVal = new JLabel("—", SwingConstants.CENTER);
        declinedVal = new JLabel("—", SwingConstants.CENTER);

        statsPanel.add(createStatCard("Total Guests", totalVal,    new Color(139, 20,  60)));
        statsPanel.add(createStatCard("Accepted",     acceptedVal, new Color(34,  139, 34)));
        statsPanel.add(createStatCard("Declined",     declinedVal, new Color(180, 50,  50)));

        main.add(statsPanel, BorderLayout.CENTER);

        // ── South: buttons
        JButton refresh   = new JButton("Refresh");
        JButton exportPdf = new JButton("Export as PDF");
        JButton back      = new JButton("Back");
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
        back.addActionListener(e -> { dashboard.setVisible(true); dispose(); });
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
            ResultSet  rs   = conn.createStatement()
                                  .executeQuery("SELECT id, name FROM events");
            eventBox.removeAllItems();
            while (rs.next())
                eventBox.addItem(rs.getInt("id") + " : " + rs.getString("name"));
            if (eventBox.getItemCount() > 0) {
                eventBox.setSelectedIndex(0);
                loadStats();
            }
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadStats() {
        if (eventBox.getSelectedItem() == null) return;
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
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth(), h = getHeight();
                g2.setColor(new Color(155, 75, 105, 30));
                g2.fillRoundRect(5, 7, w-7, h-7, 32, 32);
                g2.setColor(new Color(255, 252, 250));
                g2.fillRoundRect(0, 0, w-5, h-5, 30, 30);
                // colored top bar
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, w-5, 10, 30, 30);
                g2.fillRect(0, 5, w-5, 8);
                g2.setColor(UITheme.BORDER);
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, w-6, h-6, 30, 30);
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

    // ── Export PDF ────────────────────────────────────────────────────────
    private void doExportPdf(JButton btn) {
        String eventName = "", eventDate = "", eventLoc = "";
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
                eventLoc  = er.getString("location");
            }

            PreparedStatement gp = conn.prepareStatement(
                "SELECT name, response, guest_count FROM guests WHERE event_id=?");
            gp.setInt(1, selectedEventId);
            ResultSet gr = gp.executeQuery();
            while (gr.next()) {
                String resp = gr.getString("response");
                if (resp == null || resp.trim().isEmpty()) resp = "No Response";
                guests.add(new String[]{
                    gr.getString("name"), resp,
                    String.valueOf(gr.getInt("guest_count"))
                });
            }
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save Report PDF");
        chooser.setSelectedFile(new File("Report_" + eventName.replace(" ", "_") + ".pdf"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File target = chooser.getSelectedFile();
        if (!target.getName().toLowerCase().endsWith(".pdf"))
            target = new File(target.getAbsolutePath() + ".pdf");

        btn.setEnabled(false);
        final File   ft  = target;
        final String fn  = eventName, fd = eventDate, fl = eventLoc;
        final String tot = totalVal.getText(), acc = acceptedVal.getText(),
                     dec = declinedVal.getText();

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
                          String evName, String evDate, String evLoc,
                          String total, String accepted, String declined,
                          java.util.List<String[]> guests) throws IOException {

        StringBuilder cs = new StringBuilder();
        cs.append("BT\n/F1 20 Tf\n50 800 Td\n");
        line(cs, "Event Report");
        cs.append("/F1 11 Tf\n0 -14 Td\n");
        line(cs, "================================================");
        cs.append("/F1 13 Tf\n0 -20 Td\n");
        line(cs, "Event  :  " + evName);
        cs.append("0 -18 Td\n"); line(cs, "Date   :  " + evDate);
        cs.append("0 -18 Td\n"); line(cs, "Venue  :  " + evLoc);
        cs.append("0 -26 Td\n/F1 14 Tf\n"); line(cs, "Statistics");
        cs.append("/F1 12 Tf\n0 -18 Td\n"); line(cs, "  Total Guests  :  " + total);
        cs.append("0 -18 Td\n"); line(cs, "  Accepted      :  " + accepted);
        cs.append("0 -18 Td\n"); line(cs, "  Declined      :  " + declined);
        cs.append("0 -26 Td\n/F1 14 Tf\n"); line(cs, "Guest Details");
        cs.append("/F1 11 Tf\n0 -18 Td\n");
        line(cs, "  Name                         Response     Companions");
        cs.append("0 -14 Td\n");
        line(cs, "  -------------------------------------------------------");

        for (String[] row : guests) {
            cs.append("0 -16 Td\n");
            String resp  = row[1].contains("Accept") ? "Accepted" : "Declined";
            line(cs, "  " + pad(row[0], 30) + pad(resp, 14) + row[2]);
        }
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
        write(buf, "3 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Courier >>\nendobj\n");
        off[4] = buf.size();
        write(buf, "4 0 obj\n<< /Type /Page /Parent 2 0 R\n" +
            "   /MediaBox [0 0 595 842]\n   /Contents 5 0 R\n" +
            "   /Resources << /Font << /F1 3 0 R >> >> >>\nendobj\n");
        off[5] = buf.size();
        write(buf, "5 0 obj\n<< /Length " + csBytes.length + " >>\nstream\n");
        buf.write(csBytes);
        write(buf, "\nendstream\nendobj\n");

        int xOff = buf.size();
        StringBuilder xref = new StringBuilder();
        xref.append("xref\n0 6\n").append(String.format("%010d 65535 f \n", 0));
        for (int i = 1; i <= 5; i++) xref.append(String.format("%010d 00000 n \n", off[i]));
        xref.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n")
            .append(xOff).append("\n%%EOF\n");
        buf.write(xref.toString().getBytes("ISO-8859-1"));

        Files.write(file.toPath(), buf.toByteArray());
    }

    private void line(StringBuilder cs, String text) {
        String s = text.replace("\\","\\\\").replace("(","\\(").replace(")","\\)");
        cs.append("(").append(s).append(") Tj\n");
    }
    private void write(ByteArrayOutputStream buf, String s) throws IOException {
        buf.write(s.getBytes("ISO-8859-1"));
    }
    private String pad(String s, int len) {
        if (s == null) s = "";
        if (s.length() >= len) return s.substring(0, len);
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < len) sb.append(' ');
        return sb.toString();
    }
}