
package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class serverController {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        System.out.println(
                "    ____            _     _____ _                 __     __    _   _               ____            _                  \n" +
                        " |  _ \\ ___  __ _| |   |_   _(_)_ __ ___   ___  \\ \\   / /__ | |_(_)_ __   __ _  / ___| _   _ ___| |_ ___ _ __ ___   \n" +
                        " | |_) / _ \\/ _` | |_____| | | | '_ ` _ \\ / _ \\  \\ \\ / / _ \\| __| | '_ \\ / _` | \\___ \\| | | / __| __/ _ \\ '_ ` _ \\  \n" +
                        " |  _ <  __/ (_| | |_____| | | | | | | | |  __/   \\ V / (_) | |_| | | | | (_| |  ___) | |_| \\__ \\ ||  __/ | | | | | \n" +
                        " |_| \\_\\___|\\__,_|_|     |_| |_|_| |_| |_|\\___|    \\_/ \\___/ \\__|_|_| |_|\\__, | |____/ \\__, |___/\\__\\___|_| |_| |_| \n" +
                        "                                                                         |___/         |___/                        "

        );
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (Socket socket = clientSocket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String command = in.readLine();
            System.out.println(command);
            switch (command) {
                case "LOGIN" -> {
                    User user = new User(in.readLine(), in.readLine());
                    out.println(VotingDAO.verifyLogin(user));
                }
                case "REGISTER" -> {
                    User user = new User(in.readLine(), in.readLine());
                    out.println(VotingDAO.verifyRegister(user));
                }
                case "GET_CANDIDATES" -> out.println(VotingDAO.fetchCandidates());
                case null, default -> {
                    String email = command;
                    String candidateIdStr = in.readLine();
                    int candidateId = Integer.parseInt(candidateIdStr);
                    String response = VotingDAO.processVote(email, candidateId);
                    out.println(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

