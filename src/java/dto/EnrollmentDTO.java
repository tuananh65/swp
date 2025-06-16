package dto;

import java.math.BigDecimal;
import java.util.Date;

public class EnrollmentDTO {
    private int enrollmentId;
    private String userFullName;
    private String userEmail;
    private String courseName;
    private String packageName;
    private BigDecimal totalPrice;
    private String status;
    private Date enrollmentDate;
    private Date validFrom;
    private Date validTo;
    private String updatedByName;
    private Integer orderId;
    private String courseThumbnail;

    public EnrollmentDTO() {
    }

    public EnrollmentDTO(int enrollmentId, String userFullName, String userEmail, String courseName,String courseThumbnail, String packageName,
                         BigDecimal totalPrice, String status, Date enrollmentDate, Date validFrom, Date validTo,
                         String updatedByName, Integer orderId) {
        this.enrollmentId = enrollmentId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.courseName = courseName;
        this.courseThumbnail=courseThumbnail;
        this.packageName = packageName;
        this.totalPrice = totalPrice;
        this.status = status;
        this.enrollmentDate = enrollmentDate;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.updatedByName = updatedByName;
        this.orderId = orderId;
          }

    public String getCourseThumbnail() {
        return courseThumbnail;
    }

    // Getters và Setters
    public void setCourseThumbnail(String courseThumbnail) {
        this.courseThumbnail = courseThumbnail;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "EnrollmentDTO{" +
                "enrollmentId=" + enrollmentId +
                ", userFullName='" + userFullName + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", courseName='" + courseName + '\'' +
                ", packageName='" + packageName + '\'' +
                ", totalPrice=" + totalPrice +
                ", status='" + status + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                ", updatedByName='" + updatedByName + '\'' +
                ", orderId=" + orderId +
                '}';
    }
}
