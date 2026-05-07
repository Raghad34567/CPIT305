package com.mycompany.invitationmanagementsystem;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EmailClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void sendInvitationRequest(String guestEmail, String guestName, String inviteLink) throws IOException {

        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            writer.write(guestEmail);
            writer.newLine();

            writer.write(guestName);
            writer.newLine();

            writer.write(inviteLink);
            writer.newLine();

            writer.flush();

            String response = reader.readLine();

            if (!"SUCCESS".equals(response)) {
                throw new IOException("Server failed to send email: " + response);
            }
        }
    }
}