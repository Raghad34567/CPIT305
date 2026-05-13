package com.mycompany.invitationmanagementsystem;

import javax.swing.JTextField;

/*
 * Strategy for accepted RSVP responses.
 * It reads and validates the number of guests.
 */
public class AcceptRSVPStrategy implements RSVPStrategy {

    @Override
    public int getGuestCount(JTextField guestsField) throws Exception {
        String text = guestsField.getText().trim();

        if (text.isEmpty()) {
            throw new Exception("Enter number of guests");
        }

        int count;

        try {
            count = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new Exception("Enter valid number");
        }

        if (count <= 0) {
            throw new Exception("Guest count must be greater than 0");
        }

        return count;
    }
}