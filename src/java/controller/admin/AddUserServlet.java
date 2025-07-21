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

    String userName = request.getParameter("userName");
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String gender = request.getParameter("gender");
    String phone = request.getParameter("phone");
    String address = request.getParameter("address");
    String avatarUrl = request.getParameter("avatarUrl"); // optional
    String roleName = request.getParameter("role");
    String status = request.getParameter("status");

    // ✅ Chuẩn hóa đầu vào
    if (roleName != null) roleName = roleName.trim();
    if (status != null) {
        status = status.trim().equalsIgnoreCase("inactive") ? "Inactive" : "Active";
    }

    // ❌ Kiểm tra các trường bắt buộc
    if (userName == null || userName.trim().isEmpty() ||
        fullName == null || fullName.trim().isEmpty() ||
        email == null || email.trim().isEmpty() ||
        password == null || password.trim().isEmpty() ||
        roleName == null || roleName.trim().isEmpty()) {

        request.setAttribute("errorMessage", "Vui lòng điền đầy đủ các trường bắt buộc (Username, Full Name, Email, Password, Role).");
        setFormAttributes(request, userName, fullName, email, gender, phone, address, avatarUrl, roleName, status);
        request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
        return;
    }

    try {
        if (userDAO.isEmailExists(email)) {
request.setAttribute("errorMessage", "Email đã tồn tại. Vui lòng sử dụng email khác.");
            setFormAttributes(request, userName, fullName, email, gender, phone, address, avatarUrl, roleName, status);
            request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
            return;
        }

        if (userDAO.isUsernameExists(userName)) {
            request.setAttribute("errorMessage", "Username đã tồn tại. Vui lòng sử dụng username khác.");
            setFormAttributes(request, userName, fullName, email, gender, phone, address, avatarUrl, roleName, status);
            request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
            return;
        }

        // ✅ Hash password với BCrypt
        String hashedPassword = utils.PasswordUtils.hashPassword(password);

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setPassword(hashedPassword); // dùng password đã mã hóa
        newUser.setFullName(fullName);
        newUser.setGender(gender);
        newUser.setEmail(email);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setAvatarUrl((avatarUrl != null && !avatarUrl.isEmpty()) ? avatarUrl : "default_avatar.png");
        newUser.setStatus(status);
        newUser.setCreatedAt(new Date());
        newUser.setActivated(true);

        // ✅ Gọi hàm addUser xử lý roleId
        boolean success = userDAO.addUser(newUser, roleName);

        if (success) {
            request.getSession().setAttribute("successMessage", "Người dùng '" + fullName + "' đã được thêm thành công!");
            response.sendRedirect(request.getContextPath() + "/userlist");
        } else {
            request.setAttribute("errorMessage", "Không thể thêm người dùng. Vui lòng thử lại.");
            setFormAttributes(request, userName, fullName, email, gender, phone, address, avatarUrl, roleName, status);
            request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
        }
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
        setFormAttributes(request, userName, fullName, email, gender, phone, address, avatarUrl, roleName, status);
        request.getRequestDispatcher("/admin/addUser.jsp").forward(request, response);
    }
}

// ✅ Hàm tiện ích để giữ lại dữ liệu đã nhập
private void setFormAttributes(HttpServletRequest request, String userName, String fullName, String email,
                               String gender, String phone, String address, String avatarUrl, String role, String status) {
    request.setAttribute("enteredUserName", userName);
    request.setAttribute("enteredFullName", fullName);
    request.setAttribute("enteredEmail", email);
    request.setAttribute("enteredGender", gender);
request.setAttribute("enteredPhone", phone);
    request.setAttribute("enteredAddress", address);
    request.setAttribute("enteredAvatarUrl", avatarUrl);
    request.setAttribute("enteredRole", role);
    request.setAttribute("enteredStatus", status);
}

}