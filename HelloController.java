package com.example.ap_assignment4;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    private TableView<?> menuTable;

    // Method to handle the button click to switch to Pending Orders
    @FXML
    private void switchToPendingOrders() throws IOException {
        // Load the Pending Orders FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/ap_assignment4/PendingOrdersPage.fxml"));
        Scene pendingOrdersScene = new Scene(loader.load());

        // Get the current stage and set the new scene
        Stage stage = (Stage) menuTable.getScene().getWindow();
        stage.setScene(pendingOrdersScene);
        stage.setTitle("Pending Orders");
    }
}
