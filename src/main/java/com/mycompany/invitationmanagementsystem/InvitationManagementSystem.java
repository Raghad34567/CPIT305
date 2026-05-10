package com.mycompany.invitationmanagementsystem;

/*
 * This is the main class that starts the whole system.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class InvitationManagementSystem {

    // Main method: starts this part of the program.
    public static void main(String[] args) {

        //setup database
        CreateEventDatabase.setup();

        //run email server thread
        // Use a thread so this work can run separately from the main screen.
        new Thread(() -> {
            EmailServer.main(null);
        }).start();

        //open system
        // Show the selected window to the user.
        new RoleSelectionFrame().setVisible(true);
    }
}
