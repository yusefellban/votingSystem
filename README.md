# Real-Time Voting System

## Overview

The **Real-Time Voting System** is a JavaFX-based application that allows users to securely log in, register, and cast votes for candidates in an election. The application is designed to be simple, intuitive, and responsive, ensuring a seamless user experience. The system employs a client-server architecture, where the client communicates with a server to authenticate users, register accounts, and process votes.

## Features

- **User Authentication**: Users can log in or create a new account using a secure login page.
- **Voting**: After successful login, users can view available candidates and cast their votes in real-time.
- **Real-Time Updates**: The system ensures that voting results are updated in real-time as users vote.
- **Error Handling**: Proper error messages are displayed for failed login attempts, invalid credentials, and other system errors.
- **Responsive UI**: The application provides a clean and user-friendly interface using JavaFX.

## Technologies Used

- **JavaFX**: For building the graphical user interface (GUI).
- **Java 17**: Core language for developing the application.
- **MySQL**: For database management and storing user credentials, votes, and candidate information.
- **Socket Programming**: For communication between the client and server using TCP/IP.
- **Maven**: For dependency management.

## System Architecture

The system follows the MVC (Model-View-Controller) design pattern, dividing it into three primary components:

### Client(View)

- **LoginPage**: A login interface that allows users to authenticate using their credentials.
- **RegistrationPage**: A page for users to register a new account.
- **VotingPage**: A page where users can view candidates and cast their votes.

### Server(Controller)

- **ServerController**: Listens for incoming client connections, processes client requests (login, register, fetch candidates, cast votes), and interacts with the database.
- **ClientController**: To handel pages 
- **VotingDAO**: Handles interactions with the MySQL database for login, registration, vote processing, and fetching candidate information.

### User(model)

-**User**: A record used as a model to represent user data such as username and password.

## Setup and Installation

### Prerequisites

- **Java 17** or higher installed.
- **MySQL** installed and running.
- Maven installed for dependency management.

### Database Setup

1. Create a database named `voting_db` in MySQL.
2. Run the following SQL script to set up the necessary tables:

```sql
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE candidates (
    candidate_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    vote_count INT DEFAULT 0
);
INSERT INTO candidates (name) VALUES ('Candidate A'), ('Candidate B'), ('Candidate C');
CREATE TABLE voters (
    voter_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE,
    has_voted BOOLEAN DEFAULT FALSE
);
