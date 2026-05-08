package com.mycompany.invitationmanagementsystem;

public class InvitationManagementSystem {

    public static void main(String[] args) {

        // Organizer log in:
        // Username: admin
        // Password: 1234
         CreateEventDatabase.setup();

        new LoginFrame().setVisible(true);
        new RoleSelectionFrame().setVisible(true);
    }
}
