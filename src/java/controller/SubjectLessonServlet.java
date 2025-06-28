package controller;

import dal.LessonDAO;
import dal.SubjectDAO;
import model.Lesson;
import model.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // Giữ nguyên chú thích nếu bạn dùng web.xml
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

// Xóa hoặc comment dòng này nếu bạn chỉ dùng web.xml để mapping servlet
@WebServlet(name = "SubjectLessonServlet", urlPatterns = {"/subjectlesson", "/instructor/subjectlesson"}) // Thêm mapping /subjectlesson nếu chưa có
public class SubjectLessonServlet extends HttpServlet {

    private static final int DEFAULT_SUBJECT_ID = 1;
    private static final int LESSONS_PER_PAGE = 5;

    private static final Logger LOGGER = Logger.getLogger(SubjectLessonServlet.class.getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LOGGER.log(Level.INFO, "SubjectLessonServlet: processRequest started.");
        response.setContentType("text/html;charset=UTF-8");

        String subjectIdParam = request.getParameter("subjectId");
        int subjectId;
        try {
            subjectId = (subjectIdParam != null && !subjectIdParam.isEmpty()) ? Integer.parseInt(subjectIdParam) : DEFAULT_SUBJECT_ID;
        } catch (NumberFormatException e) {
            subjectId = DEFAULT_SUBJECT_ID;
            request.setAttribute("error", "Invalid Subject ID provided. Defaulting to ID " + DEFAULT_SUBJECT_ID + ".");
            LOGGER.log(Level.WARNING, "Invalid subjectId parameter: {0}. Defaulting to {1}", new Object[]{subjectIdParam, DEFAULT_SUBJECT_ID});
        }

        String lessonGroup = request.getParameter("filterType");
        String status = request.getParameter("filterStatus");
        String searchKeyword = request.getParameter("search");

        int currentPage = 1;
        String pageParam = request.getParameter("page");
        try {
            if (pageParam != null && !pageParam.isEmpty()) {
                currentPage = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            currentPage = 1;
            LOGGER.log(Level.WARNING, "Invalid page parameter: {0}. Defaulting to 1.", pageParam);
        }

        LessonDAO lessonDAO = new LessonDAO();
        SubjectDAO subjectDAO = new SubjectDAO();

        // --- Xử lý action POST (Activate/Deactivate/Delete) ---
        if ("POST".equalsIgnoreCase(request.getMethod())) {
            String action = request.getParameter("action");
            String lessonIdParam = request.getParameter("lessonId");
            int lessonId = -1; // Khởi tạo với giá trị không hợp lệ
            
            try {
                lessonId = Integer.parseInt(lessonIdParam);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid Lesson ID for action."); // Dùng session cho thông báo
                LOGGER.log(Level.WARNING, "Invalid lessonId parameter for POST action: {0}", lessonIdParam);
                // Redirect để tránh gửi lại form
                response.sendRedirect(request.getRequestURI() + "?subjectId=" + subjectId +
                                     (lessonGroup != null ? "&filterType=" + lessonGroup : "") +
                                     (status != null ? "&filterStatus=" + status : "") +
                                     (searchKeyword != null ? "&search=" + searchKeyword : "") +
                                     "&page=" + currentPage);
                return;
            }

            boolean success = false;
            String message = "";
            String errorMessage = "";

            if ("activate".equals(action)) {
                success = lessonDAO.updateLessonStatus(lessonId, "Active");
                message = "Lesson status updated to Active successfully!";
                errorMessage = "Failed to activate lesson.";
            } else if ("deactivate".equals(action)) {
                success = lessonDAO.updateLessonStatus(lessonId, "Inactive");
                message = "Lesson status updated to Inactive successfully!";
                errorMessage = "Failed to deactivate lesson.";
            } else if ("delete".equals(action)) { // Xử lý action delete
                success = lessonDAO.deleteLesson(lessonId); // Gọi phương thức deleteLesson
                message = "Lesson deleted successfully!";
                errorMessage = "Failed to delete lesson.";
            }

            if (success) {
                request.getSession().setAttribute("message", message); // Dùng session scope
                LOGGER.log(Level.INFO, "Action '{0}' for Lesson ID {1} successful.", new Object[]{action, lessonId});
            } else {
                request.getSession().setAttribute("error", errorMessage + " Lesson ID: " + lessonId); // Dùng session scope
                LOGGER.log(Level.WARNING, "Action '{0}' for Lesson ID {1} failed.", new Object[]{action, lessonId});
            }

            // Redirect trở lại trang hiện tại sau POST để tránh gửi lại form
            String redirectURL = request.getRequestURI() + "?subjectId=" + subjectId;
            if (lessonGroup != null && !lessonGroup.isEmpty()) {
                redirectURL += "&filterType=" + lessonGroup;
            }
            if (status != null && !status.isEmpty()) {
                redirectURL += "&filterStatus=" + status;
            }
            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                redirectURL += "&search=" + searchKeyword;
            }
            redirectURL += "&page=" + currentPage;
            
            response.sendRedirect(redirectURL);
            return; // Quan trọng: Dừng xử lý ở đây để không forward đến JSP hai lần
        }

        // --- Lấy dữ liệu cho trang (GET hoặc sau POST redirect) ---
        // Lấy thông báo từ session và chuyển sang request để hiển thị, sau đó xóa khỏi session
        if (request.getSession().getAttribute("message") != null) {
            request.setAttribute("message", request.getSession().getAttribute("message"));
            request.getSession().removeAttribute("message");
        }
        if (request.getSession().getAttribute("error") != null) {
            request.setAttribute("error", request.getSession().getAttribute("error"));
            request.getSession().removeAttribute("error");
        }


        int totalLessons = lessonDAO.getTotalLessons(subjectId, lessonGroup, status, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalLessons / LESSONS_PER_PAGE);

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        } else if (currentPage < 1) {
            currentPage = 1;
        }

        int offset = (currentPage - 1) * LESSONS_PER_PAGE;
        List<Lesson> lessons = lessonDAO.getLessonsByCourseId(subjectId, lessonGroup, status, searchKeyword, offset, LESSONS_PER_PAGE);
        Subject subject = subjectDAO.getSubjectById(subjectId);
        Set<String> uniqueTypes = lessonDAO.getUniqueLessonTypesByCourseId(subjectId);

        request.setAttribute("lessons", lessons);
        request.setAttribute("subjectName", subject != null ? subject.getName() : "Unknown Subject");
        request.setAttribute("subjectId", subjectId);

        request.setAttribute("uniqueTypes", uniqueTypes);
        request.setAttribute("selectedType", lessonGroup != null ? lessonGroup : ""); // Dùng rỗng để khớp với option value=""
        request.setAttribute("selectedStatus", status != null ? status : ""); // Dùng rỗng để khớp với option value=""
        request.setAttribute("search", searchKeyword);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("lessonsPerPage", LESSONS_PER_PAGE);

        LOGGER.log(Level.INFO, "SubjectLessonServlet: Forwarding to /instructor/SubjectLesson.jsp");
        request.getRequestDispatcher("/instructor/SubjectLesson.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for displaying subject lesson list and managing lesson status/deletion.";
    }
}