package model;

import java.sql.Timestamp; // Import Timestamp nếu bạn sử dụng createdAt

public class Slider {
    private int sliderId;
    private String image;
    private String title;
    private String backlink;
    private String status;
    private String note; // <-- Đảm bảo trường này đã tồn tại
    private String createdBy; // <-- Thêm nếu bạn có cột createdBy
    private Timestamp createdAt; // <-- Thêm nếu bạn có cột createdAt

    // Constructor có đủ 6 tham số (hoặc nhiều hơn nếu bạn có thêm createdBy, createdAt)
    // Constructor này khớp với việc bạn lấy 6 giá trị từ ResultSet trong getAllSliders()
    public Slider(int sliderId, String title, String image, String backlink, String status, String note) {
        this.sliderId = sliderId;
        this.title = title;
        this.image = image;
        this.backlink = backlink;
        this.status = status;
        this.note = note;
    }

    // Constructor đầy đủ hơn nếu bạn có createdBy và createdAt trong DB và cần dùng
    public Slider(int sliderId, String title, String image, String backlink, String status, String note, String createdBy, Timestamp createdAt) {
        this.sliderId = sliderId;
        this.title = title;
        this.image = image;
        this.backlink = backlink;
        this.status = status;
        this.note = note;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Constructor mặc định (không tham số) là cần thiết cho các phương thức dùng set methods (ví dụ: filterSlidersByStatus)
    public Slider() {
    }

    // Getters and Setters cho tất cả các thuộc tính
    public int getSliderId() { return sliderId; }
    public void setSliderId(int sliderId) { this.sliderId = sliderId; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBacklink() { return backlink; }
    public void setBacklink(String backlink) { this.backlink = backlink; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNote() { return note; } // <-- Đảm bảo getter này tồn tại
    public void setNote(String note) { this.note = note; } // <-- Và setter này
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Slider{" + "sliderId=" + sliderId + ", image=" + image + ", title=" + title + ", backlink=" + backlink + ", status=" + status + ", note=" + note + ", createdBy=" + createdBy + ", createdAt=" + createdAt + '}';
    }
}