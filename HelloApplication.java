package com.example.ap_assignment4;

import com.example.ap_assignment4.Assignment3_classes.*;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class HelloApplication extends Application {

    private TableView<Item> menuTable;
    private TableView<Order> orderTable;
    private Stage primaryStage;
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDERS_FILE = "orders.txt";

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        primaryStage.setTitle("ByteMe Canteen System");
        showMenuScreen();
    }
    private void showMenuScreen() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        Label titleLabel = new Label("Menu Items");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        // Create menu table
        menuTable = new TableView<>();
        // Name column for item in Menu
        TableColumn<Item,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        // Price column for item in menu
        TableColumn<Item,Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        // Availability column for item in menu
        TableColumn<Item,Boolean> availabilityCol = new TableColumn<>("Is Available");
        availabilityCol.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isAvailable()).asObject());

        // Category column for item in menu
        TableColumn<Item,String> categoryCol =new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        // Add the columns to the TableView
        menuTable.getColumns().addAll(nameCol,priceCol,availabilityCol,categoryCol);


        // Load menu data
        loadMenuData();

        Button switchToOrdersButton = new Button("View Pending Orders");
        switchToOrdersButton.setOnAction(e -> showOrdersScreen());

        root.getChildren().addAll(titleLabel, menuTable, switchToOrdersButton);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void showOrdersScreen() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label titleLabel = new Label("Pending Orders");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Create orders table
        orderTable = new TableView<>();

        // Order ID column
        TableColumn<Order, Integer> orderIDCol = new TableColumn<>("Order ID");
        orderIDCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getOrderId()).asObject());

        // Customer column
        TableColumn<Order, String> CustomerCol = new TableColumn<>("Customer Name");
        CustomerCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));

        // Status column
        TableColumn<Order, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        // Order Price column
        TableColumn<Order, Double> OrderPriceCol = new TableColumn<>("Order Price");
        OrderPriceCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getOrderPrice()).asObject());

        // Items column
        TableColumn<Order, String> itemsCol = new TableColumn<>("Items Ordered");
        itemsCol.setCellValueFactory(data -> {
            Map<Item, Integer> itemsMap = data.getValue().getItems();
            if (itemsMap == null) return new SimpleStringProperty("No items");

            String itemsFormatted = itemsMap.entrySet().stream()
                    .map(entry -> entry.getKey().getName() + ": " + entry.getValue())
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(itemsFormatted);
        });

        // Add the columns to the TableView
        orderTable.getColumns().addAll(orderIDCol, CustomerCol, statusCol, OrderPriceCol, itemsCol);

        // Load orders data
        loadOrdersData();

        Button switchToMenuButton = new Button("View Menu");
        switchToMenuButton.setOnAction(e -> showMenuScreen());

        root.getChildren().addAll(titleLabel, orderTable, switchToMenuButton);

        Scene scene = new Scene(root, 800, 500); // Adjusted width for Items column
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadMenuData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            ObservableList<Item> items = FXCollections.observableArrayList();

            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing whitespace
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split(",");
                try {
                    if (parts.length == 4) {
                        String name = parts[0];
                        double price = Double.parseDouble(parts[1]);
                        boolean available = Boolean.parseBoolean(parts[2]);
                        String category = parts[3];
                        items.add(new Item(name, price, available, category));
                    }
                }
                catch (NumberFormatException e){
                    System.err.println("Invalid number format in line: " + line); // Debugging info
                }
            }
            menuTable.setItems(items);
        } catch (IOException e) {
            showAlert("Error", "Could not load menu data: " + e.getMessage());
        }
    }
    private Item findItemByName(String itemName) {
        if (menuTable.getItems() == null || menuTable.getItems().isEmpty()) {
            System.err.println("Menu is empty or not loaded.");
            return null;
        }

        for (Item item : menuTable.getItems()) {
            if (item.getName().equalsIgnoreCase(itemName.trim())) {
                return item;
            }
        }

        System.err.println("Item not found: " + itemName);
        return null;
    }
    private void loadOrdersData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            ObservableList<Order> orders = FXCollections.observableArrayList();
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove leading/trailing whitespace
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                String[] parts = line.split(",", 5); // Split into at most 5 parts ie ID, Name, Status, Price, Items
                try {
                    int orderID = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String status = parts[2].trim();
                    double totalPrice = Double.parseDouble(parts[3].trim());
                    Map<Item, Integer> itemsOrdered = new HashMap<>();
                    if (!status.equals("Pending")) {
                        System.err.println("A Non Pending Order can not be Displayed on GUI");
                        continue;
                    }

                    // Parse items from the fifth part, if it exists
                    if (parts.length == 5) {
                        String itemsString = parts[4].trim();
                        String[] itemEntries = itemsString.split(";");
                        for (String entry : itemEntries) {
                            String[] itemData = entry.split(":");
                            if (itemData.length == 2) {
                                String itemName = itemData[0].trim();
                                int quantity = Integer.parseInt(itemData[1].trim());
                                // Find item by name from Menu table
                               Item item = findItemByName(itemName);
                                if (item != null) {
                                    itemsOrdered.put(item, quantity);
                                } else {
                                    System.err.println("Item not found: " + itemName);
                                }
                            }
                        }
                    }

                    // Create and add the order
                    Order order = new Order(orderID, status, customerName, itemsOrdered, totalPrice);
                    orders.add(order);

                } catch (NumberFormatException e) {
                    System.err.println("Invalid number format in line: " + line + ". Error: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + ". Error: " + e.getMessage());
                }
            }
            orderTable.setItems(orders);

        } catch (IOException e) {
            showAlert("Error", "Could not load orders data: " + e.getMessage());
        }
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch();
    }
}
