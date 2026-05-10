package com.mycompany.invitationmanagementsystem;

/*
 * This is the main class that starts the whole system.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class InvitationManagementSystem {

    public static void main(String[] args) {

        //setup database
        CreateEventDatabase.setup();

        //run email server thread
        new Thread(() -> {
            EmailServer.main(null);
        }).start();

        //open system
        new RoleSelectionFrame().setVisible(true);
    }
}