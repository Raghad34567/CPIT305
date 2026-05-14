package com.mycompany.invitationmanagementsystem;

import javax.swing.JFrame;

public class ManageGuestsFactory implements FrameFactory {

    @Override
    public JFrame createFrame(DashboardFrame dashboard) {
        return new ManageGuestsFrame(dashboard);
    }
}