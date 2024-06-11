package com.example.mugeish;


import com.example.mugeish.UserDataDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneController {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField Cpass;

    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupEmailField;
    @FXML
    private TextField signupPhoneNumberField;
    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Perform validation (replace this with your actual validation logic)
        if ("admin".equals(enteredUsername) && "admin123".equals(enteredPassword)) {
            // Successful login, navigate to the next screen (replace with your navigation logic)
            System.out.println("Login successful. Navigating to the next screen.");
            switchToHome(event); // Call the switchToHome method
        } else {
            // Invalid credentials, show an error message
            showAlert("Invalid credentials", "Please enter a valid username and password.");
        }
    }

    public void handleSignup(ActionEvent event) throws IOException {
        String username = signupUsernameField.getText();
        String email = signupEmailField.getText();
        String phoneNumber = signupPhoneNumberField.getText();
        String password = signupPasswordField.getText();

        // Call UserDataDAO to insert the user data into the database
         UserDataDAO.insertUserData(username, email, phoneNumber, password);

        // Perform any additional actions, e.g., switch to a different scene
        switchToHome(event);
    }




    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("QuizHomePage.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void switchToSignup(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SignUp.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    public void SwitchToLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void SwitchToAddNewQuiz(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("AddNewQuiz.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }



}