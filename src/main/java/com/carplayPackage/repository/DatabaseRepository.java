package com.carplayPackage.repository;

import com.carplayPackage.DatabaseConnection;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class DatabaseRepository {
    

    public String getCurrentTime() {
        String currentTime = null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT NOW()");
                if (rs.next()) {
                    currentTime = rs.getString(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currentTime;
    }
}
