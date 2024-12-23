package com.voting.votingsystem.view;

import com.voting.votingsystem.controller.clientController;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RegistrationPage {
    private final Stage stage;
    private final clientController app;
    private Label responseLabel;
    private TextField nameField;
    private TextField ageField;
    private TextField usernameField;
    private PasswordField passwordField;

    public RegistrationPage(Stage stage, clientController app) {
        this.stage = stage;
        this.app = app;
    }

    public void show() {

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white;");
        nameField = new TextField();
        nameField.setPromptText("Enter your name");

        Label ageLabel = new Label("Age:");
        ageLabel.setStyle("-fx-text-fill: white;");
        ageField = new TextField();
        ageField.setPromptText("Enter your age");


        Label usernameLabel = new Label("Username:");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");


        usernameLabel.setStyle("-fx-text-fill: white;");
        passwordLabel.setStyle("-fx-text-fill: white;");

        Button registrationButton = new Button("Register");
        registrationButton.setStyle("-fx-background-color: #0059ff; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        registrationButton.setCursor(Cursor.HAND);
        registrationButton.setOnAction(event -> register(passwordField.getText().trim(),usernameField.getText().trim()));
        Button loginButton = new Button("I have an account");
        loginButton.setStyle("-fx-background-color: #353535; -fx-text-fill: #9d9bdc; -fx-font-size: 12px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-font-style: italic;");
        loginButton.setCursor(Cursor.HAND);
        loginButton.setOnAction(event -> app.showLoginPage());

        responseLabel = new Label();
        responseLabel.setStyle("-fx-text-fill: black; -fx-background-color: red; -fx-background-radius: 7;");

        VBox buttonBox = new VBox(10, registrationButton, loginButton, responseLabel);
        buttonBox.setAlignment(Pos.CENTER);

        VBox loginLayout = new VBox(10, nameLabel, nameField, ageLabel, ageField, usernameLabel, usernameField, passwordLabel, passwordField, buttonBox);
        loginLayout.setAlignment(Pos.CENTER_LEFT);
        loginLayout.setPadding(new Insets(60));
        loginLayout.setStyle("-fx-background-color: #444444;");

        Scene loginScene = new Scene(loginLayout, 400, 500);
        stage.setTitle("Registration");
        stage.setScene(loginScene);
        stage.show();
    }


    private void register(String password,String username) {
        String age = ageField.getText().trim();
        String name = nameField.getText().trim();
        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || age.isEmpty()) {
            responseLabel.setText("There an empty fields");
            responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: red;-fx-background-radius: 7;"); // Error feedback
        }

        Task<String> registerTask = new Task<>() {
            @Override
            protected String call() {
                try (Socket socket = new Socket("localhost", 12345);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    out.println("REGISTER");
                    out.println(username);
                    out.println(password);
                    return in.readLine();
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }
        };

        registerTask.setOnSucceeded(e -> {
            if ("registration successful!".equals(registerTask.getValue())) {
              responseLabel.setText("Registration successful!");
                responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: green;-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 15px;"); // Success feedback

            } else {
//                showAlert("Error", "Invalid Fields.");
                responseLabel.setText("Registration Failed!"+registerTask.getValue());
                responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: #bb1010;-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-size: 11px;"); // Success feedback

            }
        });

        registerTask.setOnFailed(e -> {
            showAlert("Error", "Register failed.");
        });

        new Thread(registerTask).start();



    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
