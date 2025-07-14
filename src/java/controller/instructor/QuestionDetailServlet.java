package controller.instructor;

import dal.AnswerDAO;
import dal.CourseDAO;
import dal.QuestionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Answer;
import model.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.Course;

@WebServlet("/instructor/question-detail")
public class QuestionDetailServlet extends HttpServlet {

    private QuestionDAO questionDAO;
    private AnswerDAO answerDAO;

    @Override
    public void init() throws ServletException {
        questionDAO = new QuestionDAO();
        answerDAO = new AnswerDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CourseDAO courseDAO = new CourseDAO();
        List<Course> courseList = courseDAO.getAllCourses();
        request.setAttribute("courseList", courseList);

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            int questionID = Integer.parseInt(idParam);
            Question question = questionDAO.getQuestionByID(questionID);
            if (question != null) {
                List<Answer> answers = answerDAO.getAnswersByQuestionID(questionID);
                question.setAnswers(answers);
                request.setAttribute("question", question);
            } else {
                request.setAttribute("errorMessage", "Question not found.");
            }
        }

        request.getRequestDispatcher("/instructor/question-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            // Read question fields
            String questionIDParam = request.getParameter("questionID");
            int courseID = Integer.parseInt(request.getParameter("courseID"));
            String content = request.getParameter("content");
            int points = Integer.parseInt(request.getParameter("points"));
            String questionType = request.getParameter("questionType");

            // Basic validation
            if (content == null || content.trim().isEmpty() || questionType == null) {
                request.setAttribute("errorMessage", "Content and type are required!");
                request.getRequestDispatcher("/instructor/question-detail.jsp").forward(request, response);
                return;
            }

            Question question = new Question();
            question.setCourseID(courseID);
            question.setContent(content);
            question.setPoints(points);
            question.setQuestionType(questionType);

            boolean isUpdate = (questionIDParam != null && !questionIDParam.isEmpty());
            int questionID;

            if (isUpdate) {
                questionID = Integer.parseInt(questionIDParam);
                question.setQuestionID(questionID);
                questionDAO.updateQuestion(question);
                answerDAO.deleteAnswersByQuestionID(questionID);
            } else {
                questionID = questionDAO.addQuestionAndGetID(question);
            }

            // Process answers
            if ("Multiple Choice".equalsIgnoreCase(questionType)) {
                String[] answerContents = request.getParameterValues("answerContent");
                String[] correctFlags = request.getParameterValues("isCorrect");

                List<Answer> answers = new ArrayList<>();
                if (answerContents != null) {
                    for (int i = 0; i < answerContents.length; i++) {
                        String ansContent = answerContents[i];
                        boolean isCorrect = false;
                        if (correctFlags != null) {
                            for (String c : correctFlags) {
                                if (c.equals(String.valueOf(i))) {
                                    isCorrect = true;
                                    break;
                                }
                            }
                        }
                        Answer answer = new Answer();
                        answer.setQuestionID(questionID);
                        answer.setContent(ansContent);
                        answer.setCorrect(isCorrect);
                        answers.add(answer);
                    }
                }

                answerDAO.addAnswersBatch(answers);

            } else if ("Short Answer".equalsIgnoreCase(questionType)) {
                String shortAnswer = request.getParameter("shortAnswerContent");
                Answer answer = new Answer();
                answer.setQuestionID(questionID);
                answer.setContent(shortAnswer);
                answer.setCorrect(true);
                answerDAO.addAnswer(answer);
            }

            response.sendRedirect("question-list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error processing request: " + e.getMessage());
            request.getRequestDispatcher("/instructor/question-detail.jsp").forward(request, response);
        }
    }
}
