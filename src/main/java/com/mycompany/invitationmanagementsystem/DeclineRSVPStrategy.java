package com.mycompany.invitationmanagementsystem;

import javax.swing.JTextField;

/*
 * Strategy for declined RSVP responses.
 * Declined invitations always have zero guests.
 */
public class DeclineRSVPStrategy implements RSVPStrategy {

    @Override
    public int getGuestCount(JTextField guestsField) {
        return 0;
    }
}