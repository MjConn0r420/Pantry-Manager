package com.example.smartpantry.model;

/** One ingredient currently in the user's pantry. */
public class PantryItem {
    private long id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate; // "yyyy-MM-dd" or null

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }
}
