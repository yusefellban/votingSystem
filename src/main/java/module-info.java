module com.voting.votingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.voting.votingsystem to javafx.fxml;
    exports com.voting.votingsystem.view;
    opens com.voting.votingsystem.view to javafx.fxml;
    exports com.voting.votingsystem.controller;
    opens com.voting.votingsystem.controller to javafx.fxml;
}