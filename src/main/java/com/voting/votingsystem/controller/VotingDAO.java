package com.voting.votingsystem.controller;


import com.voting.votingsystem.model.User;

import java.sql.*;

public class VotingDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/voting_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123!";

    public static String verifyLogin(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {

            stmt.setString(1, user.userName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password").equals(user.password()) ? "Login successful!" : "Invalid password!";
            } else {
                return "Username not found!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error during login!";
        }
    }

    public static String verifyRegister(User user) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT IGNORE INTO users (username, password) VALUES (?, ?)")) {

            stmt.setString(1, user.userName());
            stmt.setString(2, user.password());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Registration Successful!" : "User already exists";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error during registration!";
        }
    }

    public static String fetchCandidates() {
        StringBuilder candidates = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT candidate_id, name, vote_count FROM candidates")) {

            while (rs.next()) {
                candidates.append(rs.getInt("candidate_id")).append(" - ")
                        .append(rs.getString("name")).append(" -- Votes: ")
                        .append(rs.getInt("vote_count")).append("\n");
            }
            return candidates.toString().trim();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching candidates.";
        }
    }

    public static String processVote(String email, int candidateId) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            // Check if the user has already voted
            try (PreparedStatement checkStmt = conn.prepareStatement("SELECT has_voted FROM voters WHERE email = ?")) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getBoolean("has_voted")) {
                    return "You have already voted!";
                }
            }

            // Record the vote
            try (PreparedStatement voteStmt = conn.prepareStatement(
                    "INSERT INTO voters (email, has_voted) VALUES (?, ?) ON DUPLICATE KEY UPDATE has_voted = TRUE")) {
                voteStmt.setString(1, email);
                voteStmt.setBoolean(2, true);
                voteStmt.executeUpdate();
            }

            // Update the candidate's vote count
            try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE candidates SET vote_count = vote_count + 1 WHERE candidate_id = ?")) {
                updateStmt.setInt(1, candidateId);
                updateStmt.executeUpdate();
            }

            conn.commit();
            return "Your vote has been successfully recorded. Thank you!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error processing your vote.";
        }
    }
}
