package com.voting.votingsystem.view;

import com.voting.votingsystem.controller.clientController;
import com.voting.votingsystem.model.User;
import com.voting.votingsystem.service.RegistrationService;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    //TODO Data filtration needed

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
        registrationButton.setOnAction(event -> {
                    if (dataValidation()) {
                        responseLabel.setText("correct data ");
                        responseLabel.setStyle("-fx-text-fill: black; -fx-background-color: green; -fx-font-size: 11px;-fx-padding: 8px 15px; -fx-border-radius: 6px; -fx-background-radius: 8px;");
                        RegistrationService registrationService = new RegistrationService();
                        registrationService.register(new User(usernameField.getText().trim(), passwordField.getText().trim()));
                    }
                }
        );
        Button loginButton = new Button("I have an account");
        loginButton.setStyle("-fx-background-color: #353535; -fx-text-fill: #9d9bdc; -fx-font-size: 12px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-font-style: italic;");
        loginButton.setCursor(Cursor.HAND);
        loginButton.setOnAction(event -> app.showLoginPage());

        responseLabel = new Label();


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


    private boolean dataValidation() {
        if (nameField.getText().trim().isEmpty() || ageField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty()) {
            responseLabel.setText("Please fill all the fields");
            responseLabel.setStyle("-fx-text-fill: black; -fx-background-color: red; -fx-font-size: 11px;-fx-padding: 8px 15px; -fx-border-radius: 6px; -fx-background-radius: 8px;");
            return false;
        }
        return true;
    }

}
