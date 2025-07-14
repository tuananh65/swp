package model;

import java.sql.Timestamp;

/**
 * Lớp Post đại diện cho một bài viết chi tiết trên hệ thống.
 * Bao gồm các thông tin như tiêu đề, hình đại diện, chuyên mục, mô tả ngắn,
 * nội dung đầy đủ, tác giả, ngày cập nhật, trạng thái và có nổi bật hay không.
 */
public class Post {
    // ======= Thuộc tính =======

    /** ID định danh duy nhất cho mỗi bài viết */
    private int id;

    /** Tiêu đề của bài viết */
    private String title;

    /** Đường dẫn hình ảnh thumbnail của bài viết */
    private String thumbnail;

    /** Chuyên mục (category) của bài viết */
    private String category;

    /** Thông tin mô tả ngắn gọn về bài viết */
    private String briefInfo;

    /** Nội dung đầy đủ của bài viết */
    private String content;

    /** Tên tác giả của bài viết */
    private String author;

    /** Ngày và giờ cập nhật cuối cùng của bài viết */
    private Timestamp updatedDate;

    /** Cờ đánh dấu bài viết có được hiển thị nổi bật không */
    private boolean featured;

    /** Trạng thái của bài viết (ví dụ: published, draft, deleted) */
    private String status;

    // ======= Constructor =======

    /** Constructor mặc định */
    public Post() {
    }

    /**
     * Constructor đầy đủ để khởi tạo bài viết với tất cả thuộc tính.
     *
     * @param id          ID của bài viết
     * @param title       Tiêu đề
     * @param thumbnail   Hình ảnh đại diện
     * @param category    Chuyên mục
     * @param briefInfo   Mô tả ngắn
     * @param content     Nội dung đầy đủ
     * @param author      Tác giả
     * @param updatedDate Ngày cập nhật
     * @param featured    Có nổi bật hay không
     * @param status      Trạng thái
     */
    public Post(int id, String title, String thumbnail, String category, String briefInfo,
                String content, String author, Timestamp updatedDate, boolean featured, String status) {
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

    // ======= Getter & Setter =======

    /** @return ID bài viết */
    public int getId() {
        return id;
    }

    /** @param id ID bài viết */
    public void setId(int id) {
        this.id = id;
    }

    /** @return Tiêu đề bài viết */
    public String getTitle() {
        return title;
    }

    /** @param title Tiêu đề bài viết */
    public void setTitle(String title) {
        this.title = title;
    }

    /** @return Hình ảnh đại diện */
    public String getThumbnail() {
        return thumbnail;
    }

    /** @param thumbnail Hình ảnh đại diện */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /** @return Chuyên mục */
    public String getCategory() {
        return category;
    }

    /** @param category Chuyên mục */
    public void setCategory(String category) {
        this.category = category;
    }

    /** @return Mô tả ngắn */
    public String getBriefInfo() {
        return briefInfo;
    }

    /** @param briefInfo Mô tả ngắn */
    public void setBriefInfo(String briefInfo) {
        this.briefInfo = briefInfo;
    }

    /** @return Nội dung bài viết */
    public String getContent() {
        return content;
    }

    /** @param content Nội dung bài viết */
    public void setContent(String content) {
        this.content = content;
    }

    /** @return Tác giả bài viết */
    public String getAuthor() {
        return author;
    }

    /** @param author Tác giả bài viết */
    public void setAuthor(String author) {
        this.author = author;
    }

    /** @return Ngày cập nhật bài viết */
    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    /** @param updatedDate Ngày cập nhật bài viết */
    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    /** @return true nếu bài viết được đánh dấu nổi bật */
    public boolean isFeatured() {
        return featured;
    }

    /** @param featured Đánh dấu nổi bật bài viết */
    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    /** @return Trạng thái bài viết */
    public String getStatus() {
        return status;
    }

    /** @param status Trạng thái bài viết */
    public void setStatus(String status) {
        this.status = status;
    }

    // ======= Override toString =======

    /**
     * Trả về chuỗi đại diện cho bài viết (dùng để log/debug)
     */
    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", category='" + category + '\'' +
                ", briefInfo='" + briefInfo + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", updatedDate=" + updatedDate +
                ", featured=" + featured +
                ", status='" + status + '\'' +
                '}';
    }
}
