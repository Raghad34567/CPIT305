package com.mycompany.invitationmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/invitation_db?useSSL=false&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "Lturki20";
            Connection con = DriverManager.getConnection(url, user, password);
            return con;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}