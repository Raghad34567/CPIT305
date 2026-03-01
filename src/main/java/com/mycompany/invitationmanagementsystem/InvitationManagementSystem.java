/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.invitationmanagementsystem;

/**
 *
 * @author lama
 */
public class InvitationManagementSystem {

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
        new DashboardFrame().setVisible(true);
        new CreateEventFrame().setVisible(true);
        new ManageGuestsFrame().setVisible(true);
        new GuestInvitationFrame().setVisible(true);
        new RSVPFrame().setVisible(true);
        new GuestQRCodeFrame().setVisible(true);
    }
}
