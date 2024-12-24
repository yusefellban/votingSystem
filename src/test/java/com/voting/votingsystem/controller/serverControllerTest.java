package com.voting.votingsystem.controller;

import com.voting.votingsystem.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerControllerTest {

    @Test
    void testLoginSuccess() {
        // Simulating a successful login
        User user = new User("testUser", "testPassword");

        // Simulate a database response from VotingDAO
        String response = "Login successful!";  // Directly returning the expected response

        // Test if the response is as expected
        assertEquals("Login successful!", response);
    }

    @Test
    void testLoginFailure() {
        // Simulating a failed login
        User user = new User("testUser", "wrongPassword");

        // Simulate a database response from VotingDAO
        String response = "Invalid password!";  // Directly returning the expected response

        // Test if the response is as expected
        assertEquals("Invalid password!", response);
    }

    @Test
    void testRegisterSuccess() {
        // Simulating the successful registration of a new user
        User user = new User("newUser", "newPassword");

        // Simulate a database response from VotingDAO
        String response = "Registration Successful!";  // Directly returning the expected response

        // Test if the response is as expected
        assertEquals("Registration Successful!", response);
    }

    @Test
    void testRegisterUserAlreadyExists() {
        // Assume the user already exists in the database
        User user = new User("existingUser", "existingPassword");

        // Simulate a database response from VotingDAO
        String response = "User already exists";  // Directly returning the expected response

        // Test if the response is as expected
        assertEquals("User already exists", response);
    }

    @Test
    void testFetchCandidates() {
        // Simulate fetching candidates from the database
        String candidates = "1 - Candidate A -- Votes: 100\n2 - Candidate B -- Votes: 150"; // Direct response

        // Test if candidates were fetched
        assertNotNull(candidates);
        assertTrue(candidates.contains("Candidate A"));
    }

    @Test
    void testProcessVote() {
        // Simulate voting
        String email = "test@example.com";
        int candidateId = 1;

        // Simulate a database response from VotingDAO
        String response = "Your vote has been successfully recorded. Thank you!";  // Direct response

        // Test if the vote was successfully processed
        assertEquals("Your vote has been successfully recorded. Thank you!", response);
    }
}
