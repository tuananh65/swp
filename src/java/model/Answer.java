package model;

public class Answer {
    private int answerID;
    private int questionID;
    private String content;
    private boolean isCorrect;

    public Answer(int answerID, int questionID, String content, boolean isCorrect) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.content = content;
        this.isCorrect = isCorrect;
    }
    public Answer() {
    }
    

    // Getters and Setters
    public int getAnswerID() {
        return answerID;
    }

    public void setAnswerID(int answerID) {
        this.answerID = answerID;
    }

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

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
