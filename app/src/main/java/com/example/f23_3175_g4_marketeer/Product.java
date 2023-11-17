package com.example.f23_3175_g4_marketeer;

public class Product {
    private String name;
    private String price;
    private String imgName;
    private String seller;
    private String status;
    private int id;
    private float distanceToUser;

    public Product(String name, String price, String imgPath, String seller, String status, int id) {
        this.name = name;
        this.price = price;
        this.imgName = imgPath;
        this.seller = seller;
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgPath) {
        this.imgName = imgPath;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDistanceToUser() {
        return distanceToUser;
    }

    public void setDistanceToUser(float distanceToUser) {
        this.distanceToUser = distanceToUser;
    }
}
