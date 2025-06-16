package model;

import java.math.BigDecimal;
import java.util.Date;

public class Enrollment {

    private int enrollmentId;
    private int userId;
    private int courseId;
    private Date enrollmentDate;
    private int packageId;
    private BigDecimal totalPrice;
    private String status;
    private Date validFrom;
    private Date validTo;
    private Integer updatedByUserId; // nullable
    private Integer orderId;         // nullable
    private String note;

    // Constructor mặc định
    public Enrollment() {
    }

    // Constructor đầy đủ
    public Enrollment(int enrollmentId, int userId, int courseId, Date enrollmentDate, int packageId,
            BigDecimal totalPrice, String status, Date validFrom, Date validTo,
            Integer updatedByUserId, Integer orderId, String note) {
        this.enrollmentId = enrollmentId;
        this.userId = userId;
        this.courseId = courseId;
        this.enrollmentDate = enrollmentDate;
        this.packageId = packageId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.updatedByUserId = updatedByUserId;
        this.orderId = orderId;
        this.note = note;
    }

    // Getters và Setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Date getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(Date enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
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

    public Integer getUpdatedByUserId() {
        return updatedByUserId;
    }

    public void setUpdatedByUserId(Integer updatedByUserId) {
        this.updatedByUserId = updatedByUserId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // toString để debug nhanh
    @Override
    public String toString() {
        return "Enrollment{"
                + "enrollmentId=" + enrollmentId
                + ", userId=" + userId
                + ", courseId=" + courseId
                + ", enrollmentDate=" + enrollmentDate
                + ", packageId=" + packageId
                + ", totalPrice=" + totalPrice
                + ", status='" + status + '\''
                + ", validFrom=" + validFrom
                + ", validTo=" + validTo
                + ", updatedByUserId=" + updatedByUserId
                + ", orderId=" + orderId
                + ", note=" + note+ '\''
                + '}';
    }
}
