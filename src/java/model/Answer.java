package model;

/**
 * Model đại diện cho đáp án của một câu hỏi.
 */
public class Answer {
    private int answerID;
    private int questionID;
    private String content;
    private boolean isCorrect;

    // === Constructors ===
    public Answer() {
    }

    public Answer(int answerID, int questionID, String content, boolean isCorrect) {
        this.answerID = answerID;
        this.questionID = questionID;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    // === Getter/Setter ===
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

    // === toString ===
    @Override
    public String toString() {
        return "Answer{" +
                "answerID=" + answerID +
                ", questionID=" + questionID +
                ", content='" + content + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }

    // === MAIN TEST ===
    public static void main(String[] args) {
        Answer a = new Answer(1, 1, "A programming language", true);
        System.out.println(a);

        a.setContent("Structured Query Language");
        a.setCorrect(true);
        System.out.println(a);
    }
}
