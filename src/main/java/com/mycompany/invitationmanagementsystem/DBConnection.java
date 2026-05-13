package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    // This is the shared database connection used by the whole program.
    private Connection connection;

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

    // This method returns the shared Singleton connection.
    public Connection getConnection() throws DatabaseConnectionException {
        try {
            // If the connection was not created yet, or it was closed, create it again.
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
            return connection;
        } catch (Exception e) {
            throw new DatabaseConnectionException("Cannot connect to database: " + e.getMessage(), e);
        }
    }

    // This static method is kept to support old code if needed.
    public static Connection connect() throws DatabaseConnectionException {
        return getInstance().getConnection();
    }

    // This method opens a server connection only for database setup.
    // It is separate because it connects before the project database exists.
    public Connection getServerConnection() throws DatabaseConnectionException {
        try {
            return DriverManager.getConnection(SERVER_URL, USER, PASS);
        } catch (Exception e) {
            throw new DatabaseConnectionException("Cannot connect to MySQL server: " + e.getMessage(), e);
        }
    }

    // Close the shared connection only when the whole program is finished.
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
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
