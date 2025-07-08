// Package của Servlet (nằm trong package controller.admin)
package controller.admin;

// Import DAO để thao tác DB
import dal.PostDAO;

// Import các thư viện Servlet cần thiết
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part; // Dùng để xử lý phần upload file

// Import model Post
import model.Post;

// Import các class Java khác
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// Định nghĩa URL mapping cho Servlet
@WebServlet("/admin/edit-post")

// Cho phép upload file multipart
@MultipartConfig // Chỉ annotation này mới giúp upload file được
public class EditPostServlet extends HttpServlet {

    // Khởi tạo DAO để làm việc với DB
    private final PostDAO postDAO = new PostDAO();

    // ===============================
    // XỬ LÝ HIỂN THỊ TRANG EDIT (GET)
    // ===============================

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Lấy id từ URL (vd: /admin/edit-post?id=5)
        String idParam = request.getParameter("id");
        Post post = null;

        // Nếu có id hợp lệ
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int id = Integer.parseInt(idParam);
                
                // Lấy post từ DB
                post = postDAO.getPostById(id);
                
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Nếu id không hợp lệ
            }
        }

        // Gửi đối tượng post (có thể null) sang JSP
        request.setAttribute("post", post);
        
        // Forward tới trang edit-post.jsp
        request.getRequestDispatcher("edit-post.jsp").forward(request, response);
    }

    // ===============================
    // XỬ LÝ KHI NGƯỜI DÙNG SUBMIT FORM (POST)
    // ===============================

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Đảm bảo nhận đúng charset UTF-8
        request.setCharacterEncoding("UTF-8");

        // Lấy dữ liệu từ form submit lên
        String idParam = request.getParameter("id");
        String title = request.getParameter("title");
        String category = request.getParameter("category");
        String briefInfo = request.getParameter("briefInfo");
        String content = request.getParameter("content");
        String author = request.getParameter("author");
        String status = request.getParameter("status");
        
        // Checkbox featured → nếu có check thì true, ngược lại false
        boolean featured = request.getParameter("featured") != null;

        // Lấy thời điểm hiện tại làm updatedDate
        Timestamp updatedDate = Timestamp.valueOf(LocalDateTime.now());

        // ========================
        // XỬ LÝ UPLOAD ẢNH
        // ========================

        // Lấy file từ form
        Part filePart = request.getPart("thumbnailFile");
        
        // Lấy tên file (nếu có)
        String fileName = getFileName(filePart);
        
        // Đường dẫn lưu ảnh (cho DB)
        String thumbnail = "";

        if (fileName != null && !fileName.isEmpty()) {
            
            // Đường dẫn thực tế tới /images trong webapp
            String appPath = request.getServletContext().getRealPath("/images");
            
            // Tạo folder nếu chưa có
            File imagesDir = new File(appPath);
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            // Ghi file ảnh vào thư mục images
            filePart.write(appPath + File.separator + fileName);
            System.out.println("Upload path: " + appPath);

            // Lưu path để ghi vào DB (tương đối)
            thumbnail = "images/" + fileName;
            
        } else {
            
            // Nếu không upload ảnh mới (edit post cũ)
            if (idParam != null && !idParam.isEmpty()) {
                try {
                    int id = Integer.parseInt(idParam);
                    
                    // Lấy post cũ từ DB
                    Post existingPost = postDAO.getPostById(id);
                    if (existingPost != null) {
                        // Giữ nguyên ảnh cũ
                        thumbnail = existingPost.getThumbnail();
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }

        // ========================
        // INSERT HOẶC UPDATE
        // ========================

        try {
            // Chuyển id từ String → int (nếu có)
            int id = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;

            // Tạo đối tượng Post từ dữ liệu form
            Post post = new Post(
                    id,
                    title,
                    thumbnail,
                    category,
                    briefInfo,
                    content,
                    author,
                    updatedDate,
                    featured,
                    status
            );

            if (id > 0) {
                // Update post cũ
                postDAO.updatePost(post);
                
                // Redirect lại trang edit (giữ nguyên id)
                response.sendRedirect("edit-post?id=" + id + "&message=Update success");
                
            } else {
                // Insert post mới
                int newId = postDAO.insertPost(post);
                
                // Redirect về trang edit của post mới tạo
                response.sendRedirect("edit-post?id=" + newId + "&message=Insert success");
            }

        } catch (Exception e) {
            e.printStackTrace();
            
            // Nếu có lỗi khi lưu
            request.setAttribute("error", "Error saving post!");
            request.getRequestDispatcher("edit-post.jsp").forward(request, response);
        }
    }

    // ========================
    // HÀM LẤY TÊN FILE UPLOAD
    // ========================
    private String getFileName(Part part) {
        if (part == null) return null;

        // Lấy header content-disposition
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp == null) return null;

        // Duyệt từng phần trong header để lấy filename
        for (String cd : contentDisp.split(";")) {
            if (cd.trim().startsWith("filename")) {
                // Cắt chuỗi để lấy tên file
                String fileName = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                
                // Trả về chỉ tên file (không kèm path)
                return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            }
        }
        return null;
    }
}
