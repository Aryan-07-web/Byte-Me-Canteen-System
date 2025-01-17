package com.example.ap_assignment4.Assignment3_classes;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class Test_Cases {
    private RealCustomer customer;
    private Item paneerItem;
    private Item DalItem;
    private Item AlooItem;
    private RealAdmin ByteMeAdmin;

    @Before
    public void setUp() {
        ByteMeAdmin = new RealAdmin();
        ByteMeAdmin.AddItem("Paneer", 150.0, false, "Veg");
        ByteMeAdmin.AddItem("Dal", 150.0, true, "Veg");
        ByteMeAdmin.AddItem("Aloo", 100.0, true, "Veg");
        customer = new RealCustomer("Customer1");
        paneerItem = ByteMeAdmin.getMenu().stream()
                .filter(item -> item.getName().equals("Paneer"))
                .findFirst()
                .orElse(null);
        DalItem = ByteMeAdmin.getMenu().stream()
                .filter(item -> item.getName().equals("Dal"))
                .findFirst()
                .orElse(null);
        AlooItem = ByteMeAdmin.getMenu().stream()
                .filter(item -> item.getName().equals("Aloo"))
                .findFirst()
                .orElse(null);
    }

    @Test
    public void testOrderOutOfStockItem() {  // test case 1
        assertNotNull(paneerItem);
        assertFalse(paneerItem.isAvailable());
        String errorMessage = "";
        try {
            customer.AddItemToCart(paneerItem, 1);
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
        assertEquals("Item Paneer is not Available", errorMessage);  // error message is the actual
    }
    @Test
    public void testModifyItemInEmptyCart() {
        // cart is initially empty
        assertTrue(customer.getMyCart().isEmpty());
        // Attempt to modify an item in the empty cart
        String errorMessage = "";
        try {
            customer.ModifyItemQuantity(paneerItem, 1); // Trying to modify an item not in the cart
        } catch (IllegalArgumentException e) {
            errorMessage = e.getMessage();
        }
        // Verify that the appropriate error message is returned
        assertEquals("Item is not present in the cart.", errorMessage);
    }
    @Test
    public void testAddingItemToCart() {    // test case 2
        // Ensure the item is available and not in the cart initially
        assertNotNull(DalItem);
        assertFalse(customer.getMyCart().containsKey(DalItem));
        // Add item to the cart
        customer.AddItemToCart(DalItem, 1);
        // Check that the cart now contains the item
        assertTrue(customer.getMyCart().containsKey(DalItem));
        // Verify that the total price is updated correctly (1 item at 150.0)
        assertEquals(150.0,customer.getOrderPrice(), 0.01);
    }
    @Test
    public void testMultipleItemPriceInCart() {
        // add item 1 to cart
        customer.AddItemToCart(DalItem,2);
        // add item 2 to cart
        customer.AddItemToCart(AlooItem,3);
        // verify that the price for multiple items is correctly calculated in cart
        assertEquals(600.0,customer.getOrderPrice(),0.01);
    }
    @Test
    public void testModifyingItemQuantityInCart() {   // test case 3
        // Add 1 Dal item to the cart
        customer.AddItemToCart(DalItem, 1);
        // Modify the quantity of the Dal item (now adding 2 more)
        customer.ModifyItemQuantity(DalItem, 3); // 3 total Paneer items
        // Verify the total price (3 items at 150.0 each)
        assertEquals(450.0, customer.getOrderPrice(), 0.01);
    }
    @Test(expected = IllegalArgumentException.class) // to catch if exception is thrown
    public void testPreventNegativeQuantity() {   //test case 4
        // Attempt to set a negative quantity for an item in the cart
        customer.ModifyItemQuantity(DalItem, -1);  // exception will be thrown
    }
}