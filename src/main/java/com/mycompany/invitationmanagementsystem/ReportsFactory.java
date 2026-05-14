package com.mycompany.invitationmanagementsystem;

import javax.swing.JFrame;

public class ReportsFactory implements FrameFactory {

    @Override
    public JFrame createFrame(DashboardFrame dashboard) {
        return new ReportsFrame(dashboard);
    }
}
