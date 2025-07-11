package model;

import java.sql.Timestamp; // Import Timestamp

public class Post {
    private int id;
    private String title;
    private String thumbnail;
    private String category;
    private String briefInfo;
    private String content;
    private String author;
    private Timestamp updatedDate; // Thay đổi từ String sang Timestamp
    private boolean featured;      // Thêm thuộc tính featured
    private String status;         // Thêm thuộc tính status

    // Constructor mặc định (no-arg constructor) - RẤT QUAN TRỌNG nếu bạn tạo Post không tham số
    public Post() {
    }

    // Constructor đầy đủ tham số
    public Post(int id, String title, String thumbnail, String category, String briefInfo, String content, String author, Timestamp updatedDate, boolean featured, String status) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.briefInfo = briefInfo;
        this.content = content;
        this.author = author;
        this.updatedDate = updatedDate;
        this.featured = featured;
        this.status = status;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBriefInfo() {
        return briefInfo;
    }

    public void setBriefInfo(String briefInfo) {
        this.briefInfo = briefInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Timestamp getUpdatedDate() { // Thay đổi kiểu trả về
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) { // Thay đổi kiểu tham số
        this.updatedDate = updatedDate;
    }

    public boolean isFeatured() { // Getter cho boolean thường là is<PropertyName>
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}