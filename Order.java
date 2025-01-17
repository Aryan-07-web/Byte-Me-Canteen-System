package com.example.ap_assignment4.Assignment3_classes;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class Order implements Serializable {
    private int OrderId;
    private double OrderPrice;
    private String Status;   // order status can only be Pending,Cancelled,Completed,Refunded
    private Map<Item,Integer> Items;  // for assignment 3
    private String splReq;
    private String customerName;
    private static final long serialVersionUID = 1L;
    public Order(int orderId, String status,String CustomerName, Map<Item, Integer> items, double total) {
        this.OrderId = orderId;
        this.OrderPrice = total;
        this.Status = status;
        this.Items = items;
        this.splReq = "";
        this.customerName = CustomerName;
    }
//    @Override
////    public String toString() {
////        return OrderId + "," + customerName + "," + Status + "," + OrderPrice;
////    }

    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        if (Items != null && !Items.isEmpty()) {
            for (Map.Entry<Item, Integer> entry : Items.entrySet()) {
                itemsString.append(entry.getKey().getName()).append(":").append(entry.getValue()).append(";");
            }
            // Remove the trailing semicolon
            if (itemsString.length() > 0) {
                itemsString.setLength(itemsString.length() - 1);
            }
        }
        return OrderId + "," + customerName + "," + Status + "," + OrderPrice + "," + itemsString;
    }

    public String getCustomerName() {
        return customerName;
    }

    public ArrayList<String> getOrderItemsName() {
        ArrayList<String> itemNames = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : Items.entrySet()) {
            itemNames.add(entry.getKey().getName());
        }
        return itemNames;
    }

    public ArrayList<Integer> getOrderItemsQuantity() {
        ArrayList<Integer> itemQuantities = new ArrayList<>();
        for (Map.Entry<Item, Integer> entry : Items.entrySet()) {
            itemQuantities.add(entry.getValue());
        }
        return itemQuantities;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Map<Item, Integer> getItems() {
        return Items;
    }
    public double getOrderPrice(){
        if(Items == null){
            return OrderPrice;
        }
        for (Map.Entry<Item, Integer> entry : Items.entrySet()) {
            Item item = entry.getKey();
            int quantity = entry.getValue();
            OrderPrice += item.getPrice() * quantity;
        }
        return OrderPrice;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void AddSplReq(String req){
        splReq = req;
    }
    public String getSplReq() {
        return splReq;
    }
}
