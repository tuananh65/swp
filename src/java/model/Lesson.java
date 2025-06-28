package model;

import java.sql.Timestamp; // Not strictly needed for the current fields, but good practice for date/time if added later

public class Lesson {
    private int lessonId;
    private int courseId;
    private String title;
    private String content;
    private String videoUrl;
    private Integer sortOrder; // Use Integer for nullable int
    private String status;
    private Integer order;     // Use Integer for nullable int
    private String type;       // String is nullable by default in Java



    public Lesson() {

    }

    public Lesson(int lessonId, int courseId, String title, String content, String videoUrl, Integer sortOrder, String status, Integer order, String type) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.sortOrder = sortOrder;
        this.status = status;
        this.order = order;
        this.type = type;
    }

    public Lesson(int courseId, String title, String content, String videoUrl, Integer sortOrder, String status, Integer order, String type) {
        this.courseId = courseId;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.sortOrder = sortOrder;
        this.status = status;
        this.order = order;
        this.type = type;
    }


    public Lesson(int courseId, String title) {
        this.courseId = courseId;
        this.title = title;
        this.status = "Active"; // Assuming active by default
        // Other nullable fields remain null by default
    }


    // ===== Getters and Setters =====

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
