package com.example.student_track.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class DatabaseConnection {
    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void checkConnection() {
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ Database connected successfully!");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to the database!");
            e.printStackTrace();
        }
    }
}