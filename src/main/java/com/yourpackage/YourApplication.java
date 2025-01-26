package com.yourpackage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class YourApplication {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT NOW()");
            if (rs.next()) {
                System.out.println("Database connected: " + rs.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
