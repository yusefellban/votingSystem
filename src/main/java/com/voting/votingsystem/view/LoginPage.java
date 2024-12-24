package com.voting.votingsystem.view;

import com.voting.votingsystem.controller.clientController;
import com.voting.votingsystem.model.User;
import com.voting.votingsystem.service.LoginService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
        Label passwordLabel = new Label("Password:");


        usernameField = new TextField();
        passwordField = new PasswordField();
        usernameField.setPromptText("Enter your username");
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        Button registrationButton = new Button("Create Account");

        VBox buttonBox = new VBox(10, loginButton, registrationButton);
        VBox loginLayout = new VBox(10, usernameLabel, usernameField, passwordLabel, passwordField, buttonBox);

        //styles

        usernameLabel.setStyle("-fx-text-fill: white;");
        passwordLabel.setStyle("-fx-text-fill: white;");

        loginButton.setStyle("-fx-background-color: #0059ff; -fx-text-fill: white; -fx-font-size: 14px; " + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        loginButton.setCursor(Cursor.HAND);


        registrationButton.setStyle("-fx-background-color: #353535; -fx-text-fill: #9d9bdc; -fx-font-size: 12px; " + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;-fx-font-style: italic;");
        registrationButton.setCursor(Cursor.HAND);


        buttonBox.setAlignment(Pos.CENTER);
        loginLayout.setAlignment(Pos.CENTER_LEFT);
        loginLayout.setPadding(new Insets(60));
        loginLayout.setStyle("-fx-background-color: #444444;");


        // Action buttons
        loginButton.setOnAction(e -> {
            LoginService loginService = new LoginService(app);
            loginService.handleLogin(new User(usernameField.getText().trim(), passwordField.getText().trim()));
        });

        registrationButton.setOnAction(e -> app.showRegisterPage());

        //show screen
        Scene loginScene = new Scene(loginLayout, 400, 500);
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.show();
    }

}
