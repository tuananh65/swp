/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author maitu
 */
public class Order {
    private int orderID;
    private int userID;
    private int courseID;
    private int packageID;
    private double totalPrice;
    private String orderStatus;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp paidAt;
    private String paymentMethod;
    private String note;

    public Order() {
    }

    public Order(int orderID, int userID, int courseID, int packageID, double totalPrice, String orderStatus, Timestamp createdAt, Timestamp paidAt, String paymentMethod, String note) {
        this.orderID = orderID;
        this.userID = userID;
        this.courseID = courseID;
        this.packageID = packageID;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.paymentMethod = paymentMethod;
        this.note = note;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getPackageID() {
        return packageID;
    }

    public void setPackageID(int packageID) {
        this.packageID = packageID;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Order{" + "orderID=" + orderID + ", userID=" + userID + ", courseID=" + courseID + ", packageID=" + packageID + ", totalPrice=" + totalPrice + ", orderStatus=" + orderStatus + ", createdAt=" + createdAt + ", paidAt=" + paidAt + ", paymentMethod=" + paymentMethod + ", note=" + note + '}';
    }
    
    
    
}
