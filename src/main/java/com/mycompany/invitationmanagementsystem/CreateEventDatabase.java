package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateEventDatabase {

    public static void setup() {
        try {
            String dbName = "invitation_db";

            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true",
                "root", "1234");

            Statement st = con.createStatement();

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            st.executeUpdate("USE " + dbName);

            // ── events table ──────────────────────────────
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS events (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "date VARCHAR(50), " +
                "location VARCHAR(150), " +
                "capacity INT)"   
            );

            // ── guests table ──────────────────────────────
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS guests (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100), " +
                "phone VARCHAR(100), " +
                "event_id INT, " +
                "response VARCHAR(100), " +
                "guest_count INT DEFAULT 0)"
            );

            // ── organizers table ──────────────────────────
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS organizers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "full_name VARCHAR(150), " +
                "username  VARCHAR(100) UNIQUE, " +
                "password  VARCHAR(100), " +
                "email     VARCHAR(150))"
            );

            System.out.println("Database setup completed successfully!");
            con.close();

        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    public static void main(String[] args) {
        setup();
    }
}