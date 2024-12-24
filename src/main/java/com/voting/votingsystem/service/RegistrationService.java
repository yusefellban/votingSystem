package com.voting.votingsystem.service;

import com.voting.votingsystem.model.User;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RegistrationService {

    //TODO Data filtration needed

    public void register(User user) {
             //async
        Task<String> registerTask = new Task<>() {
            @Override
            protected String call() {
                try (Socket socket = new Socket("localhost", 12345);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                    out.println("REGISTER");
                    out.println(user.userName());
                    out.println(user.password());
                    return in.readLine();
                } catch (IOException e) {
                    return "Error: " + e.getMessage();
                }
            }
        };

        registerTask.setOnSucceeded(e -> {
            if ("Registration Successful!".equals(registerTask.getValue())) {
                successAlert("Register successful.");
            } else {
                if (user.userName().isEmpty() || user.password().isEmpty()) {
                    errorAlert( "empty username or password.");
                } else {
                    errorAlert( registerTask.getValue());
                }

            }
        });

        registerTask.setOnFailed(e -> {
            errorAlert("Register failed.");
        });

        new Thread(registerTask).start();


    }

    private void errorAlert( String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Add an icon to the alert (optional)
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("success-icon.png"))));

        // Change the background color of the dialog
        alert.getDialogPane().setStyle(
                "-fx-background-color: #e80b0b; " + // Light green background
                        "-fx-border-radius: 10px; " +      // Rounded corners
                        "-fx-background-radius: 10px;"    // Match border radius
        );
        alert.showAndWait();
    }
    private void successAlert( String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        // Add an icon to the alert (optional)
//        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
//        stage.getIcons().add(new Image(getClass().getResourceAsStream("success-icon.png")));

        // Change the background color of the dialog
        alert.getDialogPane().setStyle(
                "-fx-background-color: #02ff0f; " + // Light green background
                        "-fx-border-radius: 10px; " +      // Rounded corners
                        "-fx-background-radius: 10px;"    // Match border radius
        );
        alert.showAndWait();
    }
}
