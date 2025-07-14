package controller.instructor;

import dal.CourseDAO;
import dal.QuestionDAO;
import dal.QuizDAO;
import dal.QuizQuestionDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Course;
import model.Quiz;

import java.io.IOException;
import java.util.List;
import model.Question;
import model.QuizQuestion;

@WebServlet("/instructor/quiz-detail")
public class QuizDetailServlet extends HttpServlet {

    private QuizDAO quizDAO;
    private CourseDAO courseDAO;
    private QuestionDAO questionDAO;
    private QuizQuestionDAO quizQuestionDAO;

    @Override
    public void init() throws ServletException {
        quizDAO = new QuizDAO();
        courseDAO = new CourseDAO();
        questionDAO = new QuestionDAO();
        quizQuestionDAO = new QuizQuestionDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        Quiz quiz = null;

        if (idParam != null && !idParam.isEmpty()) {
            int quizID = Integer.parseInt(idParam);
            quiz = quizDAO.getQuizByID(quizID);
        }
        if (quiz != null) {
            // Load ngân hàng câu hỏi theo CourseID
            List<Question> availableQuestions = questionDAO.getQuestionsByCourseID(quiz.getCourseID());
            // Load câu hỏi đã gán vào quiz
            List<QuizQuestion> quizQuestions = quizQuestionDAO.getQuestionsByQuizID(quiz.getQuizID());

            request.setAttribute("availableQuestions", availableQuestions);
            request.setAttribute("quizQuestions", quizQuestions);
        }

        // List of all courses for dropdown
        List<Course> courses = courseDAO.getAllCourses();

        request.setAttribute("quiz", quiz);
        request.setAttribute("courses", courses);
        request.getRequestDispatcher("/instructor/quiz-detail.jsp").forward(request, response);
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    request.setCharacterEncoding("UTF-8");

    String manageAction = request.getParameter("manageAction");

    // ✅ 1️⃣ xử lý thêm câu hỏi vào quiz
    if ("addQuestion".equals(manageAction)) {
        int quizID = Integer.parseInt(request.getParameter("quizID"));
        int questionID = Integer.parseInt(request.getParameter("questionID"));
        int points = Integer.parseInt(request.getParameter("points"));

        QuizQuestion qq = new QuizQuestion();
        qq.setQuizID(quizID);
        qq.setQuestionID(questionID);
        qq.setPoints(points);

        quizQuestionDAO.addQuizQuestion(qq);

        response.sendRedirect("quiz-detail?id=" + quizID);
        return;
    }

    // ✅ 2️⃣ xử lý xoá câu hỏi khỏi quiz
    if ("deleteQuestion".equals(manageAction)) {
        int quizID = Integer.parseInt(request.getParameter("quizID"));
        int questionID = Integer.parseInt(request.getParameter("questionID"));

        quizQuestionDAO.deleteQuizQuestion(quizID, questionID);

        response.sendRedirect("quiz-detail?id=" + quizID);
        return;
    }

    // ✅ 3️⃣ Nếu không có manageAction ➜ là submit form Quiz
    String idParam = request.getParameter("quizID");

    int courseID = Integer.parseInt(request.getParameter("courseID"));
    String title = request.getParameter("title");
    String description = request.getParameter("description");
    int duration = Integer.parseInt(request.getParameter("duration"));
    String difficulty = request.getParameter("difficulty");
    boolean isActive = "on".equals(request.getParameter("isActive"));

    Quiz quiz = new Quiz();
    quiz.setCourseID(courseID);
    quiz.setTitle(title);
    quiz.setDescription(description);
    quiz.setDuration(duration);
    quiz.setDifficulty(difficulty);
    quiz.setActive(isActive);

    boolean success;
    if (idParam == null || idParam.isEmpty()) {
        success = quizDAO.addQuiz(quiz);
    } else {
        quiz.setQuizID(Integer.parseInt(idParam));
        success = quizDAO.updateQuiz(quiz);
    }

    if (success) {
        response.sendRedirect("quiz-list");
    } else {
        request.setAttribute("errorMessage", "⚠️ Failed to save quiz. Please try again.");
        List<Course> courses = courseDAO.getAllCourses();
        request.setAttribute("courses", courses);
        request.setAttribute("quiz", quiz);
        request.getRequestDispatcher("/instructor/quiz-detail.jsp").forward(request, response);
    }
}

}
