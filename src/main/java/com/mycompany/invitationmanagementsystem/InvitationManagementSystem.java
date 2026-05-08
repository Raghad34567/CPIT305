package com.mycompany.invitationmanagementsystem;

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