package com.mycompany.invitationmanagementsystem;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.net.ssl.SSLSocketFactory;

public class EmailServer {

    private static final int SERVER_PORT = 5002;

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;

   
    private static final String SENDER_EMAIL = "invitationmanagementsystem@gmail.com";
    private static final String SENDER_PASSWORD = "xcje knvl vknn uwov";

    public static void main(String[] args) {

        System.out.println("Email Server is running on port " + SERVER_PORT);

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleClient(clientSocket);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {

        try (Socket socket = clientSocket;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            String guestEmail = reader.readLine();
            String guestName = reader.readLine();
            String inviteLink = reader.readLine();

            sendInvitationEmail(guestEmail, guestName, inviteLink);

            writer.write("SUCCESS");
            writer.newLine();
            writer.flush();

            System.out.println("Email sent to: " + guestEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendInvitationEmail(String receiverEmail, String guestName, String inviteLink) throws Exception {

    String subject = "Event Invitation";

    String body = "Dear " + guestName + ",\n\n"
            + "You are invited to our event.\n\n"
            + "Please copy the invitation link below and paste it in the RSVP page in the program:\n"
            + inviteLink + "\n\n"
            + "Best regards,\n"
            + "Invitation Management System";

    sendEmail(receiverEmail, subject, body);
}

    private static void sendEmail(String receiverEmail, String subject, String body) throws Exception {

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (Socket socket = factory.createSocket(SMTP_HOST, SMTP_PORT);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            readResponse(reader);

            sendCommand(writer, reader, "EHLO localhost");
            sendCommand(writer, reader, "AUTH LOGIN");
            sendCommand(writer, reader, encode(SENDER_EMAIL));
            sendCommand(writer, reader, encode(SENDER_PASSWORD));
            sendCommand(writer, reader, "MAIL FROM:<" + SENDER_EMAIL + ">");
            sendCommand(writer, reader, "RCPT TO:<" + receiverEmail + ">");
            sendCommand(writer, reader, "DATA");

            writer.write("From: " + SENDER_EMAIL + "\r\n");
            writer.write("To: " + receiverEmail + "\r\n");
            writer.write("Subject: " + subject + "\r\n");
            writer.write("Content-Type: text/plain; charset=UTF-8\r\n");
            writer.write("\r\n");
            writer.write(body + "\r\n");
            writer.write(".\r\n");
            writer.flush();

            readResponse(reader);

            sendCommand(writer, reader, "QUIT");
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private static void sendCommand(BufferedWriter writer, BufferedReader reader, String command) throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        readResponse(reader);
    }

    private static void readResponse(BufferedReader reader) throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);

            if (line.length() >= 4 && line.charAt(3) == ' ') {
                break;
            }
        }
    }
}
