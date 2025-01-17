Included files: 
1.	RealAdmin.java -> Models all the functionalities the admin can perform.
2.	RealCustomer.java -> Models all the functionalities the customer can perform.
3.	Item.java -> Models an Item from the menu and its related properties.
4.	Order.java -> Models an Order consisting of one or more than one item.
5.	OrderHistory.java -> The customer must record all the orders made.
6.	ByteMe.java (which behaves as a main/ running file) -> contains logic that monitors the flow of the app(as a Command line interface).

Assumptions Used in My Code:
1.	RealAdmin.java

getMenu() - Returns an unmodifiable view of the Menu list to ensure encapsulation and prevent modification.
getPendingOrders() - Returns an unmodifiable view of CustomerOrders to allow read-only access to orders.
getCustomerOrders() - Returns the CustomerOrders list directly, allowing access to all customer orders.
AddItem() - Adds a new item to the Menu list, given its name, price, availability, and category.
UpdateExistingItem() - Updates an existing menu item’s price, availability, and category based on the item’s name.
RemoveItem() - Removes an item from the menu if it matches the provided name.
ModifyItemPrice() - Updates the price of a specified item in the menu.
UpdateItemAvailable() - Updates the availability status of an item in the menu.
ViewPendingOrders() - Displays all customer orders that are in the "Pending" status.
UpdateOrderStatus() - Updates the status of an order based on the provided order ID.
ProcessOrder() - Marks a pending order as completed.
ProcessRefund() - Processes refund for a canceled order.
HandleSpecialRequest() - Handles special requests associated with a specific order ID.
generateDailySalesReport() - Generates a daily sales report, calculating the total revenue and displaying all orders.
 
2.	RealCustomer.java

getMyCart() - Returns the current cart items and quantities.
getMyOrderHistory() - Returns the customer's order history.
getCurrOrderId() - Returns the current order ID.
ViewAllItems() - Displays all items in the provided menu for the customer.
SearchItem() - Searches for an item by name in the menu.
FilterByCategory() - Filters items by category from the menu.
SortByPrice() - Sorts items by price in ascending order.
AddItemToCart() - Adds an item with a specified quantity to the customer's cart.
RemoveItemFromCart() - Removes an item from the customer’s cart.
ModifyItemQuantity() - Modifies the quantity of a specified item in the customer’s cart.
ViewTotal() - Returns the total price of all items in the customer’s cart.
Checkout() - Completes checkout, clears cart, adds order to history, displays the cart's total price, and asks customers to add any special requests.
(Note -> Customer order will be pending and will be processed by the admin. Customer can also do more orders when one order is pending.)
ViewOrderStatus() - Returns the status of any specific order by ID.
CancelOrder() - Cancels a pending order by ID.
ViewOrderHistory() - Displays all orders made by a customer.
	
3.	Item.java

getName() - Returns the name of the item.
getPrice() - Returns the price of the item.
isAvailable() - Returns the availability status of the item.
getCategory() - Returns the category of the item.
setName() - Updates the name of the item.
setPrice() - Updates the price of the item.
setAvailable() - Updates the availability status of the item.
setCategory() - Updates the category of the item.
compareTo() - Compares the item price with another item's price.

4.	Order.java

getStatus() - Returns the current status of the order.
setStatus() - Updates the status of the order.
getItems() - Returns the items and quantities in the order.
getOrderPrice() - Calculates and returns the total price of the order based on items and quantities.
getOrderId() - Returns the unique order ID.
AddSplReq() - Adds a special request to the order.
getSplReq() - Retrieves the special request associated with the order.
	
5.	ByteMe.java

Firstly, we welcome the user to the ByteMeCanteen System, ask if the user is an Admin or a Customer, and display the functionalities of each based on the user’s choice(using switch case statements).

Assumptions for Assignment-4
1. New Files

- ManageUserOrderHistory.java
- HelloApplication.java
- HelloController.java
- OrderIdManager.java
- Test_Cases.java (Test Section)

2. Workflow

First, there are two files, orders.txt and menu.txt, utilized within ByteMe.java. These files store data when the user exits the ByteMe.java application. The HelloApplication.java reads this data, splits it into components, and adds the respective entries into the menu and orders tables. The logic for loading the menu screen, loading the orders screen, displaying the menu screen, and displaying the orders screen is handled exclusively in HelloApplication.java. The graphical user interface (GUI) for the menu and pending orders is displayed when the application runs and we can switch between the 2 GUI’s also.

Additionally, a file named CustomerOrderHistory.txt is created to record all customer orders within the ByteMe canteen system, ensuring serialization. Cart files for Customers are also generated to reflect real-time changes during a session, which is defined as the time between the customer's entry into the system and their exit.

Finally, JUnit testing is implemented in the test section, where the file Test_Cases.java contains the necessary checks.

Note:- Serialization is implemented wherever needed as mentioned in this document by the .ser files but .txt files are also created for reading purposes only.

Conclusion
Development of an Online Canteen System and GUI Screens using Java collections, JavaFx and basic OOP principles for effective functionality.


