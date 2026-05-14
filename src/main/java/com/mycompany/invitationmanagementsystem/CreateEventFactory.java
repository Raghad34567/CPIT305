package com.mycompany.invitationmanagementsystem;

import javax.swing.JFrame;

public class CreateEventFactory implements FrameFactory {

    @Override
    public JFrame createFrame(DashboardFrame dashboard) {
        return new CreateEventFrame(dashboard);
    }
}