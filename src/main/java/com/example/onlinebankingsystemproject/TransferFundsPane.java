package com.example.onlinebankingsystemproject;



import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransferFundsPane extends GridPane {
    private TextField fromAccountField;
    private TextField toAccountField;
    private TextField amountField;
    private Label resultLabel;

    public TransferFundsPane() {
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(8);
        setHgap(10);

        Label fromAccountLabel = new Label("From Account:");
        fromAccountField = new TextField();
        Label toAccountLabel = new Label("To Account:");
        toAccountField = new TextField();
        Label amountLabel = new Label("Amount:");
        amountField = new TextField();
        Button transferButton = new Button("Transfer");
        resultLabel = new Label();

        add(fromAccountLabel, 0, 0);
        add(fromAccountField, 1, 0);
        add(toAccountLabel, 0, 1);
        add(toAccountField, 1, 1);
        add(amountLabel, 0, 2);
        add(amountField, 1, 2);
        add(transferButton, 1, 3);
        add(resultLabel, 1, 4);

        transferButton.setOnAction(e -> transferFunds());
    }

    private void transferFunds() {
        String fromAccount = fromAccountField.getText();
        String toAccount = toAccountField.getText();
        double amount = Double.parseDouble(amountField.getText());

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ONLINE_BANKING_PROJECT", "root", "ROHIT")) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement("SELECT balance FROM Accounts WHERE accountNumber = ?");
                 PreparedStatement stmt2 = conn.prepareStatement("UPDATE Accounts SET balance = ? WHERE accountNumber = ?");
                 PreparedStatement stmt3 = conn.prepareStatement("SELECT balance FROM Accounts WHERE accountNumber = ?");
                 PreparedStatement stmt4 = conn.prepareStatement("UPDATE Accounts SET balance = ? WHERE accountNumber = ?")) {

                stmt1.setString(1, fromAccount);
                ResultSet rs1 = stmt1.executeQuery();
                if (!rs1.next() || rs1.getDouble("balance") < amount) {
                    resultLabel.setText("Insufficient funds or account not found");
                    conn.rollback();
                    return;
                }
                double fromBalance = rs1.getDouble("balance");

                stmt3.setString(1, toAccount);
                ResultSet rs2 = stmt3.executeQuery();
                if (!rs2.next()) {
                    resultLabel.setText("Recipient account not found");
                    conn.rollback();
                    return;
                }
                double toBalance = rs2.getDouble("balance");

                stmt2.setDouble(1, fromBalance - amount);
                stmt2.setString(2, fromAccount);
                stmt2.executeUpdate();

                stmt4.setDouble(1, toBalance + amount);
                stmt4.setString(2, toAccount);
                stmt4.executeUpdate();

                conn.commit();
                resultLabel.setText("Transfer successful");
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
                resultLabel.setText("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultLabel.setText("Error: " + e.getMessage());
        }
    }
}

