package com.example.ap_assignment4;
import java.io.*;

public class OrderIdManager {
    private static final String ORDER_ID_FILE = "orderId.ser"; // to read and write
    private static int currOrderId = 1; // Default if no data

    // Private constructor to prevent instantiation
    private OrderIdManager() {}

    // Load current order ID from file
    public static void loadOrderId() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ORDER_ID_FILE))) {
            currOrderId = ois.readInt();
        } catch (FileNotFoundException e) {
            System.out.println("Order ID file not found. Starting with Order ID 1.");
        } catch (IOException e) {
            System.err.println("Error loading Order ID: " + e.getMessage());
        }
    }

    // Save current order ID to file
    public static void saveOrderId() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDER_ID_FILE))) {
            oos.writeInt(currOrderId);
        } catch (IOException e) {
            System.err.println("Error saving Order ID: " + e.getMessage());
        }
    }

    // Get and increment the current order ID
    public static int getNextOrderId() {
        int orderId = currOrderId;
        currOrderId++;
        saveOrderId(); // Save updated ID immediately
        return orderId;
    }
}
