package com.voting.votingsystem.controller;

import com.voting.votingsystem.view.LoginPage;
import com.voting.votingsystem.view.RegistrationPage;
import com.voting.votingsystem.view.VotingPage;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class clientController extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        showLoginPage();
        // Load the icon image (ensure the path is correct)
        Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Logo.png")));
        primaryStage.getIcons().add(appIcon);
    }

    public void showLoginPage() {
        LoginPage loginPage = new LoginPage(primaryStage, this);
        loginPage.show();
    }

    public void showRegisterPage() {
        RegistrationPage registrationPage = new RegistrationPage(primaryStage, this);
        registrationPage.show();
    }

    public void showVotingPage() {
        VotingPage votingPage = new VotingPage(primaryStage, this);
        votingPage.show();
    }

    public static void main(String[] args) {
        System.out.println(
                "    ____            _     _____ _                 __     __    _   _               ____            _                  \n" +
                        " |  _ \\ ___  __ _| |   |_   _(_)_ __ ___   ___  \\ \\   / /__ | |_(_)_ __   __ _  / ___| _   _ ___| |_ ___ _ __ ___   \n" +
                        " | |_) / _ \\/ _` | |_____| | | | '_ ` _ \\ / _ \\  \\ \\ / / _ \\| __| | '_ \\ / _` | \\___ \\| | | / __| __/ _ \\ '_ ` _ \\  \n" +
                        " |  _ <  __/ (_| | |_____| | | | | | | | |  __/   \\ V / (_) | |_| | | | | (_| |  ___) | |_| \\__ \\ ||  __/ | | | | | \n" +
                        " |_| \\_\\___|\\__,_|_|     |_| |_|_| |_| |_|\\___|    \\_/ \\___/ \\__|_|_| |_|\\__, | |____/ \\__, |___/\\__\\___|_| |_| |_| \n" +
                        "                                                                         |___/         |___/                        "

        );
        launch(args);
    }

}
