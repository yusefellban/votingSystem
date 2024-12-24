package com.voting.votingsystem.controller;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ClientControllerTest {

    private static clientController controller;
    private static Stage primaryStage;

    @BeforeAll
    static void setUpBeforeClass() {
        // Launch the JavaFX application once for the entire test suite
        Application.launch(TestApplication.class);
    }

    @BeforeEach
    void setUp() {
        // Create a new controller and stage for each test method
        controller = new clientController();
        primaryStage = new Stage(); // Create a fresh Stage for each test
    }

    @AfterEach
    void tearDown() {
        // Close the stage after each test to prevent lingering JavaFX threads
        if (primaryStage != null) {
            Platform.runLater(() -> primaryStage.close());
        }
    }

    @Test
    void showLoginPage() {
        // Test showLoginPage in the JavaFX application thread
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            controller.start(primaryStage);
            controller.showLoginPage();
        }));
    }

    @Test
    void showRegisterPage() {
        // Test showRegisterPage in the JavaFX application thread
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            controller.start(primaryStage);
            controller.showRegisterPage();
        }));
    }

    @Test
    void showVotingPage() {
        // Test showVotingPage in the JavaFX application thread
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            controller.start(primaryStage);
            controller.showVotingPage();
        }));
    }

    @Test
    void main() {
        // Test if the main method can run without issues in the JavaFX thread
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            clientController.main(new String[0]);
        }));
    }

    // Minimal TestApplication class to initialize the JavaFX toolkit
    public static class TestApplication extends Application {
        @Override
        public void start(Stage primaryStage) {
            // No UI code needed, just need to initialize the toolkit
        }
    }
}
