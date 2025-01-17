
package com.example.ap_assignment4.Assignment3_classes;

import java.io.*;
import java.util.*;

public class ManageUserOrderHistory {

    // Global Order History Management (not tied to a username)
    public static void SaveOrderHistory(List<Order> orders) {
        String fileName = "CustomerOrderHistory.ser";

        // Check if the order already exists in the history and update it
        List<Order> existingOrders = LoadOrderHistory();

        for (Order order : orders) {
            boolean orderUpdated = false;
            for (int i = 0; i < existingOrders.size(); i++) {
                if (existingOrders.get(i).getOrderId() == order.getOrderId()) {
                    // Update the existing order
                    existingOrders.set(i, order);
                    orderUpdated = true;
                    break;
                }
            }

            if (!orderUpdated) {
                existingOrders.add(order);
            }
        }

        // Save the updated list of orders (both serialized and as a text file)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(existingOrders);
            // Also update the text file for readable format
            UpdateOrderHistoryText(existingOrders);
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }

    // Load the global order history
    public static List<Order> LoadOrderHistory() {
        String fileName = "CustomerOrderHistory.ser";
        List<Order> orderHistory = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            orderHistory = (List<Order>) ois.readObject();

            // Remove duplicate orders by Order ID, keeping the latest version
            Map<Integer, Order> uniqueOrders = new LinkedHashMap<>();
            for (Order order : orderHistory) {
                uniqueOrders.put(order.getOrderId(), order); // Overwrites duplicates
            }

            // Return a list of unique orders
            orderHistory = new ArrayList<>(uniqueOrders.values());
        } catch (FileNotFoundException e) {
            // Return empty list if file doesn't exist yet
            return orderHistory;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
        return orderHistory;
    }

    // Update the order history text file (for checking purposes)
    private static void UpdateOrderHistoryText(List<Order> orders) {
        String textFileName = "CustomerOrderHistory.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(textFileName))) {
            writer.println("Order History for ByteMe Customers:");
            writer.println("======================================");

            for (Order order : orders) {
                writer.println("Customer Name: " + order.getCustomerName());
                writer.println("\nOrder ID: " + order.getOrderId());
                writer.println("Order Status: " + order.getStatus());
                writer.println("Order Items:");
                for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                    writer.printf("%s (x%d) of Rs%.2f each = Rs%.2f%n",
                            entry.getKey().getName(),
                            entry.getValue(),
                            entry.getKey().getPrice(),
                            entry.getKey().getPrice() * entry.getValue());
                }
                writer.println("Total Order Price: Rs" + order.getOrderPrice()/2); // look in the order class (order.getOrder method)to get info
                if (order.getSplReq() != null && !order.getSplReq().isEmpty()) {
                    writer.println("Special Request: " + order.getSplReq());
                }
                writer.println("----------------------------------------");
            }
        } catch (IOException e) {
            System.out.println("Error updating order history text file: " + e.getMessage());
        }
    }

    // Helper method to get an order by ID
    public static Order getOrderById(int orderId) {
        List<Order> orders = LoadOrderHistory();
        for (Order order : orders) {
            if (order.getOrderId() == orderId) {
                return order;
            }
        }
        return null;
    }

    // Clear all orders (for testing purposes)
    public static void ClearOrderHistory() {
        String fileName = "CustomerOrderHistory.ser";
        String textFileName = "CustomerOrderHistory.txt";

        // Clear serialized file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(new ArrayList<Order>());
        } catch (IOException e) {
            System.out.println("Error clearing serialized order history: " + e.getMessage());
        }

        // Clear text file
        try (PrintWriter writer = new PrintWriter(new FileWriter(textFileName))) {
            writer.println("Order History is empty.");
        } catch (IOException e) {
            System.out.println("Error clearing order history text file: " + e.getMessage());
        }
    }
}