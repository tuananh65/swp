package controller.admin;

import model.Subject;
import dal.SubjectDAO; // Đảm bảo đây là SubjectDAO, không phải SubjectDetailDAO
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
// Cần thêm import này nếu bạn có CategoryDAO để lấy categoryName
// import dal.CategoryDAO; 
// Cần thêm import này nếu bạn có UserDAO để lấy ownerId từ owner name
// import dal.UserDAO;

@WebServlet("/subjectDetail")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class SubjectDetailServlet extends HttpServlet {
    private SubjectDAO subjectDAO;
    // Nếu bạn cần CategoryDAO hoặc UserDAO để chuyển đổi, hãy khai báo chúng ở đây
    // private CategoryDAO categoryDAO; 
    // private UserDAO userDAO;

    @Override
    public void init() {
        subjectDAO = new SubjectDAO();
        // Khởi tạo các DAO khác nếu cần
        // categoryDAO = new CategoryDAO();
        // userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String idParam = request.getParameter("id");
        int id = 1; // Giá trị mặc định nếu không có id hoặc id từ session
        try {
            if (idParam != null && !idParam.isEmpty()) {
                id = Integer.parseInt(idParam);
                session.setAttribute("currentSubjectId", id);
            } else if (session.getAttribute("currentSubjectId") != null) {
                id = (Integer) session.getAttribute("currentSubjectId");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid subject ID format.");
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        Subject subject = subjectDAO.getSubjectById(id);
        if (subject == null) {
            request.setAttribute("errorMessage", "Subject not found.");
            // Tạo một đối tượng subject rỗng để tránh NullPointerException trên JSP
            request.setAttribute("subject", new Subject()); 
        } else {
            request.setAttribute("subject", subject);
        }
        
        // Bạn có thể cần lấy danh sách các categories hoặc owners để hiển thị trong form
        // request.setAttribute("categories", subjectDAO.getAllCategories()); // Nếu getAllCategories là tên method của bạn
        
        request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer sessionId = (Integer) session.getAttribute("currentSubjectId");
        String idParam = request.getParameter("id");
        // Đảm bảo id hợp lệ, nếu idParam có, ưu tiên idParam, nếu không dùng session, cuối cùng là 1
        int id = sessionId != null ? sessionId : (idParam != null && !idParam.isEmpty() ? Integer.parseInt(idParam) : 1);

        if (!request.getContentType().startsWith("multipart/form-data")) {
            request.setAttribute("errorMessage", "Form must be multipart/form-data.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        // Đã thay đổi: Lấy categoryName thay vì categoryIdParam
        String categoryName = request.getParameter("categoryName"); 
        String featuredParam = request.getParameter("featured");
        String description = request.getParameter("description");
        String existingThumbnail = request.getParameter("existingThumbnail");
        // Đã thay đổi: Lấy ownerIdStr thay vì owner
        String ownerIdStr = request.getParameter("ownerId"); 
        String status = request.getParameter("status");
        String numberOfLessonParam = request.getParameter("numberOfLesson");
        Part thumbnailFile = request.getPart("thumbnailFile");

        // Kiểm tra các tham số bắt buộc
        if (name == null || name.trim().isEmpty() || categoryName == null || categoryName.trim().isEmpty() || status == null || status.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Missing required parameters (Name, Category, Status).");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        int numberOfLesson = 0;
        int ownerId = 0; // Giá trị mặc định cho ownerId
        try {
            // Đã thay đổi: Kiểm tra ownerIdStr
            if (ownerIdStr != null && !ownerIdStr.trim().isEmpty()) {
                ownerId = Integer.parseInt(ownerIdStr);
            }
            if (numberOfLessonParam != null && !numberOfLessonParam.trim().isEmpty()) {
                numberOfLesson = Integer.parseInt(numberOfLessonParam);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid owner ID or number of lessons format.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        if (name.length() > 255 || (description != null && description.length() > 65535)) {
            request.setAttribute("errorMessage", "Input data too long.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        boolean featured = "on".equalsIgnoreCase(featuredParam);
        String thumbnail = existingThumbnail != null ? existingThumbnail : ""; // Giữ thumbnail cũ nếu không có file mới

        try {
            if (thumbnailFile != null && thumbnailFile.getSize() > 0) {
                String fileName = Paths.get(thumbnailFile.getSubmittedFileName()).getFileName().toString();
                if (!fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    request.setAttribute("errorMessage", "Invalid file type. Only JPG, PNG, GIF allowed.");
                    request.setAttribute("subject", subjectDAO.getSubjectById(id));
                    request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
                    return;
                }
                if (thumbnailFile.getSize() > 10 * 1024 * 1024) { // 10MB limit
                    request.setAttribute("errorMessage", "File size exceeds 10MB limit.");
                    request.setAttribute("subject", subjectDAO.getSubjectById(id));
                    request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
                    return;
                }
                
                // Lưu file vào thư mục "uploads" trong context của ứng dụng
                String uploadPath = getServletContext().getRealPath("/") + "uploads";
                Files.createDirectories(Paths.get(uploadPath)); // Tạo thư mục nếu chưa tồn tại
                String filePath = uploadPath + "/" + fileName;
                thumbnailFile.write(filePath);
                thumbnail = "/uploads/" + fileName; // Lưu đường dẫn tương đối để dễ dàng truy cập từ JSP
            }
        } catch (IOException e) {
            request.setAttribute("errorMessage", "Failed to upload thumbnail: " + e.getMessage());
            request.setAttribute("subject", subjectDAO.getSubjectById(id));
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
            return;
        }

        Subject subject = new Subject();
        subject.setSubjectId(id);
        subject.setName(name);
        // Đã thay đổi: Gọi setCategoryName thay vì setCategoryId
        subject.setCategoryName(categoryName); 
        subject.setFeatured(featured);
        subject.setThumbnail(thumbnail);
        subject.setDescription(description != null ? description : "");
        // Đã thay đổi: Gọi setOwnerId thay vì setOwner
        subject.setOwnerId(ownerId); 
        subject.setNumberOfLesson(numberOfLesson);
        subject.setStatus(status);

        boolean updated = subjectDAO.updateSubject(subject);
        if (updated) {
            session.setAttribute("currentSubjectId", id); // Cập nhật session với ID hiện tại
            response.sendRedirect(request.getContextPath() + "/subjectDetail?id=" + id + "&saved=true");
        } else {
            request.setAttribute("errorMessage", "Failed to update subject. Please check logs for details.");
            request.setAttribute("subject", subjectDAO.getSubjectById(id)); // Lấy lại subject để hiển thị lại form
            request.getRequestDispatcher("/admin/subject-detail.jsp").forward(request, response);
        }
    }
}