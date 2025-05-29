package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Users {
    private int userID;
    private String userName;
    private String password;
    private int roleId;
    private String fullName;
    private Date dateOfBirth;
    private String avatarUrl;
    private String email;
    private String phone;
    private String address;
    private String status;
    private Timestamp createdAt;
    private String gender;


    public Users() {
    }

    public Users(int userID, String userName, String password, int roleId, String fullName, Date dateOfBirth, String avatarUrl, String email, String phone, String address, String status, Timestamp createdAt, String gender) {
        this.userID = userID;
        this.userName = userName;
        this.password = password;
        this.roleId = roleId;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.createdAt = createdAt;
        this.gender = gender;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Users{" + "userID=" + userID + ", userName=" + userName + ", password=" + password + ", roleId=" + roleId + ", fullName=" + fullName + ", dateOfBirth=" + dateOfBirth + ", avatarUrl=" + avatarUrl + ", email=" + email + ", phone=" + phone + ", address=" + address + ", status=" + status + ", createdAt=" + createdAt + ", gender=" + gender + '}';
    }

    public void setRoleName(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}

   