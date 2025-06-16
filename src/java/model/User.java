package model;

import java.sql.Timestamp;
import java.util.Date;

public class User {
    private int userId;
    private String userName;
    private String password;
    private int roleId;
    private String fullName;
    private String gender;
    private Date dateOfBirth;
    private String avatarUrl;
    private String email;
    private String phone;
    private String address;
    private String status;
    private Date createdAt;
    private String activationToken;
    private boolean isActivated; 
    private String resetToken;
    private Timestamp tokenExpiry;

    public User() {
    }

    public User(int userId, String userName, String password, int roleId, String fullName, String gender, 
                Date dateOfBirth, String avatarUrl, String email, String phone, String address, 
                String status, Date createdAt, String activationToken, boolean isActivated, String resetToken, Timestamp tokenExpiry) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.activationToken = activationToken;
        this.isActivated = isActivated;
        this.resetToken = resetToken;
        this.tokenExpiry = tokenExpiry;
    }

    // Các getters/setters khác giữ nguyên
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }
    
    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Timestamp getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(Timestamp tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", userName=" + userName + ", password=" + password 
               + ", roleId=" + roleId + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth 
               + ", avatarUrl=" + avatarUrl + ", email=" + email + ", phone=" + phone 
               + ", address=" + address + ", status=" + status + ", createdAt=" + createdAt 
               + ", gender=" + gender + ", activationToken=" + activationToken 
               + ", isActivated=" + isActivated + ", resetToken=" + resetToken + ", tokenExpiry=" + tokenExpiry + '}';
    }
}