package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateEventDatabase {

    public static void setup() {
        try {
            String dbName = "invitation_db";
            String connectionURL = "jdbc:mysql://localhost:3306/";

            Connection con = DriverManager.getConnection(
                    connectionURL, "root", "Raghadnaif123456");

            Statement st = con.createStatement();

            st.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            st.executeUpdate("USE " + dbName);

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS events (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "date VARCHAR(50), " +
                    "time VARCHAR(50), " +
                    "location VARCHAR(150), " +
                    "capacity INT, " +
                    "description VARCHAR(255))"
            );

            try {
                st.executeUpdate("ALTER TABLE events ADD COLUMN capacity INT");
            } catch (Exception e) {
                // العمود موجود خلاص، تجاهلي
            }

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS guests (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "phone VARCHAR(100), " +
                    "event_id INT)"
            );

            System.out.println("Database setup completed successfully!");

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        setup();
    }
}