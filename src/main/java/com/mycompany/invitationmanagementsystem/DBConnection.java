package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * This class handles the database connection using the Singleton Design Pattern.
 *
 * Singleton means only one object from this class can be created and used
 * in the whole program. This keeps the database connection settings in one place.
 */
public class DBConnection {

    // Database name used by the project.
    private static final String DB_NAME = "invitation_db";

    // URL for connecting directly to the project database.
    private static final String URL =
            "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&allowPublicKeyRetrieval=true";

    // URL for connecting to MySQL server before the database is created.
    private static final String SERVER_URL =
            "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";

    // Database username.
    private static final String USER = "root";

    // Database password.
    private static final String PASS = "1234";

    // This is the single object of DBConnection.
    private static DBConnection instance;

    // Private constructor: no other class can create an object using new DBConnection().
    private DBConnection() {
    }

    // This method returns the single object. If it does not exist, it creates it once.
    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // This static method is kept so the other classes can still use DBConnection.connect().
    public static Connection connect() throws DatabaseConnectionException {
        return getInstance().getConnection();
    }

    // This method opens a connection with the project database.
    public Connection getConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Cannot connect to database: " + e.getMessage(), e);
        }
    }

    // This method opens a connection with MySQL server, used when creating the database.
    public Connection getServerConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(SERVER_URL, USER, PASS);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Cannot connect to MySQL server: " + e.getMessage(), e);
        }
    }

    // Getter for the database name. Used by the setup class.
    public String getDatabaseName() {
        return DB_NAME;
    }

    // Custom exception for database connection errors.
    static class DatabaseConnectionException extends Exception {

        public DatabaseConnectionException(String message) {
            super(message);
        }

        public DatabaseConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
