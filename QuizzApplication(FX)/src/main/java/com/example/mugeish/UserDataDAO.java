package com.example.mugeish;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDataDAO {
    // Database connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mugeish";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2005";
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message
        }
    }

    // Insert user data into the user_data table
    public static void insertUserData(String username, String email, String phoneNumber, String password) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // SQL insert statement
            String sql = "INSERT INTO user_data (username, email, phone_number, password) VALUES (?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                // Set values for the parameters
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, phoneNumber);
                preparedStatement.setString(4, password);

                // Execute the update
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                // Handle PreparedStatement-specific exceptions
                e.printStackTrace();
                // Add additional error handling or throw a custom exception if needed
            }
        } catch (SQLException e) {
            // Handle Connection-specific exceptions
            e.printStackTrace();
            // Add additional error handling or throw a custom exception if needed
        }
    }

    public static void Questiodata(String Question_text, String Op1, String Op2,String Op3,String Op4,String Correct_Answer,String Q_id) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            // SQL insert statement
            String Questdata = "INSERT INTO quiz_question (Question_text,Op1,Op2,Op3,Op4,Correct_Answer,Q_id) VALUES (?, ?, ?, ?,?,?,?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(Questdata)) {
                // Set values for the parameters
                preparedStatement.setString(1,Question_text);
                preparedStatement.setString(2, Op1);
                preparedStatement.setString(3, Op2);
                preparedStatement.setString(4, Op3);
                preparedStatement.setString(5, Op4);
                preparedStatement.setString(6, Correct_Answer);
                preparedStatement.setString(7, Q_id);

                // Execute the update
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                // Handle PreparedStatement-specific exceptions
                e.printStackTrace();
                // Add additional error handling or throw a custom exception if needed
            }
        } catch (SQLException e) {
            // Handle Connection-specific exceptions
            e.printStackTrace();
            // Add additional error handling or throw a custom exception if needed
        }
    }
}
