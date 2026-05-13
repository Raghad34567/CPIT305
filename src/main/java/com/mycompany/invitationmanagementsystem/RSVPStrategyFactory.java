package com.mycompany.invitationmanagementsystem;

/*
 * Factory class that selects the correct RSVP strategy.
 */
public class RSVPStrategyFactory {

    public static RSVPStrategy getStrategy(String status) {

        if ("Accept with pleasure".equals(status)) {
            return new AcceptRSVPStrategy();
        }

        if ("Regretfully Decline".equals(status)) {
            return new DeclineRSVPStrategy();
        }

        throw new IllegalArgumentException("Unknown RSVP response: " + status);
    }
}