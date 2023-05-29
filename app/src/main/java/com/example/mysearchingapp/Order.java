package com.example.mysearchingapp;

import java.io.Serializable;

public class Order implements Serializable {

    //Instance variables
    private byte[] flowerImageBytes;
    private String username, receiverName, date, time, destination, flowerType, quantity, message;
    private double destinationLatitude, destinationLongitude;

    //Constructor for initialization
    public Order(String username, byte[] flowerImageByte,String receiverName, String date, String time, String destination, double destinationLatitude, double destinationLongitude, String flowerType, String quantity, String message) {
        this.username = username;
        this.flowerImageBytes = flowerImageByte;
        this.receiverName = receiverName;
        this.date = date;
        this.time = time;
        this.destination = destination;
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
        this.flowerType = flowerType;
        this.quantity = quantity;
        this.message = message;
    }

    //Empty constructor
    public Order() {

    }

    //Getters and setters

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getReceiverName() {
        return receiverName;
    }
    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public double getDestinationLatitude() {
        return destinationLatitude;
    }
    public void setDestinationLatitude(double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }
    public double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }
    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public byte[] getFlowerImageBytes() {
        return flowerImageBytes;
    }
    public void setFlowerImageBytes(byte[] flowerImageBytes) {
        this.flowerImageBytes = flowerImageBytes;
    }
    public String getFlowerType() {
        return flowerType;
    }
    public void setFlowerType(String flowerType) {
        this.flowerType = flowerType;
    }
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

}
