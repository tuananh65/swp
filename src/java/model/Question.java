/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Admin
 */
public class Question {
     private int questionID;
    private int testID;
    private String content;
    private int points;

    // Getter và Setter
    // ...

    public Question(int questionID, int testID, String content, int points) {
        this.questionID = questionID;
        this.testID = testID;
        this.content = content;
        this.points = points;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question{");
        sb.append("questionID=").append(questionID);
        sb.append(", testID=").append(testID);
        sb.append(", content=").append(content);
        sb.append(", points=").append(points);
        sb.append('}');
        return sb.toString();
    }
    
}
