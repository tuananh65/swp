package controller;

import dal.LessonDAO;
import model.Lesson; // Đảm bảo bạn có lớp Lesson trong package model
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,     // 10MB
    maxRequestSize = 1024 * 1024 * 50   // 50MB
)
@WebServlet(name = "LessonDetailServlet", urlPatterns = {
    "/instructor/lessondetail", // Cho cả hiển thị form thêm mới và chỉnh sửa
    "/updateLesson",             // URL xử lý POST cho việc cập nhật
    "/addLesson",                // URL xử lý POST cho việc thêm mới
    "/uploadImage"               // URL xử lý POST cho việc upload ảnh (Froala Editor)
})
public class LessonDetailServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LessonDetailServlet.class.getName());
    // Đường dẫn tương đối để lưu ảnh tải lên
    private static final String UPLOAD_DIRECTORY = "uploads" + File.separator + "images"; 

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        LessonDAO lessonDAO = new LessonDAO();
        Lesson lesson = null;
        
        // Lấy message và error từ session (nếu có sau khi redirect từ POST)
        String message = (String) request.getSession().getAttribute("message");
        String error = (String) request.getSession().getAttribute("error");
        
        // Xóa thuộc tính khỏi session sau khi lấy
        if (message != null) {
            request.getSession().removeAttribute("message");
        }
        if (error != null) {
            request.getSession().removeAttribute("error");
        }

        if (uri.endsWith("/instructor/lessondetail") && request.getParameter("lessonId") == null) {
            // Đây là trường hợp truy cập form thêm mới (LessonDetail.jsp dùng cho cả add và edit)
            // URL: /instructor/lessondetail?subjectId=X (không có lessonId)
            lesson = new Lesson();
            try {
                int subjectId = Integer.parseInt(request.getParameter("subjectId"));
                lesson.setSubjectId(subjectId);
                lesson.setOrder(lessonDAO.getNextOrderForSubject(subjectId)); // Lấy order tiếp theo tự động
                lesson.setStatus("Active"); // Mặc định trạng thái Active
                lesson.setType("Lesson");   // Mặc định loại là Lesson
            } catch (NumberFormatException e) {
                error = "Invalid or missing Subject ID for adding new lesson.";
                LOGGER.log(Level.SEVERE, error, e);
            } catch (Exception e) {
                error = "An error occurred while preparing to add a new lesson: " + e.getMessage();
                LOGGER.log(Level.SEVERE, error, e);
            }
        } else {
            // Trường hợp truy cập form chỉnh sửa (có lessonId)
            // URL: /instructor/lessondetail?lessonId=X&subjectId=Y
            try {
                int lessonId = Integer.parseInt(request.getParameter("lessonId"));
                lesson = lessonDAO.getLessonById(lessonId);
                if (lesson == null) {
                    error = "Lesson not found for ID: " + lessonId;
                    LOGGER.log(Level.WARNING, error);
                }
            } catch (NumberFormatException e) {
                error = "Invalid lesson ID format.";
                LOGGER.log(Level.SEVERE, error, e);
            } catch (Exception e) {
                error = "An error occurred while fetching lesson details: " + e.getMessage();
                LOGGER.log(Level.SEVERE, error, e);
            }
        }
        
        // Đặt thuộc tính vào request scope để JSP hiển thị
        request.setAttribute("lesson", lesson);
        request.setAttribute("message", message);
        request.setAttribute("error", error);

        // Truyền subjectId để nút CANCEL và các liên kết khác hoạt động đúng
        request.setAttribute("subjectId", (lesson != null ? lesson.getSubjectId() : request.getParameter("subjectId")));

        LOGGER.log(Level.INFO, "Serving LessonDetail.jsp (GET). Lesson ID: {0}, Subject ID: {1}", 
                   new Object[]{lesson != null ? lesson.getLessonId() : "N/A (new)", lesson != null ? lesson.getSubjectId() : request.getParameter("subjectId")});
        request.getRequestDispatcher("/instructor/LessonDetail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Xử lý upload ảnh từ Froala Editor
        if (request.getRequestURI().endsWith("/uploadImage")) {
            handleImageUpload(request, response);
            return; // Dừng xử lý sau khi upload ảnh
        }
        
        LessonDAO lessonDAO = new LessonDAO();
        String error = null;

        // Lấy lessonId (sẽ là 0 nếu là thêm mới)
        int lessonId = 0;
        String lessonIdParam = request.getParameter("lessonId");
        if (lessonIdParam != null && !lessonIdParam.isEmpty()) {
            try {
                lessonId = Integer.parseInt(lessonIdParam);
            } catch (NumberFormatException e) {
                error = "Invalid Lesson ID format.";
                LOGGER.log(Level.WARNING, error, e);
            }
        }

        // Lấy subjectId (bắt buộc)
        int subjectId = 0;
        String subjectIdParam = request.getParameter("subjectId");
        if (subjectIdParam != null && !subjectIdParam.isEmpty()) {
            try {
                subjectId = Integer.parseInt(subjectIdParam);
            } catch (NumberFormatException e) {
                error = "Invalid Subject ID format.";
                LOGGER.log(Level.WARNING, error, e);
            }
        } else {
            error = "Subject ID is required.";
            LOGGER.log(Level.WARNING, error);
        }

        // Lấy các tham số khác từ form
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String videoUrl = request.getParameter("videoUrl");
        String status = request.getParameter("status");
        String type = request.getParameter("type");
        
        Integer order = null; // Dùng Integer để có thể null
        String orderParam = request.getParameter("order");
        if (orderParam != null && !orderParam.isEmpty()) {
            try {
                order = Integer.parseInt(orderParam);
            } catch (NumberFormatException e) {
                error = "Order must be a valid number.";
                LOGGER.log(Level.WARNING, error, e);
            }
        }

        // --- Bắt đầu Validation ---
        if (title == null || title.trim().isEmpty()) {
            error = (error == null ? "" : error + " ") + "Title is required.";
        }
        if (order == null || order <= 0) { // Order phải là số dương
            error = (error == null ? "" : error + " ") + "Order must be a positive number.";
        }
        if (type == null || type.trim().isEmpty()) {
            error = (error == null ? "" : error + " ") + "Type is required.";
        }
        if (status == null || status.trim().isEmpty()) {
            error = (error == null ? "" : error + " ") + "Status is required.";
        }
        // VideoUrl và Content có thể là null/empty, không cần check required
        if (videoUrl == null) videoUrl = ""; // Đảm bảo không null
        if (content == null) content = ""; // Đảm bảo không null

        // Tạo đối tượng Lesson từ dữ liệu form (để giữ lại dữ liệu nếu có lỗi)
        Lesson lesson = new Lesson(subjectId, title, content, videoUrl, status, order, type);
        lesson.setLessonId(lessonId); // Đặt lessonId (0 cho thêm mới, >0 cho cập nhật)

        if (error != null) {
            // Nếu có lỗi validation, set attribute và forward lại JSP
            request.setAttribute("lesson", lesson); // Truyền lại lesson object để hiển thị dữ liệu đã nhập
            request.setAttribute("error", error);
            LOGGER.log(Level.WARNING, "Validation error during lesson operation: {0}", error);
            request.getRequestDispatcher("/instructor/LessonDetail.jsp").forward(request, response);
            return;
        }

        // --- Xử lý thêm mới hoặc cập nhật ---
        boolean success;
        String successMessage;
        
        if (lessonId == 0) { // Đây là trường hợp thêm mới
            success = lessonDAO.addLesson(lesson);
            successMessage = success ? "Lesson added successfully!" : "Failed to add lesson.";
            LOGGER.log(Level.INFO, "Attempting to add new lesson: '{0}' for Subject ID {1}. Success: {2}", 
                       new Object[]{title, subjectId, success});
        } else { // Đây là trường hợp cập nhật
            success = lessonDAO.updateLesson(lesson);
            successMessage = success ? "Lesson updated successfully!" : "Failed to update lesson.";
            LOGGER.log(Level.INFO, "Attempting to update lesson ID {0} ('{1}'). Success: {2}", 
                       new Object[]{lessonId, title, success});
        }

        if (success) {
            request.getSession().setAttribute("message", successMessage);
            // Redirect về trang danh sách bài học sau khi thành công
            response.sendRedirect(request.getContextPath() + "/subjectlesson?subjectId=" + subjectId);
        } else {
            // Nếu thao tác DB thất bại, đặt lỗi vào request và forward lại trang
            request.setAttribute("lesson", lesson); // Truyền lại lesson object để người dùng có thể sửa
            request.setAttribute("error", successMessage); // Message thông báo lỗi DB
            LOGGER.log(Level.SEVERE, "Database operation failed for lesson ID {0}: {1}", 
                       new Object[]{lessonId, successMessage});
            request.getRequestDispatcher("/instructor/LessonDetail.jsp").forward(request, response);
        }
    }

    private void handleImageUpload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Lấy đường dẫn thực tế của ứng dụng trên server
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadPath = applicationPath + File.separator + UPLOAD_DIRECTORY;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        try {
            Part filePart = request.getPart("file"); // Lấy file từ request
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // Tên gốc của file
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf(".");
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
            }
            
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension; // Tạo tên file độc nhất
            String filePath = uploadPath + File.separator + uniqueFileName;

            // Lưu file vào thư mục upload
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, new File(filePath).toPath());
            }

            // Trả về URL của ảnh đã upload cho Froala Editor
            String imageUrl = request.getContextPath() + "/" + UPLOAD_DIRECTORY.replace(File.separator, "/") + "/" + uniqueFileName;
            response.getWriter().write("{\"link\":\"" + imageUrl + "\"}");
            LOGGER.log(Level.INFO, "Image uploaded successfully: {0}", imageUrl);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Image upload error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Trả về lỗi 500
            response.getWriter().write("{\"error\":\"Upload failed: " + e.getMessage() + "\"}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles lesson creation, update, and image uploads";
    }
}