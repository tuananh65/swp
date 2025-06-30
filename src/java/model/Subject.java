package model;

import java.util.List;

public class Subject {
    private int subjectId;
    private String name;
    private boolean featured;
    private String thumbnail;
    private String description;
    private int numberOfLesson;
    private String status;
    private int ownerId;
    private String categoryName;

    // Quan hệ 1-n: Subject -> Course
    private List<Course> courseList;

    // Quan hệ 1-n: Subject -> Dimension
    private List<Dimension> dimensionList;

    // Constructors
    public Subject() {}

    public Subject(int subjectId, String name, boolean featured,
                   String thumbnail, String description, int numberOfLesson,
                   int ownerId, String status, String categoryName) {
        this.subjectId = subjectId;
        this.name = name;
        this.featured = featured;
        this.thumbnail = thumbnail;
        this.description = description;
        this.numberOfLesson = numberOfLesson;
        this.ownerId = ownerId;
        this.status = status;
        this.categoryName = categoryName;
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

    public int getNumberOfLesson() {
        return numberOfLesson;
    }

    public void setNumberOfLesson(int numberOfLesson) {
        this.numberOfLesson = numberOfLesson;
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
}
