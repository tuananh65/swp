package model;

public class Post {
    private int id;
    private String title;
    private String thumbnail;
    private String category;
    private String briefInfo;
    private String content;
    private String author;
    private String updatedDate;

    // Constructor
    public Post(int id, String title, String thumbnail, String category, String briefInfo, String content, String author, String updatedDate) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.category = category;
        this.briefInfo = briefInfo;
        this.content = content;
        this.author = author;
        this.updatedDate = updatedDate;
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

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
