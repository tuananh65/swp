package model;

/**
 * Model đại diện cho câu hỏi trong hệ thống Quiz.
 */
public class Question {
    private int questionID;  // Khóa chính của bảng Question
    private String content;  // Nội dung câu hỏi
    private int points;      // Số điểm cho câu hỏi này
    private int courseID;

public int getCourseID() { return courseID; }
public void setCourseID(int courseID) { this.courseID = courseID; }


    // ==== Constructors ====
    public Question() {
    }

    public Question(int questionID, int courseID, String content, int points) {
    this.questionID = questionID;
    this.courseID = courseID;
    this.content = content;
    this.points = points;
}


    // ==== Getter và Setter ====
    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
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

    // ==== toString để debug dễ dàng ====
    @Override
    public String toString() {
        return "Question{" +
                "questionID=" + questionID +
                ", content='" + content + '\'' +
                ", points=" + points +
                '}';
    }

    
}
