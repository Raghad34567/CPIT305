package com.mycompany.invitationmanagementsystem;

public class InvitationManagementSystem {

    public static void main(String[] args) {

         CreateEventDatabase.setup();

//        new LoginFrame().setVisible(true);
        new RoleSelectionFrame().setVisible(true);
    }
}
