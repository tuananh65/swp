package model;

// Nên dùng java.util.Date cho các trường ngày tháng trong model
// java.sql.Date chỉ nên dùng khi tương tác trực tiếp với JDBC PreparedStatement.setDate() và ResultSet.getDate()
import java.util.Date; // <-- Đổi từ java.sql.Date sang java.util.Date để linh hoạt hơn

public class Course {
    private int courseID;
    private String courseName;
    private String tagLine;
    private String briefInfo;
    private String description;
    private double originalPrice;
    private double salePrice;
    private String courseThumbnail;
    private Date createdAt;
    private Date updatedAt;
    private int userID;
    private boolean featured;
    private int subjectID;
    private Integer lessonID; // Có thể null

    public Course() {
    }

    public Course(int courseID, String courseName, String tagLine, String briefInfo, String description, double originalPrice, double salePrice, String courseThumbnail, Date createdAt, Date updatedAt, int userID, boolean featured, int subjectID, Integer lessonID) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.tagLine = tagLine;
        this.briefInfo = briefInfo;
        this.description = description;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
        this.courseThumbnail = courseThumbnail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userID = userID;
        this.featured = featured;
        this.subjectID = subjectID;
        this.lessonID = lessonID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getBriefInfo() {
        return briefInfo;
    }

    public void setBriefInfo(String briefInfo) {
        this.briefInfo = briefInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getCourseThumbnail() {
        return courseThumbnail;
    }

    public void setCourseThumbnail(String courseThumbnail) {
        this.courseThumbnail = courseThumbnail;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public Integer getLessonID() {
        return lessonID;
    }

    public void setLessonID(Integer lessonID) {
        this.lessonID = lessonID;
    }

    @Override
    public String toString() {
        return "Course{" + "courseID=" + courseID + ", courseName=" + courseName + ", tagLine=" + tagLine + ", briefInfo=" + briefInfo + ", description=" + description + ", originalPrice=" + originalPrice + ", salePrice=" + salePrice + ", courseThumbnail=" + courseThumbnail + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", userID=" + userID + ", featured=" + featured + ", subjectID=" + subjectID + ", lessonID=" + lessonID + '}';
    }

}  