package com.example.onlinebankingsystemproject;



import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewBalancePane extends GridPane {
    private TextField accountNumberField;
    private Label ownerLabel;
    private Label balanceLabel;

    public ViewBalancePane() {
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(8);
        setHgap(10);

        Label accountNumberLabel = new Label("Account Number:");
        accountNumberField = new TextField();
        Button viewBalanceButton = new Button("View Balance");
        ownerLabel = new Label();
        balanceLabel = new Label();

        add(accountNumberLabel, 0, 0);
        add(accountNumberField, 1, 0);
        add(viewBalanceButton, 1, 1);
        add(ownerLabel, 1, 2);
        add(balanceLabel, 1, 3);

        viewBalanceButton.setOnAction(e -> viewBalance());
    }

    private void viewBalance() {
        String accountNumber = accountNumberField.getText();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ONLINE_BANKING_PROJECT", "root", "ROHIT");
             PreparedStatement stmt = conn.prepareStatement("SELECT owner, balance FROM Accounts WHERE accountNumber = ?")) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ownerLabel.setText("Owner: " + rs.getString("owner"));
                balanceLabel.setText("Balance: " + rs.getDouble("balance"));
            } else {
                ownerLabel.setText("Account not found");
                balanceLabel.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ownerLabel.setText("Error: " + e.getMessage());
            balanceLabel.setText("");
        }
    }
}

