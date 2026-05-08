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

    private static final int    SERVER_PORT    = 5002;
    private static final String SMTP_HOST      = "smtp.gmail.com";
    private static final int    SMTP_PORT      = 465;
    private static final String SENDER_EMAIL   = "invitationmanagementsystem@gmail.com";
    private static final String SENDER_PASSWORD = "xcje knvl vknn uwov";
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("Email Server started on port " + SERVER_PORT);
        System.out.println("Thread pool size: 10 threads");

        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

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

    // =========================================================
    // HTML invitation email
    // =========================================================
    private static void sendInvitationEmail(String receiverEmail, String guestName, String inviteLink)
            throws EmailServerException {
        String subject  = "You Are Cordially Invited \u2665";
        String htmlBody = buildHtmlEmail(guestName, inviteLink);
        try {
            sendEmail(receiverEmail, subject, htmlBody);
        } catch (Exception e) {
            throw new EmailServerException("Failed to send email to " + receiverEmail + ": " + e.getMessage());
        }
    }

    private static String buildHtmlEmail(String guestName, String inviteLink) {
        return "<!DOCTYPE html>\r\n"
            + "<html lang=\"en\">\r\n"
            + "<head>\r\n"
            + "  <meta charset=\"UTF-8\">\r\n"
            + "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0\">\r\n"
            + "  <title>Event Invitation</title>\r\n"
            + "</head>\r\n"
            + "<body style=\"margin:0;padding:0;background-color:#fff0f4;font-family:'Georgia',serif;\">\r\n"
            + "  <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\r\n"
            + "         style=\"background-color:#fff0f4;padding:40px 0;\">\r\n"
            + "    <tr><td align=\"center\">\r\n"
            + "\r\n"
            + "      <!-- Card -->\r\n"
            + "      <table width=\"580\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"\r\n"
            + "             style=\"background-color:#fffcf6;border:2px solid #c69b50;\r\n"
            + "                    border-radius:16px;overflow:hidden;\r\n"
            + "                    box-shadow:0 6px 24px rgba(139,20,60,0.15);\">\r\n"
            + "\r\n"
            + "        <!-- Top accent bar -->\r\n"
            + "        <tr><td height=\"8\" style=\"background:linear-gradient(90deg,#8b143c,#c04070,#8b143c);\">&nbsp;</td></tr>\r\n"
            + "\r\n"
            + "        <!-- Header -->\r\n"
            + "        <tr><td align=\"center\" style=\"padding:36px 40px 10px;\">\r\n"
            + "          <p style=\"margin:0;font-size:13px;color:#c69b50;letter-spacing:3px;text-transform:uppercase;\">"
            +               "&#10086;&nbsp; Event Invitation &nbsp;&#10086;</p>\r\n"
            + "          <h1 style=\"margin:12px 0 6px;font-size:30px;color:#8b143c;font-weight:bold;letter-spacing:1px;\">"
            +               "You Are Cordially Invited</h1>\r\n"
            + "          <p style=\"margin:0;font-size:14px;color:#c69b50;letter-spacing:2px;\">"
            +               "&#10022; &mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&mdash;&mdash; &#10022;</p>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- Greeting -->\r\n"
            + "        <tr><td style=\"padding:18px 50px 6px;\">\r\n"
            + "          <p style=\"margin:0;font-size:17px;color:#371926;\">Dear <strong>"
            +               escapeHtml(guestName) + "</strong>,</p>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- Body text -->\r\n"
            + "        <tr><td style=\"padding:10px 50px 20px;\">\r\n"
            + "          <p style=\"margin:0;font-size:16px;color:#371926;line-height:1.7;\">\r\n"
            + "            We are delighted to invite you to join us for a special event.<br>\r\n"
            + "            Please click the button below to open your personal invitation\r\n"
            + "            and confirm your attendance (RSVP).\r\n"
            + "          </p>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- Divider -->\r\n"
            + "        <tr><td style=\"padding:0 50px;\">\r\n"
            + "          <hr style=\"border:none;border-top:1px solid #e8c8d4;margin:0;\">\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- CTA Button -->\r\n"
            + "        <tr><td align=\"center\" style=\"padding:28px 40px;\">\r\n"
            + "          <a href=\"" + escapeHtml(inviteLink) + "\"\r\n"
            + "             style=\"display:inline-block;"
            +                     "background:linear-gradient(135deg,#8b143c,#c04070);"
            +                     "color:#ffffff;text-decoration:none;"
            +                     "font-size:16px;font-weight:bold;"
            +                     "padding:14px 38px;border-radius:50px;"
            +                     "letter-spacing:1px;"
            +                     "box-shadow:0 4px 14px rgba(139,20,60,0.35);\">\r\n"
            + "            Open My Invitation &rarr;\r\n"
            + "          </a>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- Link fallback (copy-paste in app) -->\r\n"
            + "        <tr><td align=\"center\" style=\"padding:0 50px 20px;\">\r\n"
            + "          <p style=\"margin:0;font-size:12px;color:#8c557a;\">"
            +               "Or copy &amp; paste this link into the RSVP page:</p>\r\n"
            + "          <p style=\"margin:6px 0 0;font-size:12px;color:#8b143c;word-break:break-all;\">"
            +               escapeHtml(inviteLink) + "</p>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "        <!-- Bottom accent bar -->\r\n"
            + "        <tr><td height=\"8\" style=\"background:linear-gradient(90deg,#8b143c,#c04070,#8b143c);\">&nbsp;</td></tr>\r\n"
            + "\r\n"
            + "        <!-- Footer -->\r\n"
            + "        <tr><td align=\"center\" style=\"padding:16px 40px;background-color:#fdf5f8;\">\r\n"
            + "          <p style=\"margin:0;font-size:12px;color:#b07090;letter-spacing:1px;\">\r\n"
            + "            &copy; Invitation Management System &nbsp;|&nbsp;\r\n"
            + "            Sent with <span style=\"color:#c04070;\">&#10084;</span>\r\n"
            + "          </p>\r\n"
            + "        </td></tr>\r\n"
            + "\r\n"
            + "      </table>\r\n"
            + "      <!-- End Card -->\r\n"
            + "\r\n"
            + "    </td></tr>\r\n"
            + "  </table>\r\n"
            + "</body>\r\n"
            + "</html>\r\n";
    }

    
    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private static void sendEmail(String receiverEmail, String subject, String htmlBody) throws Exception {
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

            writer.write("From: Invitation System <" + SENDER_EMAIL + ">\r\n");
            writer.write("To: " + receiverEmail + "\r\n");
            writer.write("Subject: " + subject + "\r\n");
            writer.write("MIME-Version: 1.0\r\n");
            writer.write("Content-Type: text/html; charset=UTF-8\r\n");
            writer.write("\r\n");
            writer.write(htmlBody);
            writer.write("\r\n.\r\n");
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

    // Custom Exception
    static class EmailServerException extends Exception {
        public EmailServerException(String message) { super(message); }
        public EmailServerException(String message, Throwable cause) { super(message, cause); }
    }
}
