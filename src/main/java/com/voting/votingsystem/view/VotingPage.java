package com.voting.votingsystem.view;

import com.voting.votingsystem.controller.clientController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.regex.Pattern;

public class VotingPage {
    private final Stage stage;
    private TextField emailField;
    private TextField candidateIdField;
    private Label responseLabel;
    private TextArea candidatesList;
    private final clientController app;

    public VotingPage(Stage stage, clientController app) {
        this.stage = stage;
        this.app = app;
    }

    public void show() {
        Label instructionLabel = new Label("Enter your email and select a candidate to vote:");
        instructionLabel.setFont(Font.font("Arial", 16));
        instructionLabel.setTextFill(Color.LIGHTGRAY);
        instructionLabel.setStyle("-fx-text-fill: #0095ff; -fx-font-size: 15px");

        Label emailLabel = new Label("Email:");
        emailLabel.setFont(Font.font("Arial", 14));
        emailLabel.setTextFill(Color.WHITE);

        Label candidateIdLabel = new Label("Candidate ID:");
        candidateIdLabel.setFont(Font.font("Arial", 14));
        candidateIdLabel.setTextFill(Color.WHITE);

        Label candidatesLabel = new Label("Available Candidates:");
        candidatesLabel.setFont(Font.font("Arial", 14));
        candidatesLabel.setTextFill(Color.WHITE);

        emailField = new TextField();
        emailField.setPromptText("Enter your email");

        candidateIdField = new TextField();
        candidateIdField.setPromptText("Enter candidate ID");

        candidatesList = new TextArea();
        candidatesList.setEditable(false);
        candidatesList.setStyle("-fx-control-inner-background: #444444; -fx-text-fill: white; "
                + "-fx-font-size: 14px; -fx-font-family: 'Monospaced'; "
                + "-fx-border-color: #007BFF; -fx-border-width: 2px; -fx-background-radius: 5;");
        candidatesList.setPrefHeight(250);

        responseLabel = new Label();
        responseLabel.setStyle("-fx-text-fill: black; -fx-background-color: red; -fx-background-radius: 7;");

        Button submitButton = createStyledButton("Vote");
        submitButton.setOnAction(e -> handleSendVote());

        Button exitButton = createStyledButton("Exit");
        exitButton.setOnAction(e -> app.showLoginPage());

        VBox inputSection = new VBox(10, candidatesLabel, candidatesList, emailLabel, emailField, candidateIdLabel, candidateIdField);
        inputSection.setAlignment(Pos.CENTER);

        HBox buttonSection = new HBox(10, submitButton, exitButton);
        buttonSection.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, instructionLabel, inputSection, buttonSection, responseLabel);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #444444;");

        Scene scene = new Scene(layout, 400, 500);
        stage.setTitle("Real-Time Voting System");
        stage.setScene(scene);
        stage.show();

        fetchCandidates();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-size: 14px; "
                + "-fx-padding: 8px 15px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        return button;
    }

    private void handleSendVote() {
        String email = emailField.getText().trim();
        String candidateIdStr = candidateIdField.getText().trim();

        if (!isValidEmail(email)) {
            responseLabel.setText("Invalid email address.");
            responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: red;-fx-background-radius: 7;"); // Error feedback
            return;
        }

        if (candidateIdStr.isEmpty() || !candidateIdStr.matches("\\d+")) {
            responseLabel.setText("Invalid candidate ID.");
            responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: red;-fx-background-radius: 7;"); // Error feedback
            return;
        }

        Task<String> voteTask = new Task<>() {
            @Override
            protected String call() {
                try (Socket socket = new Socket("localhost", 12345);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    out.println(email);
                    out.println(candidateIdStr);
                    return in.readLine();
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }
        };

        voteTask.setOnSucceeded(e -> {
            responseLabel.setText(voteTask.getValue());
            if ("Your vote has been successfully recorded. Thank you!".equals(voteTask.getValue())) {
                emailField.clear();
                candidateIdField.clear();
                responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: green;-fx-background-radius: 7;"); // Success feedback
            } else {
                responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: red;-fx-background-radius: 7;"); // Error feedback
            }
        });

        voteTask.setOnFailed(e -> {
            responseLabel.setText("Error: " + voteTask.getException().getMessage());
            responseLabel.setStyle("-fx-text-fill: black;-fx-background-color: red;-fx-background-radius: 7;"); // Error feedback
        });

        new Thread(voteTask).start();
    }


    private void fetchCandidates() {
        Task<Void> fetchTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    try (Socket socket = new Socket("localhost", 12345);
                         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                        out.println("GET_CANDIDATES");
                        String serverResponse;
                        StringBuilder candidatesBuilder = new StringBuilder();

                        while ((serverResponse = in.readLine()) != null) {
                            candidatesBuilder.append(serverResponse).append("\n");
                        }

                        String candidatesData = candidatesBuilder.toString().trim();
                        // Update the UI in the JavaFX Application Thread
                        Platform.runLater(() -> candidatesList.setText(candidatesData));
                    } catch (IOException e) {
                        System.err.println("Error fetching candidates: " + e.getMessage());
                    }

                    Thread.sleep(5000); // Refresh every 5 seconds
                }
            }
        };

        Thread fetchThread = new Thread(fetchTask);
        fetchThread.setDaemon(true);
        fetchThread.start();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailPattern, email);
    }

}

