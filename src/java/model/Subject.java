package model;
import java.util.List;
import model.Course;
import model.Dimension;

public class Subject {
    private int subjectId;
    private String name;
    private int categoryId;
    private boolean featured;
    private String thumbnail;
    private String description;
    private int numberOfLesson;
    private String owner;
    private String status;

    // Quan hệ 1-n: Subject -> Course
    private List<Course> courseList;

    // Quan hệ 1-n: Subject -> Dimension
    private List<Dimension> dimensionList;

    // Quan hệ 1-n: Subject -> Package
    private List<Package> packageList;

    // Constructors
    public Subject() {}

    public Subject(int subjectId, String name, int categoryId, boolean featured,
                   String thumbnail, String description, int numberOfLesson,
                   String owner, String status) {
        this.subjectId = subjectId;
        this.name = name;
        this.categoryId = categoryId;
        this.featured = featured;
        this.thumbnail = thumbnail;
        this.description = description;
        this.numberOfLesson = numberOfLesson;
        this.owner = owner;
        this.status = status;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<Package> packageList) {
        this.packageList = packageList;
    }
}
