package com.example.f23_3175_g4_marketeer;

public class Transaction {
    private String date, productName, imageName, username;
    private int amount;

    public Transaction(String date, String productName, String imageName, String username) {
        this.date = date;
        this.productName = productName;
        this.imageName = imageName;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public String getProductName() {
        return productName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageName() {
        return imageName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}