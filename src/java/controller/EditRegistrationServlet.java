package controller;

import dal.CourseDAO;
import dal.EnrollmentDAO;
import dal.PackageDAO;
import dal.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Enrollment;
import model.User;
import model.Course;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/edit-registration")
public class EditRegistrationServlet extends HttpServlet {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(); // ✅ luôn tạo nếu chưa có
        User currentUser = (User) session.getAttribute("currentUser");

        // ✅ Kiểm tra quyền truy cập
        if (currentUser == null || currentUser.getRoleId() != 3) {
            System.out.println("❌ Không có quyền truy cập: " + currentUser);
            response.sendRedirect(request.getContextPath() + "/view/403.jsp");
            return;
        }

        // ✅ Debug session
        System.out.println("➡️ currentUser (GET): " + currentUser.getFullName() + " - RoleID: " + currentUser.getRoleId());

        // ✅ Lấy Enrollment ID từ URL
        int enrollmentId;
        try {
            enrollmentId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ");
            return;
        }

        Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
        if (enrollment == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Course course = new CourseDAO().getCourseById(enrollment.getCourseId());
        User user = userDAO.getUserById(enrollment.getUserId());
        List<model.Package> packageList = new PackageDAO().getAllPackages();

        // ✅ Truyền dữ liệu ra JSP
        request.setAttribute("enrollment", enrollment);
        request.setAttribute("user", user);
        request.setAttribute("course", course);
        request.setAttribute("packageList", packageList);
        request.setAttribute("basePrice", course != null ? course.getOriginalPrice() : null);

        request.getRequestDispatcher("/admin/registration-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        // ✅ Kiểm tra quyền
        if (currentUser == null || currentUser.getRoleId() != 3) {
            System.out.println("❌ Không có quyền sửa.");
            response.sendRedirect(request.getContextPath() + "/view/403.jsp");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        try {
            int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
            String status = request.getParameter("status");
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            Date validFrom = Date.valueOf(request.getParameter("validFrom"));
            Date validTo = Date.valueOf(request.getParameter("validTo"));
            String note = request.getParameter("note");
            String gender = request.getParameter("gender");
            String phone = request.getParameter("phone");

            // ✅ Kiểm tra logic ngày
            if (validTo.before(validFrom)) {
                request.setAttribute("error", "Ngày 'Hiệu lực đến' không thể trước 'Hiệu lực từ'.");
                doGet(request, response); return;
            }

            Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
            if (enrollment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // ✅ Cập nhật dữ liệu
            enrollment.setStatus(status);
            enrollment.setPackageId(packageId);
            enrollment.setValidFrom(validFrom);
            enrollment.setValidTo(validTo);
            enrollment.setNote(note);
            enrollment.setUpdatedByUserId(currentUser.getUserId()); // ✅ Ghi người sửa

            boolean enrollmentUpdated = enrollmentDAO.updateEnrollment(enrollment);
            boolean userUpdated = userDAO.updateUserGenderAndPhone(enrollment.getUserId(), gender, phone);

            if (enrollmentUpdated || userUpdated) {
                response.sendRedirect(request.getContextPath() + "/admin/edit-registration?id=" + enrollmentId + "&success=true");
            } else {
                request.setAttribute("error", "Không thể cập nhật dữ liệu!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi hệ thống: " + e.getMessage());
            doGet(request, response);
        }
    }
}
