package com.voting.votingsystem.service;

import com.voting.votingsystem.controller.clientController;
import com.voting.votingsystem.model.User;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginService {
    private final clientController app;

    public LoginService(clientController app) {
        this.app = app;
    }
    public void handleLogin(User user) {

        if (user.userName().isEmpty() || user.password().isEmpty()) {
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
                    out.println(user.userName());
                    out.println(user.password());
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
