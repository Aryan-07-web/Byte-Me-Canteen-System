package com.example.ap_assignment4.Assignment3_classes;
import com.example.ap_assignment4.OrderIdManager;

import java.io.*;
import java.util.*;
public class ByteMe {
    private static final String admin_pass = "ByteMe@123";
    private static final RealAdmin ByteMeAdmin = new RealAdmin();  // whosoever logins as admin will get access of it with assumption that there will be only 1 admin of the whole system
    private static final Map<String,RealCustomer> ByteMeCustomers = new HashMap<>();  // <CustomerName,Customer>
    private static final Scanner sc = new Scanner(System.in);
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDER_FILE = "orders.txt";

    public static void saveMenuData(List<Item> menu) {
        // if the menu file does not exist then the filewriter automatically creates it and if already created then the data is overwritten
        try (PrintWriter writer = new PrintWriter(new FileWriter(MENU_FILE))) {
            for (Item item : menu) {
                writer.println(item.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving menu data: " + e.getMessage());
        }
    }
    public static void saveOrdersData(List<Order> orders) {
        // if the menu file does not exists then the filewriter automatically creates it and if already created then the data is overwritten
        try (PrintWriter writer = new PrintWriter(new FileWriter(ORDER_FILE))) {
            for (Order order : orders) {
                writer.println(order.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving orders data: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        OrderIdManager.loadOrderId(); //load
        // some pre added items
        if(ByteMeAdmin.getMenu().isEmpty()) {
            ByteMeAdmin.AddItem("Paneer", 150.0, true, "Veg");
            ByteMeAdmin.AddItem("Dal", 120.0, true, "Veg");
            ByteMeAdmin.AddItem("Aloo", 100.0, true, "Veg");
            ByteMeAdmin.AddItem("Chicken", 200.0, true, "NonVeg");
            ByteMeAdmin.AddItem("Omelette", 50.0, true, "NonVeg");
        }



        boolean isOperating = true;
        System.out.println("Welcome to ByteMe Canteen Online Application...");
        while(isOperating){
            System.out.println("1. Admin ");
            System.out.println("2. Customer ");
            System.out.println("3. Exit ");
            System.out.println("Enter Your Choice: ");
            int Choice_One = sc.nextInt();
            sc.nextLine();
            switch (Choice_One){
                case 1:
                    if(AdminCheck(sc)){
                        AdminView(sc);
                    }
                    else{
                        System.out.println("Invalid Password.. Try Again");
                    }
                    break;
                case 2:
                    System.out.println("Enter Customer Name: ");
                    String ByteMeCustomerName = sc.nextLine();
                    // if the ByteMeCustomer name already exists in the map then it returns the key else insert into the map
                    RealCustomer customer = ByteMeCustomers.computeIfAbsent(ByteMeCustomerName, name -> new RealCustomer(ByteMeAdmin.getMenu(),ByteMeCustomerName));
                    CustomerView(customer,sc);
                    break;
                case 3:
                    // save both data in both the separate files
                    saveMenuData(ByteMeAdmin.getMenu());
                    saveOrdersData(ByteMeAdmin.getPendingOrders());
                    System.out.println("Exiting ByteMe... ");
                    isOperating = false;
                    break;
                default:
                    System.out.println("Invalid Choice.. Try Again");
            }
        }
    }
    public static boolean AdminCheck(Scanner sc){
        System.out.println("Enter Admin Password: ");
        String pass = sc.nextLine();
        return pass.equals(admin_pass);
    }
    public static void AdminView(Scanner sc){
        boolean AdminOnline = true;
        while(AdminOnline){
            System.out.println("Welcome To Admin View: ");
            System.out.println("1 -> Add New Item");
            System.out.println("2 -> Update Existing Item");
            System.out.println("3 -> Remove Item");
            System.out.println("4 -> Modify Prices");
            System.out.println("5 -> Update Availability");
            System.out.println("6 -> View Pending Orders");
            System.out.println("7 -> Update Order Status");
            System.out.println("8 -> Process Refund");
            System.out.println("9 -> Handle Special Requests");
            System.out.println("10 -> Generate Daily Sales Report");
            System.out.println("11 -> Process the Orders");
            System.out.println("12 -> Back");


            System.out.println("Enter Your Choice: ");
            int admin_choice = sc.nextInt();
            sc.nextLine();

            switch (admin_choice) {
                case 1:
                    System.out.println("Enter Item Name: ");
                    String name = sc.nextLine();
                    System.out.println("Enter Item Price: ");
                    double price = sc.nextDouble();
                    System.out.println("Is Item Available(true/false): ");
                    boolean available = sc.nextBoolean();
                    sc.nextLine();
                    System.out.println("Enter Item Category: ");
                    String category = sc.nextLine();
                    ByteMeAdmin.AddItem(name, price, available, category);
                    break;
                case 2:
                    System.out.println("Enter Item Name to Update: ");
                    String i_name = sc.nextLine();
                    System.out.println("Enter New Price: ");
                    double i_price = sc.nextDouble();
                    System.out.println("Is Item Available(true/false): ");
                    boolean i_available = sc.nextBoolean();
                    sc.nextLine();
                    System.out.println("Enter New Category: ");
                    String i_category = sc.nextLine();
                    ByteMeAdmin.UpdateExistingItem(i_name, i_price, i_available, i_category);
                    break;
                case 3:
                    System.out.println("Enter Item Name to Remove: ");
                    String RemoveName = sc.nextLine();
                    ByteMeAdmin.RemoveItem(RemoveName);
                    break;
                case 4:
                    System.out.println("Enter Item Name to Modify Price: ");
                    String modify_name = sc.nextLine();
                    System.out.println("Enter New Price: ");
                    double newPrice = sc.nextDouble();
                    ByteMeAdmin.ModifyItemPrice(modify_name, newPrice);
                    break;
                case 5:
                    System.out.println("Enter Item Name to Update Availability: ");
                    String ItemName = sc.nextLine();
                    System.out.println("Is Item Available? (true/false): ");
                    boolean ItemAvailable = sc.nextBoolean();
                    ByteMeAdmin.UpdateItemAvailable(ItemName, ItemAvailable);
                    break;
                case 6:
                    ByteMeAdmin.ViewPendingOrders();
                    break;
                case 7:
                    System.out.println("Enter Order ID to Update Status: ");
                    int orderId = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter New Status: ");
                    String status = sc.nextLine();
                    ByteMeAdmin.UpdateOrderStatus(orderId, status);
                    // Get the updated order and serialize it
                    Order updatedOrder = ByteMeAdmin.findOrderById(orderId);
                    if (updatedOrder != null) {
//                        ManageUserOrderHistory.SaveUserOrderHistory(updatedOrder.getCustomerName(), updatedOrder);
                        ManageUserOrderHistory.SaveOrderHistory(ByteMeAdmin.getCustomerOrders());
//                        SaveOrderHistory(); // Save all orders after update
                        System.out.println("Order status updated and saved.");
                    }
                    break;
                case 8:
                    System.out.println("Enter Order ID to Process Refund: ");
                    int OrderId = sc.nextInt();
                    ByteMeAdmin.ProcessRefund(OrderId);
                    Order refundedOrder = ByteMeAdmin.findOrderById(OrderId);
                    if (refundedOrder != null) {
//                        ManageUserOrderHistory.SaveUserOrderHistory(refundedOrder.getCustomerName(), refundedOrder);

//                        List<Order> LatestOrders = ManageUserOrderHistory.LoadOrderHistory();
                        ManageUserOrderHistory.SaveOrderHistory(ByteMeAdmin.getCustomerOrders());
//                        SaveOrderHistory();
//                        ManageUserOrderHistory.SaveOrderHistory(LatestOrders);
                        System.out.println("Refund processed and order history saved.");
                    }
                    break;
                case 9:
                    System.out.println("Enter Order ID to handle Special Request: ");
                    int order_id = sc.nextInt();
                    sc.nextLine();
                    System.out.println();
                    System.out.println("Handle Special Request: ");
                    String request = sc.nextLine();
                    ByteMeAdmin.HandleSpecialRequest(order_id, request);
                    break;
                case 10:
                    ByteMeAdmin.generateDailySalesReport();
                    break;
                case 11:
                    System.out.print("Enter Order ID to Process: ");
                    int ProcessOrderId = sc.nextInt();
                    ByteMeAdmin.ProcessOrder(ProcessOrderId);
                    Order processedOrder = ByteMeAdmin.findOrderById(ProcessOrderId);
                    // if exists
                    if (processedOrder != null) {
//                        ManageUserOrderHistory.SaveUserOrderHistory(processedOrder.getCustomerName(), processedOrder);
                        ManageUserOrderHistory.SaveOrderHistory(ByteMeAdmin.getCustomerOrders());
//                        SaveOrderHistory();
                        System.out.println("Order processed and history saved.");
                    }
                    break;
                case 12:
                    SaveOrderHistory();
                    AdminOnline = false;
                    break;
                default:
                    System.out.println("Invalid Choice.. Try Again");
            }
        }
    }


    public static void SaveCartData(String UserName, Map<Item, Integer> cart) {
        String fileName = UserName + "_cart.ser";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(cart); // Serialize and write the cart map
        } catch (IOException e) {
            System.out.println("Error saving cart: " + e.getMessage());
        }
        // to save o/p file as a .txt file also
        SaveCartAsText(UserName,cart);
    }
    public static void SaveCartAsText(String userName, Map<Item, Integer> cart) {
        String textFileName = userName + "_cart.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(textFileName))) {
            writer.println("Cart for " + userName + ":");
            if (cart.isEmpty()) {
                writer.println("Cart is empty.");
            }
            else {
                for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
                    Item item = entry.getKey();
                    int quantity = entry.getValue();
                    writer.println("Item: " + item.getName() + ", Quantity: " + quantity + ", Price: " + item.getPrice());
                }
            }
            writer.println("Total Price: Rs " + calculateCartTotal(cart));
        } catch (IOException e) {
            System.out.println("Error saving cart as text: " + e.getMessage());
        }
    }
    public static double calculateCartTotal(Map<Item, Integer> cart) {
        double total = 0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            total += item.getPrice() * quantity;
        }
        return total;
    }

    public static Map<Item, Integer> loadCartData(String userName) {
        String fileName = userName + "_cart.ser";
        Map<Item, Integer> cart = new HashMap<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            cart = (Map<Item, Integer>) ois.readObject(); // Deserialize the cart map
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return an empty cart
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading cart: " + e.getMessage());
        }

        return cart;
    }
    public static List<Order> LoadOrderHistory() {
        String fileName = "CustomerOrderHistory.ser";
        List<Order> orders = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            orders = (List<Order>) ois.readObject(); // Deserialize the order list
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return an empty list
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }

        return orders;
    }
    public static void SaveOrderHistory() {
        String fileName = "order_history.ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(ByteMeAdmin.getCustomerOrders()); // Serialize the order list
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }
    public static void clearCartData(String userName) {
        String serializedFileName = userName + "_cart.ser";
        String textFileName = userName + "_cart.txt";

        // Clear cart data in the .ser file
        File cartFile = new File(serializedFileName);
        if (cartFile.exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(cartFile))) {
                oos.writeObject(new HashMap<Item, Integer>()); // Write an empty cart
                System.out.println("Cleared serialized cart for " + userName);
            } catch (IOException e) {
                System.out.println("Error clearing serialized cart for " + userName + ": " + e.getMessage());
            }
        }

        // Write an empty cart to the .txt file
        try (PrintWriter writer = new PrintWriter(new FileWriter(textFileName))) {
            writer.println("Cart for " + userName + ":");
            writer.println("Cart is empty.");
            writer.println("Total Price: Rs 0.0");
            System.out.println("Cleared text cart for " + userName);
        } catch (IOException e) {
            System.out.println("Error clearing text cart for " + userName + ": " + e.getMessage());
        }
    }
    public static void LoadMenuData() {
        File menuFile = new File(MENU_FILE);
        if (!menuFile.exists()) {
            System.out.println("Menu file not found. Starting with a default menu.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    boolean available = Boolean.parseBoolean(parts[2].trim());
                    String category = parts[3].trim();
                    ByteMeAdmin.AddItem(name, price, available, category);
                }
            }
            System.out.println("Menu loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading menu: " + e.getMessage());
        }
    }
    public static Item findItemByName(String itemName) {
//        ManageUserOrderHistory
        if (ByteMeAdmin.getMenu() == null || ByteMeAdmin.getMenu().isEmpty()) {
            System.err.println("Menu is empty or not initialized.");
            return null;
        }
        for (Item item : ByteMeAdmin.getMenu()) {
            if (item.getName().trim().equalsIgnoreCase(itemName.trim())) {
                return item; // Return matched item
            }
        }
        // Log missing item for debugging
        System.err.println("Item not found: " + itemName);
        return null;
    }

    public static void CustomerView(RealCustomer Customer, Scanner sc) {
        boolean CustomerOnline = true;
        while (CustomerOnline) {
            System.out.println("Welcome To Customer View: ");
            System.out.println("1 -> View All Items");
            System.out.println("2 -> Search Item");
            System.out.println("3 -> Filter by Category");
            System.out.println("4 -> Sort by Price");
            System.out.println("5 -> Add Item to Cart");  // for order
            System.out.println("6 -> Modify Cart Item Quantity");  //for order
            System.out.println("7 -> Remove Item from Cart");  // for order
            System.out.println("8 -> View Cart Total");
            System.out.println("9 -> Checkout");// order of customer will be recorded  (only case where order is created)
            System.out.println("10 -> View Order Status");
            System.out.println("11 -> Cancel Order");
            System.out.println("12 -> View Order History");
            System.out.println("13 -> Back");

            System.out.print("Enter Your Choice: ");
            int Customer_choice = sc.nextInt();
            sc.nextLine();
            switch(Customer_choice){
                case 1:
                    List<Item> DisplayMenu = Customer.ViewAllItems(ByteMeAdmin.getMenu());
                    if(DisplayMenu.isEmpty()){
                        System.out.println("No items in the menu yet");
                        break;
                    }
                    else{
                        for(Item i:DisplayMenu){
                            System.out.println("Item Name: " + i.getName());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Enter item name to search: ");
                    String ItemToSearchName = sc.nextLine();
                    Item ItemToSearch = Customer.SearchItem(ItemToSearchName,ByteMeAdmin.getMenu());
                    if (ItemToSearch!=null){
                        System.out.println("Item Name: " + ItemToSearch.getName() + ", Item Price: " + ItemToSearch.getPrice() + ", Item Category: " + ItemToSearch.getCategory());
                    }
                    else{
                        System.out.println("Item not found");
                    }
                    break;
                case 3:
                    System.out.println("Enter category to filter by: ");
                    String ItemCategory = sc.nextLine();
                    List<Item> CategoryItems = Customer.FilterByCategory(ItemCategory,ByteMeAdmin.getMenu());
                    if(CategoryItems.isEmpty()){
                        System.out.println("No Items present in this Category");
                    }
                    else {
                        System.out.println("Items with Category: " + ItemCategory);
                        for (Item i : CategoryItems) {
                            System.out.println(i.getName());
                        }
                    }
                    break;
                case 4:
                    List<Item> SortedItems =  Customer.SortByPrice(ByteMeAdmin.getMenu());
                    if(SortedItems.isEmpty()){
                        System.out.println("No Items to Sort By Price");
                    }
                    else {
                        for (Item i : SortedItems) {
                            System.out.println("Item Name: " + i.getName() + ", Item Price: " + i.getPrice());
                        }
                    }
                    break;
                case 5:
                    System.out.println("Enter item name to add to cart: ");
                    String itemName = sc.nextLine();
                    System.out.println("Enter quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    Item ItemToAdd = null;
                    for (Item i:ByteMeAdmin.getMenu()) {
                        if (i.getName().equalsIgnoreCase(itemName)) {
                            ItemToAdd = i;
                            break;
                        }
                    }

                    if (ItemToAdd != null) {
                        if(ItemToAdd.isAvailable()) {
                            Customer.AddItemToCart(ItemToAdd, quantity);
//                            ManageUserOrderHistory.SaveCart(Customer.getName(),Customer.getMyCart());
                            ByteMe.SaveCartData(Customer.getName(),Customer.getMyCart());
//                            CartManager.saveCart(Customer.getName(),Customer.getMyCart(),ByteMeAdmin.getMenu());
                        }
                        else{
                            System.out.println("Item " + itemName + " is not available");
                        }
                    }
                    else {
                        System.out.println("Item " + itemName + " not found in the menu.");
                    }
                    break;
                case 6:
                    if(ByteMeAdmin.getMenu().isEmpty()){
                        System.out.println("No items in the menu yet");
                        break;
                    }
                    System.out.print("Enter item name in cart to modify: xx");
                    String modifyItemName = sc.nextLine();
                    System.out.print("Enter new quantity: ");
                    int newQuantity = sc.nextInt();
                    sc.nextLine();
                    Item ItemToModify = null;
                    for (Item i:ByteMeAdmin.getMenu()) {
                        if (i.getName().equalsIgnoreCase(modifyItemName)) {
                            ItemToModify = i;
                            break;
                        }
                    }

                    if (ItemToModify != null) {
                        Customer.ModifyItemQuantity(ItemToModify,newQuantity);
//                        ManageUserOrderHistory.SaveCart(Customer.getName(), Customer.getMyCart());
                        ByteMe.SaveCartData(Customer.getName(),Customer.getMyCart());
//                        CartManager.saveCart(Customer.getName(),Customer.getMyCart(),ByteMeAdmin.getMenu());

                    }
                    else {
                        System.out.println("Item " + modifyItemName + " not found in the menu.");
                    }
                    break;
                case 7:
                    if(ByteMeAdmin.getMenu().isEmpty()){
                        System.out.println("No items in the menu yet");
                        break;
                    }
                    System.out.print("Enter item name to remove from cart: ");
                    String removeItemName = sc.nextLine();
                    Item ItemToRemove = null;
                    for (Item i:ByteMeAdmin.getMenu()) {
                        if (i.getName().equalsIgnoreCase(removeItemName)) {
                            ItemToRemove = i;
                            break;
                        }
                    }
                    if (removeItemName != null) {
                        Customer.RemoveItemFromCart(ItemToRemove);
//                        ManageUserOrderHistory.SaveCart(Customer.getName(), Customer.getMyCart());
                        ByteMe.SaveCartData(Customer.getName(),Customer.getMyCart());
//                        CartManager.saveCart(Customer.getName(),Customer.getMyCart(),ByteMeAdmin.getMenu());
                    }
                    else {
                        System.out.println("Item " + removeItemName + " not found in the menu.");
                    }
                case 8:
                    double CustomerCartPrice = Customer.ViewTotal();
                    System.out.println("Cart Price: Rs " + CustomerCartPrice);
                    break;
                case 9:
                    // issue here
                    Order CustomerOrder = Customer.Checkout();
                    if(CustomerOrder == null){
                        System.out.println("Order was not placed");
                        return;
                    }
                    // add to admin list
                    ByteMeAdmin.getCustomerOrders().add(CustomerOrder);

                    // handle special requests for any order
                    System.out.println("Do you want to add any Special requests(Yes/No) for the order " + CustomerOrder.getOrderId());
                    String specialChoice = sc.nextLine();
                    if(specialChoice.equals("Yes")){
                        System.out.println("Enter Special Request: ");
                        String CustomerReq = sc.nextLine();
                        CustomerOrder.AddSplReq(CustomerReq);
                        System.out.println("Your Special Request has been submitted ");
                    }
                    // no
                    else{
                        System.out.println("No Special Requests");
                    }
                    Customer.getMyCart().clear();
//                    CustomerOrder.getItems().putAll(Customer.getMyCart()); // Order items map will be populated now
                    // Save order history for the user
//                    ManageUserOrderHistory.SaveUserOrderHistory(Customer.getName(),CustomerOrder);
                    ManageUserOrderHistory.SaveOrderHistory(ByteMeAdmin.getCustomerOrders());
                    System.out.println("Order saved successfully");
//                    ManageUserOrderHistory.ClearCart(Customer.getName());
                    ByteMe.clearCartData(Customer.getName());
//                    CartManager.clearCart(Customer.getName());
                    System.out.println("Cart data for " + Customer.getName() + " has been cleared");
                    break;
                case 10:
                    System.out.println("Enter order id to View Status: ");
                    int order_id = sc.nextInt();
                    sc.nextLine();
                    String Status = Customer.ViewOrderStatus(order_id);
                    System.out.println("Status for Order id " + order_id + " is :" + Status);
                    break;
                case 11:
                    System.out.println("Enter order id to Cancel: ");
                    int order_id_cancel = sc.nextInt();
                    sc.nextLine();
                    Customer.CancelOrder(order_id_cancel);
                    ManageUserOrderHistory.SaveOrderHistory(ByteMeAdmin.getCustomerOrders());
                    break;
                case 12:
                    Customer.ViewOrderHistory();
                    break;
                case 13:
                    CustomerOnline = false;
                    break;
                default:
                    System.out.println("Invalid Choice.. Try Again");
            }
        }
    }
}
