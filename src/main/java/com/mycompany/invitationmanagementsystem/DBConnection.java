package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection connect() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/wedding_system?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "1234"
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
