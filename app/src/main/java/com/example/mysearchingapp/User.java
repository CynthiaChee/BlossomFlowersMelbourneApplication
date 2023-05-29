package com.example.mysearchingapp;

public class User {

    //Instance variables
    private byte[] image;
    private String name, username, password, phoneNo;

    //Constructor for initialization
    public User(byte[] image, String name, String username, String password, String phoneNo) {

        this.image = image;
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNo = phoneNo;
    }

    //Empty constructor
    public User() {

    }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
