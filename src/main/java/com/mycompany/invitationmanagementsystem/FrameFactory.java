package com.mycompany.invitationmanagementsystem;
 
import javax.swing.JFrame;
 
/*
 * Factory Method Pattern:
 * This interface defines the contract for creating frames.
 * Each concrete factory is responsible for creating one specific frame.
 *
 * Problem solved: DashboardFrame was directly creating all frames using "new",
 * which means any new frame required modifying the Dashboard itself.
 * Now, each frame has its own factory, and the Dashboard only talks to the factory.
 */

public interface FrameFactory {
 
    // This method creates and returns the appropriate frame.
    JFrame createFrame(DashboardFrame dashboard);
}

