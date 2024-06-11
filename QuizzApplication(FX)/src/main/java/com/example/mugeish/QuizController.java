package com.example.mugeish;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class QuizController implements Initializable {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mugeish";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2005";

    @FXML
    private String currentQuizTitle;
    @FXML
    private TextArea questionTextArea;

    @FXML
    private TextField option1TextField;
    @FXML
    private TextField option2TextField;
    @FXML
    private TextField option3TextField;
    @FXML
    private TextField option4TextField;

    @FXML
    private RadioButton correctOption1RadioButton;
    @FXML
    private RadioButton correctOption2RadioButton;
    @FXML
    private RadioButton correctOption3RadioButton;
    @FXML
    private RadioButton correctOption4RadioButton;

    @FXML
    private ToggleGroup radioGroup;

    @FXML
    private TextField quizTitleTextField;

    @FXML
    private Button createQuizButton;
    @FXML
    private Button addNextQuestionButton;
    @FXML
    private Button submitQuizButton;

    private Connection connection;

    public void initialize(URL url, ResourceBundle rb) {
        radioButtonSetup();

        createQuizButton.setOnAction(event -> createQuiz());
        addNextQuestionButton.setOnAction(event -> addNextQuestion());
        submitQuizButton.setOnAction(event -> submitQuiz());

        // Initialize the database connection
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to connect to the database.");
        }
    }

    private void radioButtonSetup() {
        radioGroup = new ToggleGroup();
        correctOption1RadioButton.setToggleGroup(radioGroup);
        correctOption2RadioButton.setToggleGroup(radioGroup);
        correctOption3RadioButton.setToggleGroup(radioGroup);
        correctOption4RadioButton.setToggleGroup(radioGroup);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void createQuiz() {
        String quizTitle = quizTitleTextField.getText();
        if (quizTitle.isEmpty()) {
            showAlert("Error", "Please enter a quiz title.");
            return;
        }

        try {
            // Check if the quiz title already exists
            if (quizTitleExists(quizTitle)) {
                showAlert("Error", "Quiz title already exists. Choose a different title.");
                return;
            }

            // Save the quiz title to the database
            saveQuizTitle(quizTitle);

            // Set the current quiz title
            currentQuizTitle = quizTitle;

            showAlert("Success", "Quiz created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to create the quiz. Please try again.");
        }
    }

    private boolean quizTitleExists(String quizTitle) throws SQLException {
        String query = "SELECT * FROM quiz_titles WHERE title = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, quizTitle);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    private void saveQuizTitle(String quizTitle) throws SQLException {
        String query = "INSERT INTO quiz_titles (title) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, quizTitle);
            preparedStatement.executeUpdate();
        }
    }

    private void addNextQuestion() {
        // Check if the quiz title is set
        if (currentQuizTitle == null || currentQuizTitle.isEmpty()) {
            showAlert("Error", "Please set a quiz title first.");
            return;
        }

        // Validate and get question details from the UI
        QuizQuestion question = getQuestionFromUI();
        if (question == null) {
            return;
        }

        // Save the question to the database
        saveQuestion(question);

        // Clear the fields for the next question
        clearQuestionFields();
    }

    private void saveQuestion(QuizQuestion question) {
        // Your code to save the question to the database goes here
        // Use the question parameter to associate the question with the quiz
        // You can use JDBC to execute SQL queries
        // Example code:
        try {
            String query = "INSERT INTO quiz_questions (quiz_title_id, question_text, option1, option2, option3, option4, correct_option) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, getQuizTitleId(currentQuizTitle));
                preparedStatement.setString(2, question.getQuestionText());
                preparedStatement.setString(3, question.getOption1());
                preparedStatement.setString(4, question.getOption2());
                preparedStatement.setString(5, question.getOption3());
                preparedStatement.setString(6, question.getOption4());
                preparedStatement.setString(7, question.getCorrectOption());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the question. Please try again.");
        }
    }

    private int getQuizTitleId(String quizTitle) {
        try {
            String query = "SELECT id FROM quiz_titles WHERE title = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, quizTitle);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve quiz title ID.");
        }
        return -1;
    }

    private QuizQuestion getQuestionFromUI() {
        String questionText = questionTextArea.getText();
        String option1 = option1TextField.getText();
        String option2 = option2TextField.getText();
        String option3 = option3TextField.getText();
        String option4 = option4TextField.getText();

        RadioButton selectedRadioButton = (RadioButton) radioGroup.getSelectedToggle();
        String correctOption = (selectedRadioButton != null) ? selectedRadioButton.getText() : null;

        // Validate that none of the fields are empty
        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctOption == null) {
            showAlert("Error", "Please fill in all fields and select a correct option.");
            return null;
        }

        return new QuizQuestion(questionText, option1, option2, option3, option4, correctOption);
    }

    private void clearQuestionFields() {
        questionTextArea.clear();
        option1TextField.clear();
        option2TextField.clear();
        option3TextField.clear();
        option4TextField.clear();
        radioGroup.getSelectedToggle().setSelected(false);
    }


    private void submitQuiz() {
        // Check if the quiz title is set
        if (currentQuizTitle == null || currentQuizTitle.isEmpty()) {
            showAlert("Error", "Please set a quiz title first.");
            return;
        }

        // Retrieve all questions for the current quiz title from the UI
        List<QuizQuestion> questions = getAllQuestionsFromUI();

        // Check if there are questions to submit
        if (questions.isEmpty()) {
            showAlert("Error", "No questions added to the quiz.");
            return;
        }

        // Store the quiz title to the database if it doesn't exist
        try {
            if (!quizTitleExists(currentQuizTitle)) {
                saveQuizTitle(currentQuizTitle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save the quiz title. Please try again.");
            return;
        }

        // Store all questions for the current quiz title to the database
        for (QuizQuestion question : questions) {
            saveQuestion(question);
        }

        showAlert("Quiz Submitted", "Quiz submitted successfully!");
    }

    private List<QuizQuestion> getAllQuestionsFromUI() {
        List<QuizQuestion> questions = new ArrayList<>();

        // Iterate over UI elements and retrieve questions
        // You can modify this based on the structure of your UI
        // For example, you might have a dynamic UI where users can add multiple questions

        // Sample code assuming a single question for simplicity
        QuizQuestion question = getQuestionFromUI();
        if (question != null) {
            questions.add(question);
        }

        return questions;
    }

}
