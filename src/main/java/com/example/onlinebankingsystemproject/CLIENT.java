package com.example.onlinebankingsystemproject;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class CLIENT extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Online Banking System");

        TabPane tabPane = new TabPane();

        Tab viewBalanceTab = new Tab("View Balance", new ViewBalancePane());
        Tab transferFundsTab = new Tab("Transfer Funds", new TransferFundsPane());

        tabPane.getTabs().add(viewBalanceTab);
        tabPane.getTabs().add(transferFundsTab);

        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
