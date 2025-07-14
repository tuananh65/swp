package controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User; // Adjust package if necessary
import dal.UserDAO; // Adjust package if necessary

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp; // Import Timestamp if using for CreatedAt
// import org.mindrot.jbcrypt.BCrypt; // Uncomment if you implement BCrypt for password hashing

@WebServlet(name = "AddUserServlet", urlPatterns = {"/addUser"})
public class AddUserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Đường dẫn JSP đã được điều chỉnh thành /admin/addUser.jsp theo yêu cầu của bạn
        request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Lấy các tham số từ form, bao gồm userName
        String userName = request.getParameter("userName");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String gender = request.getParameter("gender");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String avatarUrl = request.getParameter("avatarUrl"); // Assuming you might add this later or handle default
        String roleName = request.getParameter("role");
        String status = request.getParameter("status");

        // Validation cơ bản: kiểm tra các trường bắt buộc
        if (userName == null || userName.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            roleName == null || roleName.trim().isEmpty()) {

            request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc (Username, Full Name, Email, Password, Role).");
            // Giữ lại dữ liệu đã nhập trong request scope để hiển thị lại trên form
            request.setAttribute("enteredUserName", userName);
            request.setAttribute("enteredFullName", fullName);
            request.setAttribute("enteredEmail", email);
            request.setAttribute("enteredGender", gender);
            request.setAttribute("enteredPhone", phone);
            request.setAttribute("enteredAddress", address);
            request.setAttribute("enteredAvatarUrl", avatarUrl);
            request.setAttribute("enteredRole", roleName);
            request.setAttribute("enteredStatus", status);
            // Forward lại về trang form với thông báo lỗi
            request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
            return; // Dừng xử lý
        }

        try {
            // Kiểm tra xem email đã tồn tại chưa
            if (userDAO.isEmailExists(email)) {
                request.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng sử dụng một email khác.");
                // Giữ lại dữ liệu đã nhập
                request.setAttribute("enteredUserName", userName);
                request.setAttribute("enteredFullName", fullName);
                request.setAttribute("enteredEmail", email);
                request.setAttribute("enteredGender", gender);
                request.setAttribute("enteredPhone", phone);
                request.setAttribute("enteredAddress", address);
                request.setAttribute("enteredAvatarUrl", avatarUrl);
                request.setAttribute("enteredRole", roleName);
                request.setAttribute("enteredStatus", status);
                request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
                return; // Dừng xử lý
            }
            
            // Kiểm tra xem username đã tồn tại chưa (quan trọng nếu username là duy nhất)
            if (userDAO.isUsernameExists(userName)) { // You'll need to add this method to UserDAO
                request.setAttribute("errorMessage", "Username đã tồn tại. Vui lòng sử dụng một username khác.");
                // Giữ lại dữ liệu đã nhập
                request.setAttribute("enteredUserName", userName);
                request.setAttribute("enteredFullName", fullName);
                request.setAttribute("enteredEmail", email);
                request.setAttribute("enteredGender", gender);
                request.setAttribute("enteredPhone", phone);
                request.setAttribute("enteredAddress", address);
                request.setAttribute("enteredAvatarUrl", avatarUrl);
                request.setAttribute("enteredRole", roleName);
                request.setAttribute("enteredStatus", status);
                request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
                return; // Dừng xử lý
            }


            // Tạo đối tượng User mới
            User newUser = new User();
            newUser.setUserName(userName);
            // Mật khẩu PHẢI được hash trong ứng dụng thực tế. Dòng này chỉ để tạm thời.
            // String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            // newUser.setPassword(hashedPassword);
            newUser.setPassword(password); // Tạm thời để plaintext nếu chưa có hashing
            newUser.setFullName(fullName);
            newUser.setGender(gender);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setAddress(address);
            newUser.setAvatarUrl(avatarUrl != null && !avatarUrl.isEmpty() ? avatarUrl : "default_avatar.png"); // Default avatar
            newUser.setStatus(status != null && !status.trim().isEmpty() ? status : "Active"); // Giá trị mặc định
            newUser.setCreatedAt(new Date()); // Thời gian tạo là hiện tại
            newUser.setActivated(true); // Mặc định là đã kích hoạt khi admin tạo

            // Gọi phương thức để thêm người dùng vào DB
            boolean success = userDAO.createUserByAdmin(newUser);

            if (success) {
                // Thêm thông báo thành công vào session để hiển thị sau khi redirect
                request.getSession().setAttribute("successMessage", "Người dùng '" + fullName + "' đã được thêm thành công!");
                // Chuyển hướng đến trang danh sách người dùng
                response.sendRedirect(request.getContextPath() + "/userlist");
            } else {
                request.setAttribute("errorMessage", "Không thể thêm người dùng. Vui lòng kiểm tra lại thông tin và thử lại.");
                // Giữ lại dữ liệu đã nhập khi thêm thất bại
                request.setAttribute("enteredUserName", userName);
                request.setAttribute("enteredFullName", fullName);
                request.setAttribute("enteredEmail", email);
                request.setAttribute("enteredGender", gender);
                request.setAttribute("enteredPhone", phone);
                request.setAttribute("enteredAddress", address);
                request.setAttribute("enteredAvatarUrl", avatarUrl);
                request.setAttribute("enteredRole", roleName);
                request.setAttribute("enteredStatus", status);
                request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
            request.setAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình xử lý: " + e.getMessage());
            // Giữ lại dữ liệu đã nhập khi có lỗi hệ thống
            request.setAttribute("enteredUserName", userName);
            request.setAttribute("enteredFullName", fullName);
            request.setAttribute("enteredEmail", email);
            request.setAttribute("enteredGender", gender);
            request.setAttribute("enteredPhone", phone);
            request.setAttribute("enteredAddress", address);
            request.setAttribute("enteredAvatarUrl", avatarUrl);
            request.setAttribute("enteredRole", roleName);
            request.setAttribute("enteredStatus", status);
            request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
        }
    }
}