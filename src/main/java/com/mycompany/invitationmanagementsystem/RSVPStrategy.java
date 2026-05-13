package com.mycompany.invitationmanagementsystem;

import javax.swing.JTextField;

/*
 * Strategy interface for handling different RSVP responses.
 */
public interface RSVPStrategy {

    int getGuestCount(JTextField guestsField) throws Exception;
}