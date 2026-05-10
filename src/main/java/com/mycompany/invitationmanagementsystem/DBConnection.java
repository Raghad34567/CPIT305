package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * This class handles the database connection.
 * 
 * This project was developed using Java Swing,
 * MySQL database, and threading concepts.
 */

public class DBConnection {

    // This method handles the "jdbc:mysql://localhost:3306/invitation_db?useSSL=false&allowPublicKeyRetrieval=true"; part of the class logic.
    private static final String URL  = "jdbc:mysql://localhost:3306/invitation_db?useSSL=false&allowPublicKeyRetrieval=true";
    // This method handles the "root"; part of the class logic.
    private static final String USER = "root";
    
    private static final String PASS = "Raghadnaif123456";

    // This method opens a connection with the MySQL database.
    public static Connection connect() throws DatabaseConnectionException {
        try {
            Connection con = DriverManager.getConnection(URL, USER, PASS);
            return con;
        } catch (Exception e) {
            throw new DatabaseConnectionException("Cannot connect to database: " + e.getMessage());
        }
    }
    
    //Custom Exception
    static class DatabaseConnectionException
            extends Exception {

        public DatabaseConnectionException(String message) {
            super(message);
        }

        public DatabaseConnectionException(
                String message,
                Throwable cause) {

            super(message, cause);
        }
    }
}
    
