package model;

import java.util.List;
import java.sql.Date; // Hoặc java.time.LocalDate nếu bạn dùng Java 8+

public class Subject {
    private int subjectId;
    private String name;
    private boolean featured;
    private String thumbnail;
    private String description;
    private String status;
    private int ownerId;
    private String categoryName;
    private int lessonCount; // Added to store lesson count temporarily
    private Date createdAt; // Thêm thuộc tính này

    // Quan hệ 1-n: Subject -> Course
    private List<Course> courseList;

    // Quan hệ 1-n: Subject -> Dimension
    private List<Dimension> dimensionList;

    // Constructors
    public Subject() {}

    // Constructor cũ (có thể giữ lại hoặc không, tùy nhu cầu sử dụng)
    public Subject(int subjectId, String name, boolean featured,
                   String thumbnail, String description,
                   int ownerId, String status, String categoryName) {
        this.subjectId = subjectId;
        this.name = name;
        this.featured = featured;
        this.thumbnail = thumbnail;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
        this.categoryName = categoryName;
        // createdAt sẽ là null nếu không được gán, hoặc bạn có thể gán giá trị mặc định ở đây nếu muốn
    }

    // Constructor MỚI với createdAt
    public Subject(int subjectId, String name, boolean featured,
                   String thumbnail, String description,
                   int ownerId, String status, String categoryName,
                   Date createdAt) { // Thêm tham số createdAt
        this.subjectId = subjectId;
        this.name = name;
        this.featured = featured;
        this.thumbnail = thumbnail;
        this.description = description;
        this.ownerId = ownerId;
        this.status = status;
        this.categoryName = categoryName;
        this.createdAt = createdAt; // Gán giá trị cho createdAt
    }


    // Getters and Setters
    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }

    public List<Dimension> getDimensionList() {
        return dimensionList;
    }

    public void setDimensionList(List<Dimension> dimensionList) {
        this.dimensionList = dimensionList;
    }

    // Added getter and setter for lessonCount
    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    // Getter and Setter for createdAt
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}