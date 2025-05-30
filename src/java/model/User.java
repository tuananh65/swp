/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class User {
    private int userID;
    private String userName;
    private String userPassword;
    private int roleID;
    private String userFullName;
    private String userGender;
    private Date dateOfBirth;
    private String userAvatarUrl;
    private String userEmail;
    private String userPhone;
    private String userAddress;
    private String userStatus;
    private Date createdAt;
    
    public User (){
        
    }

    public User(int userID, String userName, String userPassword, int roleID, String userFullName, String userGender, Date dateOfBirth, String userAvatarUrl, String userEmail, String userPhone, String userAddress, String userStatus, Date createdAt) {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
        this.roleID = roleID;
        this.userFullName = userFullName;
        this.userGender = userGender;
        this.dateOfBirth = dateOfBirth;
        this.userAvatarUrl = userAvatarUrl;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userAddress = userAddress;
        this.userStatus = userStatus;
        this.createdAt = createdAt;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roldID) {
        this.roleID = roldID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }   
}
