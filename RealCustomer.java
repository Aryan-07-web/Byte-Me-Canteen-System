package com.example.ap_assignment4.Assignment3_classes;
import com.example.ap_assignment4.OrderIdManager;

import java.util.*;

public class RealCustomer {
    private String Name;
    private Map<Item,Integer> MyCart;
    private List<Order> MyOrderHistory;
    private static final String ORDER_ID_FILE = "orderId.ser"; // File for saving CurrOrderId
    public RealCustomer(List<Item> MainMenu,String c_name){
        this.Name = c_name;
//      this.MyCart = new HashMap<>();
        this.MyCart = ByteMe.loadCartData(c_name);
//        this.MyOrderHistory = new ArrayList<>();
        this.MyOrderHistory = ByteMe.LoadOrderHistory(); // serilaization
    }
    // for junit testing only
    public RealCustomer(String c_name){
        this.Name = c_name;
        this.MyCart = new HashMap<>();
        this.MyOrderHistory = new ArrayList<>();
    }
    // Getters
    public String getName(){
        return this.Name;
    }
    public Map<Item, Integer> getMyCart() {
        return this.MyCart;
    }

    public List<Order> getMyOrderHistory() {
        return MyOrderHistory;
    }

    // New method to calculate the total order price for a given cart
    public double getOrderPrice() {
        double totalPrice = 0.0;

        // Iterate over the cart and calculate the total price
        for (Map.Entry<Item, Integer> entry : MyCart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getPrice() * quantity;  // Multiply item price by quantity
        }

        return totalPrice;
    }

    public List<Item> ViewAllItems(List<Item> menu) {
        return menu;
    }
    public Item SearchItem(String item_name,List<Item> menu) {
        for (Item i:menu) {
            if (i.getName().equalsIgnoreCase(item_name)) {
                return i;
            }
        }
        return null;
    }

    public List<Item> FilterByCategory(String item_category,List<Item> menu) {
        List<Item> filteredByCategoryItems = new ArrayList<>();
        for (Item i:menu) {
            if (i.getCategory().equalsIgnoreCase(item_category)) {
                filteredByCategoryItems.add(i);
            }
        }
        return filteredByCategoryItems;
    }

    public List<Item> SortByPrice(List<Item> menu) {
        List<Item> sortedByPriceItems = new ArrayList<>(menu);
        sortedByPriceItems.sort(Item::compareTo);

        return sortedByPriceItems;
    }


    public void AddItemToCart(Item item, int quantity) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }

        if (!item.isAvailable()) {
            // Throw an exception if the item is out of stock or not available
            throw new IllegalArgumentException("Item " + item.getName() + " is not Available");
        }
        else{
            MyCart.put(item,quantity+MyCart.getOrDefault(item,0)); // to add to existing quantity
            System.out.println("Added " + quantity + " quantity of " + item.getName() + " to the cart.");
        }
    }

    public void RemoveItemFromCart(Item item) {
        if (MyCart.containsKey(item)) {
            MyCart.remove(item);
            System.out.println("Removed " + item.getName() + " from cart.");
        }
        else {
            System.out.println("Item is not present in cart.");
        }
    }

    public void ModifyItemQuantity(Item item, int quantity) {
        if(MyCart.isEmpty()){
            throw new IllegalArgumentException("Item is not present in the cart.");
        }
        if (!MyCart.containsKey(item)) {
            throw new IllegalArgumentException("Item not in cart");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity for an item can not be negative");
        }

        if (quantity == 0) {
            RemoveItemFromCart(item);  // If quantity is 0, remove the item
        } else {
            MyCart.put(item, quantity);  // Otherwise, update the quantity
            System.out.println("Item " + item.getName() + " updated with quantity " + quantity);
        }
    }


    public double ViewTotal() {
        double totalPrice = 0.0;
        for (Map.Entry<Item, Integer> entry : MyCart.entrySet()) {
            totalPrice += entry.getKey().getPrice() * entry.getValue();
        }
        return totalPrice;
    }


public Order Checkout() {
    if (this.getMyCart().isEmpty()) {
        System.out.println("Your Cart is empty. You can only checkout if you have added items to the cart.");
        return null;
    }

    double total = ViewTotal(); // Calculate the total price of the order
    System.out.println("Your total amount to be paid is: " + total);
    int order_id = OrderIdManager.getNextOrderId();

    // Deep copy of the cart to ensure order retains its items independently
    Map<Item, Integer> orderItems = new HashMap<>();
    for (Map.Entry<Item, Integer> entry : this.getMyCart().entrySet()) {
        orderItems.put(entry.getKey(), entry.getValue());
    }

    // Create the new order with a deep copy of the cart
    Order order = new Order(order_id, "Pending", this.getName(), orderItems, total);
    MyOrderHistory.add(order);

    System.out.println("Order ID " + order.getOrderId() + " has been placed successfully!");
    return order;
}


    public String ViewOrderStatus(int orderId) {
        // Retrieve the order from the order history
//        Order order = ManageUserOrderHistory.getOrderById(this.(), orderId);
        Order order = ManageUserOrderHistory.getOrderById(orderId);
        // If the order is found, return the status; otherwise, return a message indicating the order wasn't found.
        if (order != null && order.getCustomerName().equals(this.getName())) {
            return "Status for Order ID " + orderId + ": " + order.getStatus();
        }
        else {
            return "Order with ID " + orderId + " not found or it is not your Order";
        }
    }

// cancelled orders will be refunded by admin
    public void CancelOrder(int orderId) {
        for (Order order : MyOrderHistory) {
            if (order.getOrderId() == orderId) {
                if (order.getStatus().equals("Pending")) {
                    order.setStatus("Cancelled");
                    System.out.println("Order ID " + orderId + " has been cancelled.");
                    return;
                }
                else {
                    System.out.println("Only Pending Orders can be cancelled");
                    return;
                }
            }
        }
        System.out.println("Order ID " + orderId + " not found.");
    }
    public void ViewOrderHistory() {
//    List<Order> orderHistory = ManageUserOrderHistory.LoadUserOrderHistory(this.getName());
//        List<Order> orderHistory = ManageUserOrderHistory.LoadOrderHistory();
        List<Order> MyOrders = this.getMyOrderHistory();
    if (MyOrders.isEmpty()) {
        System.out.println("No past orders found.");
    } else {
        System.out.println("Your Order History: ");
        for (Order order : MyOrders) {
            if(order.getCustomerName().equals(this.getName())) {
                System.out.println("Order ID: " + order.getOrderId() + ", Status: " + order.getStatus());
            }
        }
    }
}
}


