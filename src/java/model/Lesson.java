package model;

import java.sql.Timestamp;

public class Lesson {
    private int lessonId;
    private int subjectId; // ĐÃ ĐỔI TỪ courseId THÀNH subjectId
    private String title;
    private String content;
    private String videoUrl;
    // private Integer sortOrder; // ĐÃ XÓA/COMMENT DÒNG NÀY ĐỂ CHỈ DÙNG 'order'
    private String status;
    private Integer order;    // GIỮ LẠI DÒNG NÀY
    private String type;

    public Lesson() {
    }

    // Constructor đầy đủ: Đã bỏ sortOrder, đổi courseId thành subjectId
    public Lesson(int lessonId, int subjectId, String title, String content, String videoUrl, String status, Integer order, String type) {
        this.lessonId = lessonId;
        this.subjectId = subjectId;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.status = status;
        this.order = order;
        this.type = type;
    }

    // Constructor khi thêm mới (không có lessonId): Đã bỏ sortOrder, đổi courseId thành subjectId
    public Lesson(int subjectId, String title, String content, String videoUrl, String status, Integer order, String type) {
        this.subjectId = subjectId;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.status = status;
        this.order = order;
        this.type = type;
    }

    // Constructor tối thiểu: Đã đổi courseId thành subjectId
    public Lesson(int subjectId, String title) {
        this.subjectId = subjectId;
        this.title = title;
        this.status = "Active";
    }

    // ===== Getters and Setters =====

    public int getLessonId() { return lessonId; }
    public void setLessonId(int lessonId) { this.lessonId = lessonId; }

    public int getSubjectId() { return subjectId; } // ĐÃ ĐỔI TỪ getCourseId THÀNH getSubjectId
    public void setSubjectId(int subjectId) { this.subjectId = subjectId; } // ĐÃ ĐỔI TỪ setCourseId THÀNH setSubjectId

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    // public Integer getSortOrder() { return sortOrder; } // ĐÃ XÓA/COMMENT
    // public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; } // ĐÃ XÓA/COMMENT

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getOrder() { return order; }
    public void setOrder(Integer order) { this.order = order; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}