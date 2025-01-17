package com.example.ap_assignment4.Assignment3_classes;
import java.util.*;  // for using different data structures

public class RealAdmin {
    private List<Item> Menu;
    private List<Order> CustomerOrders;
    public RealAdmin(){
        Menu = new ArrayList<>();
//        CustomerOrders = new ArrayList<>();
        CustomerOrders = ByteMe.LoadOrderHistory();

    }
    // getters
    public List<Item> getMenu() {
        return Collections.unmodifiableList(Menu); // ensures encapsulation
    }

    public List<Order> getPendingOrders() {
        return Collections.unmodifiableList(CustomerOrders); // ensures encapsulation
    }
    public List<Order> getCustomerOrders(){
        return CustomerOrders;
    }
    // Menu management methods

    public void AddItem(String item_name, double item_price, boolean item_available, String item_category) {
        Item newItem = new Item(item_name,item_price,item_available,item_category);
        Menu.add(newItem);
        System.out.println("Item " + item_name + " has been added");
    }
    public Order findOrderById(int order_id){
        for(Order order:CustomerOrders){
            if(order.getOrderId() == order_id){
                return order;
            }
        }
        return null;
    }
    public void UpdateExistingItem(String item_name, double item_cost, boolean item_available, String item_category) {
        for(Item item:Menu){
            if(item.getName().equalsIgnoreCase(item_name)){
                item.setCategory(item_category);
                item.setAvailable(item_available);
                item.setPrice(item_cost);
                System.out.println("Item " + item_name + " has been updated");
                return;
            }
        }
        System.out.println("Item " + item_name + " not found");
    }

    public void RemoveItem(String item_name) {
        if(Menu.isEmpty()){
            System.out.println("No items present in menu yet");
        }
        else {
            for(Item i:Menu){
                if(i.getName().equalsIgnoreCase(item_name)){
                    Menu.remove(i);
                    System.out.println("Item " + item_name + " has been removed");
                    return;
                }
            }
            System.out.println("Item " + item_name + " not found in menu");
        }
    }

    public void ModifyItemPrice(String item_name, double item_newPrice) {
        for(Item item:Menu){
            if(item.getName().equalsIgnoreCase(item_name)){
                item.setPrice(item_newPrice);
                System.out.println("Price for " + item_name + " has been updated");
                return;
            }
        }
        System.out.println("Item " + item_name + " not found in menu");
    }

    public void UpdateItemAvailable(String item_name, boolean item_available) {
        for(Item item:Menu){
            if(item.getName().equalsIgnoreCase(item_name)){
                item.setAvailable(item_available);
                System.out.println("Availability for " + item_name + " has been updated");
                return;
            }
        }
        System.out.println("Item " + item_name + " not found in menu");
    }

    public void ViewPendingOrders(){
        if(CustomerOrders.isEmpty()){
            System.out.println("No Pending Orders yet");
            return;
        }
        // print all pending orders
        System.out.println("Pending Orders are: ");
        for(Order order:CustomerOrders){
            if(order.getStatus().equals("Pending")) {
                System.out.println(order.getOrderId());
            }
        }
    }

    public void UpdateOrderStatus(int order_id, String order_status) {
        for(Order order:CustomerOrders){
            if(order.getOrderId() == order_id){
                order.setStatus(order_status);
                System.out.println("Status for Order " + order_id + " has been updated");
                return;
            }
        }
        System.out.println("Order " + order_id + " not found");
    }

    // move orders to completed section (only for the pending orders)
    public void ProcessOrder(int order_id) {
        if(CustomerOrders.isEmpty()){
            System.out.println("No Customer Orders yet");
        }
        for (Order order : CustomerOrders) {
            if (order.getOrderId() == order_id && order.getStatus().equalsIgnoreCase("Pending")) {
                order.setStatus("Completed");
                System.out.println("Order " + order_id + " has been processed.");
                for (Map.Entry<Item, Integer> entry : order.getItems().entrySet()) {
                    String itemName = entry.getKey().getName();
                    int quantity = entry.getValue();
                    double pricePerUnit = 0.0;
                    for (Item item : Menu) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            pricePerUnit = item.getPrice();
                            break;
                        }
                    }
                }
                return;
            }
        }
        System.out.println("Either the Order was already processed or not found");
        System.out.println("Customer with Order id " + order_id + ", please check your order status");
    }

    public void ProcessRefund(int order_id) {
        for (Order order:CustomerOrders) {
            if (order.getOrderId() == order_id && order.getStatus().equalsIgnoreCase("Cancelled")) {
                order.setStatus("Refunded");
                System.out.println("Refund processed for Order : " + order_id);
                return;
            }
        }
        System.out.println("Order either not eligible for refund or not found with ID: " + order_id);
    }
    public void HandleSpecialRequest(int order_id, String special_request) {
        for (Order order:CustomerOrders) {
            // if the order has a special request
            if (order.getOrderId() == order_id && !order.getSplReq().isEmpty()) {
                System.out.println("Special Request for Order " + order_id + " is " + order.getSplReq());
                System.out.println("Special Request for Order " + order_id + " has been handled");
                return;
            }
            // if order does not have a special request
            else if(order.getOrderId() == order_id && order.getSplReq().isEmpty()){
                System.out.println("No Special Request for Order " + order_id);
            }
        }
        System.out.println("Order " + order_id + " not found");
    }


    public void generateDailySalesReport() {
        if (CustomerOrders.isEmpty()) {
            System.out.println("No orders placed today.");
            return;
        }
        double OrderAmount = 0.0;
        int NoOfOrders = 0;
        for(Order order:CustomerOrders){
            if(order.getStatus().equals("Completed")){
                OrderAmount+=order.getOrderPrice();
                NoOfOrders++;
            }
        }
        System.out.println("Total Completed Order Sale: Rs. " + OrderAmount);
        System.out.println("Total No of Completed Orders Made: " + NoOfOrders);
        System.out.println("Displaying Orders");
        int sno = 1;
        for(Order order:CustomerOrders){
            System.out.println("S No. " + sno + " order id : " + order.getOrderId() + " order price : " +order.getOrderPrice() + " order status : " + order.getStatus());
            sno++;
        }
    }
}
