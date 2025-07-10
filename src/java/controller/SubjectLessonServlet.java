package controller;

import dal.LessonDAO;
import dal.SubjectDAO;
import model.Lesson;
import model.Subject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SubjectLessonServlet", urlPatterns = {"/subjectlesson", "/instructor/subjectlesson"})
public class SubjectLessonServlet extends HttpServlet {

    private static final int DEFAULT_SUBJECT_ID = 1;
    private static final int LESSONS_PER_PAGE = 5;

    private static final Logger LOGGER = Logger.getLogger(SubjectLessonServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        LessonDAO lessonDAO = new LessonDAO();

        String action = request.getParameter("action");
        int subjectIdForRedirect = DEFAULT_SUBJECT_ID;
        int currentPageForRedirect = 1;

        String subjectIdParam = request.getParameter("subjectId");
        if (subjectIdParam != null && !subjectIdParam.isEmpty()) {
            try {
                subjectIdForRedirect = Integer.parseInt(subjectIdParam);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid subjectId parameter for redirect in POST: {0}", subjectIdParam);
            }
        }
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                currentPageForRedirect = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid page parameter for redirect in POST: {0}", pageParam);
            }
        }
        String filterType = request.getParameter("filterType");
        String filterStatus = request.getParameter("filterStatus");
        String searchKeyword = request.getParameter("search");


        if ("toggleStatus".equals(action)) {
            int lessonId = 0;
            String newStatus = null;
            try {
                lessonId = Integer.parseInt(request.getParameter("lessonId"));
                newStatus = request.getParameter("newStatus"); 

                if (newStatus != null && (newStatus.equals("Active") || newStatus.equals("Inactive"))) {
                    if (lessonDAO.updateLessonStatus(lessonId, newStatus)) {
                        // --- CHỈNH SỬA THÔNG BÁO THÀNH CÔNG TẠI ĐÂY ---
                        request.getSession().setAttribute("message", "Lesson status for ID " + lessonId + " updated successfully to " + newStatus + "!");
                        LOGGER.log(Level.INFO, "Lesson {0} status toggled to {1}.", new Object[]{lessonId, newStatus});
                    } else {
                        // --- CHỈNH SỬA THÔNG BÁO LỖI TẠI ĐÂY ---
                        request.getSession().setAttribute("error", "Failed to update lesson status for ID " + lessonId + ".");
                        LOGGER.log(Level.SEVERE, "Failed to update status for lesson {0}.", lessonId);
                    }
                } else {
                    // --- CHỈNH SỬA THÔNG BÁO LỖI KHÔNG HỢP LỆ TẠI ĐÂY ---
                    request.getSession().setAttribute("error", "Invalid new status provided for lesson ID " + lessonId + ".");
                    LOGGER.log(Level.WARNING, "Invalid new status for lesson {0}: {1}", new Object[]{lessonId, newStatus});
                }
            } catch (NumberFormatException e) {
                // --- CHỈNH SỬA THÔNG BÁO LỖI ĐỊNH DẠNG TẠI ĐÂY ---
                request.getSession().setAttribute("error", "Invalid lesson ID or new status format.");
                LOGGER.log(Level.SEVERE, "Invalid lesson ID or new status format for status toggle.", e);
            } catch (Exception e) {
                // --- CHỈNH SỬA THÔNG BÁO LỖI CHUNG TẠI ĐÂY ---
                request.getSession().setAttribute("error", "An unexpected error occurred while updating status: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Unexpected error during status toggle.", e);
            }
            
            response.sendRedirect(buildRedirectUrl(request, subjectIdForRedirect, filterType, filterStatus, searchKeyword, currentPageForRedirect));
            return;
        } 
        else if ("delete".equals(action)) {
            int lessonId = -1;
            try {
                lessonId = Integer.parseInt(request.getParameter("lessonId"));

                if (lessonDAO.deleteLesson(lessonId)) {
                    // --- CHỈNH SỬA THÔNG BÁO THÀNH CÔNG KHI XÓA TẠI ĐÂY ---
                    request.getSession().setAttribute("message", "Lesson deleted successfully!");
                    LOGGER.log(Level.INFO, "Lesson with ID {0} deleted.", lessonId);
                } else {
                    // --- CHỈNH SỬA THÔNG BÁO LỖI KHI XÓA TẠI ĐÂY ---
                    request.getSession().setAttribute("error", "Failed to delete lesson.");
                    LOGGER.log(Level.SEVERE, "Failed to delete lesson with ID {0}.", lessonId);
                }
            } catch (NumberFormatException e) {
                // --- CHỈNH SỬA THÔNG BÁO LỖI ĐỊNH DẠNG KHI XÓA TẠI ĐÂY ---
                request.getSession().setAttribute("error", "Invalid lesson ID for deletion.");
                LOGGER.log(Level.SEVERE, "Invalid lessonId parameter for delete action.", e);
            }
            response.sendRedirect(buildRedirectUrl(request, subjectIdForRedirect, filterType, filterStatus, searchKeyword, currentPageForRedirect));
            return;
        }

        handleRequest(request, response);
    }

    protected void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        LessonDAO lessonDAO = new LessonDAO();
        SubjectDAO subjectDAO = new SubjectDAO();

        String subjectIdParam = request.getParameter("subjectId");
        int subjectId;
        try {
            subjectId = (subjectIdParam != null && !subjectIdParam.isEmpty()) ? Integer.parseInt(subjectIdParam) : DEFAULT_SUBJECT_ID;
            Subject subject = subjectDAO.getSubjectById(subjectId);
            if (subject == null) {
                LOGGER.log(Level.WARNING, "Subject with ID {0} not found. Defaulting to ID {1}", new Object[]{subjectId, DEFAULT_SUBJECT_ID});
                subjectId = DEFAULT_SUBJECT_ID;
                // --- CHỈNH SỬA THÔNG BÁO LỖI CHỦ ĐỀ KHÔNG TÌM THẤY TẠI ĐÂY ---
                request.setAttribute("error", "Subject with ID " + subjectIdParam + " not found. Displaying lessons for default Subject ID " + DEFAULT_SUBJECT_ID + ".");
            } else {
                request.setAttribute("subjectName", subject.getName());
            }

        } catch (NumberFormatException e) {
            subjectId = DEFAULT_SUBJECT_ID;
            // --- CHỈNH SỬA THÔNG BÁO LỖI ID CHỦ ĐỀ KHÔNG HỢP LỆ TẠI ĐÂY ---
            request.setAttribute("error", "Invalid Subject ID provided. Displaying lessons for default Subject ID " + DEFAULT_SUBJECT_ID + ".");
            LOGGER.log(Level.WARNING, "Invalid subjectId parameter: {0}. Defaulting to {1}", new Object[]{subjectIdParam, DEFAULT_SUBJECT_ID});
        }
        
        String lessonType = request.getParameter("filterType");
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
        
        if (request.getSession().getAttribute("message") != null) {
            request.setAttribute("message", request.getSession().getAttribute("message"));
            request.getSession().removeAttribute("message");
        }
        if (request.getSession().getAttribute("error") != null) {
            request.setAttribute("error", request.getSession().getAttribute("error"));
            request.getSession().removeAttribute("error");
        }
        
        int totalLessons = lessonDAO.getTotalLessons(subjectId, lessonType, status, searchKeyword);
        int totalPages = (int) Math.ceil((double) totalLessons / LESSONS_PER_PAGE);

        if (totalPages > 0 && currentPage > totalPages) {
            currentPage = totalPages;
        } else if (currentPage < 1) {
            currentPage = 1;
        }

        int offset = (currentPage - 1) * LESSONS_PER_PAGE;
        List<Lesson> lessons = lessonDAO.getLessonsByCourseId(subjectId, lessonType, status, searchKeyword, offset, LESSONS_PER_PAGE);
        
        Set<String> uniqueTypes = lessonDAO.getUniqueLessonTypesByCourseId(subjectId);

        request.setAttribute("lessons", lessons);
        request.setAttribute("subjectId", subjectId);

        request.setAttribute("uniqueTypes", uniqueTypes);
        request.setAttribute("selectedType", lessonType != null ? lessonType : "");
        request.setAttribute("selectedStatus", status != null ? status : "");
        request.setAttribute("search", searchKeyword != null ? searchKeyword : "");

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("lessonsPerPage", LESSONS_PER_PAGE);

        LOGGER.log(Level.INFO, "SubjectLessonServlet: Forwarding to /instructor/SubjectLesson.jsp for subject ID: {0}", subjectId);
        request.getRequestDispatcher("/instructor/SubjectLesson.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for displaying subject lesson list and managing lesson status/deletion.";
    }
    
    private String buildRedirectUrl(HttpServletRequest request, int subjectId, String lessonType, String status, String searchKeyword, int currentPage) {
        StringBuilder redirectURL = new StringBuilder(request.getContextPath());
        redirectURL.append("/subjectlesson?subjectId=").append(subjectId);

        if (lessonType != null && !lessonType.isEmpty()) {
            redirectURL.append("&filterType=").append(lessonType);
        }
        if (status != null && !status.isEmpty()) {
            redirectURL.append("&filterStatus=").append(status);
        }
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            redirectURL.append("&search=").append(searchKeyword);
        }
        redirectURL.append("&page=").append(currentPage);
        
        return redirectURL.toString();
    }
}