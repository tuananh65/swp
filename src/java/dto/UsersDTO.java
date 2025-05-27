package dto;

import java.sql.Date;
import java.sql.Timestamp;

public class UsersDTO {
    private int userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private int roleId;
    private String avatar;
    private String status;
    private String gender;
    private Date dateOfBirth;
    private Timestamp createdAt;

    // Constructor cơ bản
    public UsersDTO() {
    }

    // Constructor với các field chính
    public UsersDTO(int userId, String fullName, String email, String phone, 
                   String address, int roleId, String avatar, String status) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
        this.avatar = avatar;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public String getRoleName() {
        switch (roleId) {
            case 1: return "Admin";
            case 2: return "Instructor";
            case 3: return "Student";
            default: return "Unknown";
        }
    }

    public boolean isAdmin() {
        return roleId == 1;
    }

    public boolean isInstructor() {
        return roleId == 2;
    }

    public boolean isStudent() {
        return roleId == 3;
    }

    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "UsersDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", roleId=" + roleId +
                ", status='" + status + '\'' +
                '}';
    }
}