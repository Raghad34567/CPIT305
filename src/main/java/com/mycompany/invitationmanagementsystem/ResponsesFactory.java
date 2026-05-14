package com.mycompany.invitationmanagementsystem;

import javax.swing.JFrame;

public class ResponsesFactory implements FrameFactory {

    @Override
    public JFrame createFrame(DashboardFrame dashboard) {
        return new ResponsesFrame(dashboard);
    }
}