package com.krnelx.javafxcrudapp;

import com.github.javafaker.Faker;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableInitializer {

  public static void main(String[] args) {
    String url = "jdbc:mysql://localhost:3306/database";
    String user = "";
    String password = "";

    int numberOfRecords = 50;

    try (Connection conn = DriverManager.getConnection(url, user, password)) {
      Faker faker = new Faker();
      String sql = "INSERT INTO users (first_name, last_name, email, age, gender, country) VALUES (?, ?, ?, ?, ?, ?)";
      PreparedStatement statement = conn.prepareStatement(sql);

      for (int i = 0; i < numberOfRecords; i++) {
        statement.setString(1, faker.name().firstName());
        statement.setString(2, faker.name().lastName());
        statement.setString(3, faker.internet().emailAddress());
        statement.setInt(4, faker.number().numberBetween(18, 80));
        statement.setString(5, faker.demographic().sex());
        statement.setString(6, faker.country().name());
        statement.executeUpdate();
      }

      System.out.println("Records inserted successfully");
    } catch (SQLException e) {
      System.out.println("SQL Exception: " + e.getMessage());
    }
  }
}
