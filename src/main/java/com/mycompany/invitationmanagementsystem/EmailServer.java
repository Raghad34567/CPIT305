package com.mycompany.invitationmanagementsystem;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLSocketFactory;

public class EmailServer {

    private static final int SERVER_PORT = 5002;
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 465;
    private static final String SENDER_EMAIL = "invitationmanagementsystem@gmail.com";
    private static final String SENDER_PASSWORD = "xcje knvl vknn uwov";

    // ✅ FIX 1: ExecutorService بدل thread واحد — يدعم multiple clients بشكل حقيقي
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("Email Server started on port " + SERVER_PORT);
        System.out.println("Thread pool size: 10 threads");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // ✅ كل client يشتغل في thread منفصل من الـ pool
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            threadPool.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket) {
        System.out.println("Handling client in thread: " + Thread.currentThread().getName());

        try (Socket socket = clientSocket;
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter writer = new BufferedWriter(
                     new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {

            String guestEmail = reader.readLine();
            String guestName  = reader.readLine();
            String inviteLink = reader.readLine();

            // ✅ FIX 2: Custom exception بدل Exception عام
            if (guestEmail == null || guestName == null || inviteLink == null) {
                throw new EmailServerException("Incomplete data received from client");
            }

            sendInvitationEmail(guestEmail, guestName, inviteLink);

            writer.write("SUCCESS");
            writer.newLine();
            writer.flush();
            System.out.println("Email sent to: " + guestEmail + " | Thread: " + Thread.currentThread().getName());

        } catch (EmailServerException e) {
            System.out.println("Email error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error in handleClient: " + e.getMessage());
        }
    }

    private static void sendInvitationEmail(String receiverEmail, String guestName, String inviteLink)
            throws EmailServerException {
        String subject = "Event Invitation";
        String body = "Dear " + guestName + ",\n\n"
                + "You are invited to our event.\n\n"
                + "Please copy the invitation link below and paste it in the RSVP page:\n"
                + inviteLink + "\n\n"
                + "Best regards,\nInvitation Management System";
        try {
            sendEmail(receiverEmail, subject, body);
        } catch (Exception e) {
            throw new EmailServerException("Failed to send email to " + receiverEmail + ": " + e.getMessage());
        }
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

    private static void sendCommand(BufferedWriter writer, BufferedReader reader, String command)
            throws IOException {
        writer.write(command + "\r\n");
        writer.flush();
        readResponse(reader);
    }

    private static void readResponse(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            if (line.length() >= 4 && line.charAt(3) == ' ') break;
        }
    } 
    
    //Custom Exception
    static class EmailServerException extends Exception {

        public EmailServerException(String message) {
            super(message);
        }

        public EmailServerException(
                String message,
                Throwable cause) {

            super(message, cause);
        }
    }
}
