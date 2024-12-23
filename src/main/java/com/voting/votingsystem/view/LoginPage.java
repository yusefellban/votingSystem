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

public class LoginPage {
    private final Stage stage;
    private final clientController app;
    private TextField usernameField;
    private PasswordField passwordField;
    public LoginPage(Stage stage, clientController app) {
        this.stage = stage;
        this.app = app;
    }

    public void show() {
        Label usernameLabel = new Label("Username:");
         usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label passwordLabel = new Label("Password:");
         passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        usernameLabel.setStyle("-fx-text-fill: white;");
        passwordLabel.setStyle("-fx-text-fill: white;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #0059ff; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
       loginButton.setCursor(Cursor.HAND);
        loginButton.setOnAction(e -> handleLogin(usernameField.getText().trim(), passwordField.getText().trim()));

        Button registrationButton = new Button("Create Account");
        registrationButton.setStyle("-fx-background-color: #353535; -fx-text-fill: #9d9bdc; -fx-font-size: 12px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-font-style: italic;");
       registrationButton.setCursor(Cursor.HAND);
        registrationButton.setOnAction(e -> app.showRegisterPage());

        VBox buttonBox = new VBox(10, loginButton, registrationButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, buttonBox);
        loginLayout.setAlignment(Pos.CENTER_LEFT);
        loginLayout.setPadding(new Insets(60));
        loginLayout.setStyle("-fx-background-color: #444444;");

        Scene loginScene = new Scene(loginLayout, 400, 500);
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

    private void handleLogin(String username, String password) {

        if(username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Empty username or password.");
            return;
        }
        Task<String> loginTask = new Task<>() {
            @Override
            protected String call() {
                try (Socket socket = new Socket("localhost", 12345);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    out.println("LOGIN");
                    out.println(username);
                    out.println(password);
                    return in.readLine();
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }
        };

        loginTask.setOnSucceeded(e -> {
            if ("Login successful!".equals(loginTask.getValue())) {
                app.showVotingPage();
            } else {
                showAlert("Error", "IInvalid username or password.");
            }
        });

        loginTask.setOnFailed(e -> {
            showAlert("Error", "Login failed.");
        });

        new Thread(loginTask).start();

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
