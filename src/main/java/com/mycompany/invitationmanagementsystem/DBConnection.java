package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL  = "jdbc:mysql://localhost:3306/invitation_db?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "Lturki20";

    // 
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
    
