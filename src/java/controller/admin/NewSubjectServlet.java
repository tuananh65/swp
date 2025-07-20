package controller.admin;

import dal.SubjectDAO;
import dal.UserDAO;
import model.Subject;
import model.User;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.*;

@WebServlet(name = "NewSubjectServlet", urlPatterns = {"/NewSubjectServlet"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,     // 1MB
    maxFileSize = 1024 * 1024 * 5,       // 5MB
    maxRequestSize = 1024 * 1024 * 10    // 10MB
)
public class NewSubjectServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDAO userDAO = new UserDAO();
        List<User> adminList = userDAO.getAllAdmins();
        request.setAttribute("adminList", adminList);

        request.getRequestDispatcher("/admin/NewSubject.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        boolean featured = "on".equals(request.getParameter("featured"));
        int ownerId = Integer.parseInt(request.getParameter("ownerId"));

        // Tạo SubjectDAO
        SubjectDAO dao = new SubjectDAO();

        // Kiểm tra Subject name đã tồn tại chưa
        if (dao.isSubjectNameExists(name)) {
            request.setAttribute("error", "Tên môn học đã tồn tại trong hệ thống!");

            // Nạp lại danh sách admin
            UserDAO userDao = new UserDAO();
            List<User> adminList = userDao.getAllAdmins();
            request.setAttribute("adminList", adminList);

            request.getRequestDispatcher("/admin/NewSubject.jsp").forward(request, response);
            return;
        }

        try {
            // Xử lý ảnh thumbnail
            Part filePart = request.getPart("thumbnail");
            String fileName = java.nio.file.Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

            String uploadPath = getServletContext().getRealPath("/images/uploads");
            java.io.File uploadDir = new java.io.File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String filePath = uploadPath + java.io.File.separator + fileName;
            filePart.write(filePath);

            String thumbnail = "images/uploads/" + fileName;

            // Gán vào đối tượng Subject
            Subject subject = new Subject();
            subject.setName(name);
            subject.setThumbnail(thumbnail);
            subject.setCategoryName(category);
            subject.setDescription(description);
            subject.setStatus(status);
            subject.setFeatured(featured);
            subject.setOwnerId(ownerId);

            // Gọi DAO để thêm
            boolean success = dao.addSubject(subject);

            if (success) {
                request.setAttribute("success", "Thêm môn học thành công!");
            } else {
                request.setAttribute("error", "Thêm môn học thất bại!");
            }

        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi xử lý: " + e.getMessage());
        }

        // Luôn nạp lại danh sách admin để dropdown không lỗi
        UserDAO userDao = new UserDAO();
        List<User> adminList = userDao.getAllAdmins();
        request.setAttribute("adminList", adminList);

        // Forward lại trang
        request.getRequestDispatcher("/admin/NewSubject.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "New Subject creation servlet";
    }
}
