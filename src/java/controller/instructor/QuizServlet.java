package controller.instructor;

import dal.DBContext;
import dal.QuizDAO;
import model.Course;
import model.Question;
import model.Quiz;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Servlet xử lý hiển thị và cập nhật Quiz TRÊN CÙNG 1 TRANG.
 * - GET: load thông tin quiz + câu hỏi + ngân hàng câu hỏi
 * - POST: lưu thông tin quiz + lưu danh sách câu hỏi đã chọn
 */
@WebServlet("/instructor/quiz")
public class QuizServlet extends HttpServlet {

    // Handle GET: Hiển thị form tạo hoặc chỉnh sửa Quiz
    @Override
 
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String idRaw = request.getParameter("id");
    String removeQRaw = request.getParameter("removeQuestion");

    try (Connection conn = new DBContext().getConnection()) {
        QuizDAO dao = new QuizDAO(conn);

        // ✅ Nếu có yêu cầu remove câu hỏi
        if (idRaw != null && !idRaw.isBlank() && removeQRaw != null && !removeQRaw.isBlank()) {
            int quizId = Integer.parseInt(idRaw);
            int questionId = Integer.parseInt(removeQRaw);
            dao.removeQuestionFromQuiz(quizId, questionId);
        }

        // Load tất cả khóa học
        List<Course> courses = dao.getAllCourses();
        request.setAttribute("courses", courses);

        if (idRaw != null && !idRaw.isBlank()) {
            // ---------- EDIT EXISTING QUIZ ----------
            int quizId = Integer.parseInt(idRaw);
            Quiz quiz = dao.getQuizById(quizId);

            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }

            // Load câu hỏi đã gán
            List<Question> quizQuestions = dao.getQuestionsByQuiz(quizId);
            List<Question> bankQuestions = dao.getAvailableQuestionsForQuiz(quizId, quiz.getCourseID());

            request.setAttribute("quiz", quiz);
            request.setAttribute("quizQuestions", quizQuestions);
            request.setAttribute("bankQuestions", bankQuestions);
            request.setAttribute("isCreate", false);

        } else {
            // ------------ CREATE NEW QUIZ ------------
            String selectedCourseRaw = request.getParameter("courseID");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            String difficulty = request.getParameter("difficulty");
            String durationRaw = request.getParameter("duration");
            boolean active = request.getParameter("active") != null;

            int selectedCourseID = 0;
            if (selectedCourseRaw != null && !selectedCourseRaw.isBlank()) {
                selectedCourseID = Integer.parseInt(selectedCourseRaw);
            }

            int duration = 30;
            if (durationRaw != null && !durationRaw.isBlank()) {
                try {
                    duration = Integer.parseInt(durationRaw);
                } catch (NumberFormatException ignore) {}
            }

            Quiz quiz = new Quiz(0,
                    selectedCourseID,
                    title != null ? title : "",
                    description != null ? description : "",
                    difficulty != null ? difficulty : "Easy",
                    duration,
                    active);

            request.setAttribute("quiz", quiz);

            if (selectedCourseID > 0) {
                List<Question> bankQuestions = dao.getBankQuestionsByCourse(selectedCourseID);
                request.setAttribute("bankQuestions", bankQuestions);
            } else {
                request.setAttribute("bankQuestions", List.of());
            }

            request.setAttribute("quizQuestions", List.of());
            request.setAttribute("isCreate", true);
        }

        request.getRequestDispatcher("/instructor/quiz-detail.jsp").forward(request, response);

    } catch (Exception e) {
        throw new ServletException(e);
    }
}




@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try (Connection conn = new DBContext().getConnection()) {
        QuizDAO dao = new QuizDAO(conn);

        // === 1️⃣ Lấy thông tin quiz ===
        String idRaw = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String difficulty = request.getParameter("difficulty");

        String durationRaw = request.getParameter("duration");
        int duration = 30;
        if (durationRaw != null && !durationRaw.isEmpty()) {
            try {
                duration = Integer.parseInt(durationRaw);
            } catch (NumberFormatException e) {
                throw new ServletException("Invalid duration format.");
            }
        }

        boolean active = request.getParameter("active") != null;

        int courseId = 0;
        String courseIdRaw = request.getParameter("courseID");
        if (courseIdRaw != null && !courseIdRaw.isEmpty()) {
            try {
                courseId = Integer.parseInt(courseIdRaw);
            } catch (NumberFormatException e) {
                throw new ServletException("Invalid course ID format.");
            }
        }

        // === 2️⃣ Tạo mới hoặc update Quiz ===
        Quiz quiz;
        boolean isNew = (idRaw == null || idRaw.isBlank() || "0".equals(idRaw));

        if (isNew) {
            quiz = new Quiz(0, courseId, title, description, difficulty, duration, active);
            int newId = dao.createQuizAndReturnId(quiz);
            quiz.setTestID(newId);
        } else {
            int quizId = Integer.parseInt(idRaw);
            quiz = dao.getQuizById(quizId);
            if (quiz == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Quiz not found.");
                return;
            }
            quiz.setTitle(title);
            quiz.setDescription(description);
            quiz.setDifficulty(difficulty);
            quiz.setDuration(duration);
            quiz.setActive(active);
            quiz.setCourseID(courseId);
            dao.updateQuiz(quiz);
        }

        int quizID = quiz.getTestID();

        // === 3️⃣ Xoá mapping cũ
        dao.clearQuizQuestions(quizID);

        // === 4️⃣ Thêm lại mapping mới
        String[] selectedQuestions = request.getParameterValues("selectedQuestions[]");
        if (selectedQuestions != null) {
            for (String questionIdStr : selectedQuestions) {
                try {
                    int questionId = Integer.parseInt(questionIdStr);
                    String pointsStr = request.getParameter("points_" + questionId);
                    int points = 5;
                    if (pointsStr != null && !pointsStr.trim().isEmpty()) {
                        points = Integer.parseInt(pointsStr.trim());
                    }
                    dao.addQuestionToQuiz(quizID, questionId, points);
                } catch (NumberFormatException e) {
                    // Ignore invalid entry
                }
            }
        }

        // === 5️⃣ Update nội dung và điểm câu hỏi đã gán (edit inline)
        String[] editQuestionIDs = request.getParameterValues("editQuestionID[]");
        String[] editContents = request.getParameterValues("editContent[]");
        String[] editPoints = request.getParameterValues("editPoints[]");

        if (editQuestionIDs != null && editContents != null && editPoints != null) {
            for (int i = 0; i < editQuestionIDs.length; i++) {
                try {
                    int questionId = Integer.parseInt(editQuestionIDs[i]);
                    String content = editContents[i];
                    int points = Integer.parseInt(editPoints[i]);

                    Question q = new Question();
                    q.setQuestionID(questionId);
                    q.setContent(content);
                    q.setPoints(points);

                    dao.updateQuestion(q);
                } catch (NumberFormatException ex) {
                    // Ignore invalid entry
                }
            }
        }

        // === 6️⃣ Redirect
        response.sendRedirect(request.getContextPath() + "/instructor/quiz-list");

    } catch (Exception e) {
        e.printStackTrace();
        throw new ServletException(e);
    }
}


}
