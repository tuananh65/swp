/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Quiz {
    private int testID;
    private int courseID;
    private String title;
    private String description;
    private String difficulty;
    private int duration;
    private boolean active;
    private String courseName;

public String getCourseName() {
    return courseName;
}
public void setCourseName(String courseName) {
    this.courseName = courseName;
}


    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Quiz(int testID, int courseID, String title, String description, String difficulty, int duration, boolean active) {
        this.testID = testID;
        this.courseID = courseID;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.duration = duration;
        this.active = active;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Quiz{");
        sb.append("testID=").append(testID);
        sb.append(", courseID=").append(courseID);
        sb.append(", title=").append(title);
        sb.append(", description=").append(description);
        sb.append(", difficulty=").append(difficulty);
        sb.append(", duration=").append(duration);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }
    
    

}
