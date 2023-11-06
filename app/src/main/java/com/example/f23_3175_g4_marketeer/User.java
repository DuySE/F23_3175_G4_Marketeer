package com.example.f23_3175_g4_marketeer;

public class User {
    // Fields
    private String username, password, address, phone, profile_img;
    static String receiver;

    // Constructor
    public User(String username, String password, String address, String phone, String profile_img) {
        this.username = username;
        this.password = password;
        this.address = address;
        this.phone = phone;
        this.profile_img = profile_img;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImg() {
        return profile_img;
    }

    public void setProfileImg(String profile_img) {
        this.profile_img = profile_img;
    }
}