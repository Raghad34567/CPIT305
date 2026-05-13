/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.invitationmanagementsystem;

/*
 * Observer Pattern:
 * This interface defines the contract for any class
 * that wants to be notified when a guest submits an RSVP.
 */
public interface RSVPObserver {
    void onRSVPReceived(String guestName, String response);
}