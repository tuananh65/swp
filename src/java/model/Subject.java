/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

public class Subject {
    private int subjectId;
    private String name;
    private Integer categoryId;
    private Boolean featured;
    private Integer statusId;
    private String thumbnail;
    private String description;

    // Constructor đầy đủ
    public Subject(int subjectId, String name, Integer categoryId, Boolean featured, Integer statusId, String thumbnail, String description) {
        this.subjectId = subjectId;
        this.name = name;
        this.categoryId = categoryId;
        this.featured = featured;
        this.statusId = statusId;
        this.thumbnail = thumbnail;
        this.description = description;
    }

    // Constructor không có subjectId (dùng khi insert mới)
    public Subject(String name, Integer categoryId, Boolean featured, Integer statusId, String thumbnail, String description) {
        this.name = name;
        this.categoryId = categoryId;
        this.featured = featured;
        this.statusId = statusId;
        this.thumbnail = thumbnail;
        this.description = description;
    }
    
    
    // Getters và setters ...

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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
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
}
