package controller.admin;

import model.Subject;
import dal.SubjectDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Servlet xử lý chi tiết và cập nhật thông tin môn học.
 */

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class SubjectDetailServlet extends HttpServlet {
    private SubjectDAO subjectDAO;

    @Override
    public void init() {
        // Khởi tạo DAO để kết nối với cơ sở dữ liệu
        subjectDAO = new SubjectDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("subjectId");
        int id;
        try {
            id = Integer.parseInt(idParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid subject ID");
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        // Lấy thông tin môn học từ cơ sở dữ liệu
        Subject subject = subjectDAO.getSubjectById(id);
        if (subject == null) {
            request.setAttribute("errorMessage", "Subject not found");
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        // Lấy danh sách category từ database
        List<String> categoryList = subjectDAO.getAllCategories();
        request.setAttribute("categoryList", categoryList);
        request.setAttribute("subject", subject);
        request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;

        // Kiểm tra ID hợp lệ
        if (id == 0) {
            request.setAttribute("errorMessage", "Invalid subject ID for update");
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        // Kiểm tra kiểu dữ liệu multipart
        if (!request.getContentType().startsWith("multipart/form-data")) {
            request.setAttribute("errorMessage", "Form must be multipart/form-data");
            
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String categoryName = request.getParameter("categoryName");
        String featuredParam = request.getParameter("featured");
        String description = request.getParameter("description");
        String existingThumbnail = request.getParameter("existingThumbnail");
        String ownerIdParam = request.getParameter("ownerId");
        String status = request.getParameter("status");
        Part thumbnailFile = request.getPart("thumbnailFile");

        // Kiểm tra các tham số bắt buộc
        if (name == null || name.trim().isEmpty() || categoryName == null || status == null || ownerIdParam == null) {
            request.setAttribute("errorMessage", "Missing required parameters");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        int ownerId;
        try {
            ownerId = Integer.parseInt(ownerIdParam);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid owner ID");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        boolean featured = "on".equalsIgnoreCase(featuredParam);
        String thumbnail = existingThumbnail != null && !existingThumbnail.isEmpty() ? existingThumbnail : null;

        // Xử lý tải lên hình ảnh
        try {
            if (thumbnailFile != null && thumbnailFile.getSize() > 0) {
                String uploadPath = getServletContext().getRealPath("") + "uploads";
                Files.createDirectories(Paths.get(uploadPath));
                String fileName = Paths.get(thumbnailFile.getSubmittedFileName()).getFileName().toString();
                if (fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    String filePath = uploadPath + "/" + fileName;
                    thumbnailFile.write(filePath);
                    thumbnail = "/uploads/" + fileName;
                }
            }
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Failed to upload thumbnail");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        // Cập nhật đối tượng Subject
        Subject subject = new Subject();
        subject.setSubjectId(id);
        subject.setName(name);
        subject.setCategoryName(categoryName);
        subject.setFeatured(featured);
        subject.setThumbnail(thumbnail);
        subject.setDescription(description != null ? description : "");
        subject.setOwnerId(ownerId);
        subject.setStatus(status);

        // Thực hiện cập nhật và chuyển hướng
        if (subjectDAO.updateSubject(subject)) {
            response.sendRedirect(request.getContextPath() + "/subjectDetail?subjectId=" + id + "&saved=true");
        } else {
            request.setAttribute("errorMessage", "Failed to update subject");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.setAttribute("categoryList", subjectDAO.getAllCategories());

            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
        }
    }
}