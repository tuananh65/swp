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
import utils.EmailSender;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/admin/edit-registration")
public class EditRegistrationServlet extends HttpServlet {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final UserDAO userDAO = new UserDAO();

    private boolean hasForceMailFlag(String note) {
        return note != null && note.toLowerCase().contains("nothing");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null || currentUser.getRoleId() != 3) {
            response.sendRedirect(request.getContextPath() + "/view/403.jsp");
            return;
        }

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

        if (currentUser == null || currentUser.getRoleId() != 3) {
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

            boolean forceMailFromNote = hasForceMailFlag(note);

            if (validTo.before(validFrom)) {
                request.setAttribute("error", "Ngày 'Hiệu lực đến' không thể trước 'Hiệu lực từ'.");
                doGet(request, response);
                return;
            }

            Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);
            if (enrollment == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            enrollment.setStatus(status);
            enrollment.setPackageId(packageId);
            enrollment.setValidFrom(validFrom);
            enrollment.setValidTo(validTo);
            enrollment.setNote(note);
            enrollment.setUpdatedByUserId(currentUser.getUserId());

            boolean enrollmentUpdated = enrollmentDAO.updateEnrollment(enrollment);
            boolean userUpdated = userDAO.updateUserGenderAndPhone(enrollment.getUserId(), gender, phone);

            boolean createdAccount = false;
            User user = userDAO.getUserById(enrollment.getUserId());

            if ("Approved".equalsIgnoreCase(status)) {
                if (user == null) {
                    String email = request.getParameter("email");
                    String fullName = request.getParameter("fullName");
                    String genPass = RandomStringUtils.randomAlphanumeric(8);

                    User newUser = new User();
                    newUser.setUserName(email.split("@")[0]);
                    newUser.setPassword(genPass);
                    newUser.setRoleId(2);
                    newUser.setFullName(fullName);
                    newUser.setGender(gender);
                    newUser.setEmail(email);
                    newUser.setPhone(phone);
                    newUser.setStatus("Active");

                    boolean created = userDAO.createUserByAdmin(newUser);
                    if (created) {
                        createdAccount = true;

                        String loginURL = request.getScheme() + "://" + request.getServerName() + ":" +
                  request.getServerPort() + request.getContextPath() + "/view/SignIn.jsp";


                        String subject = "Thông tin tài khoản học tập của bạn";
                        String content = "Xin chào " + newUser.getFullName() + ",\n\n"
                                + "Hệ thống đã ghi nhận bạn đã thanh toán và tự động tạo tài khoản học viên cho bạn.\n\n"
                                + "👉 Link đăng nhập: " + loginURL + "\n"
                                + "📧 Email: " + newUser.getEmail() + "\n"
                                + "🔑 Mật khẩu: " + genPass + "\n\n"
                                + "Vui lòng đăng nhập và đổi mật khẩu ngay sau lần đăng nhập đầu tiên.\n"
                                + "Trân trọng.";

                        EmailSender.send(newUser.getEmail(), subject, content);
                    }
                    // <editor-fold defaultstate="collapsed" desc="// End send email">
                } else if (forceMailFromNote) {
                    createdAccount = true; // để hiển thị popup
                    String loginURL = request.getScheme() + "://" + request.getServerName() + ":" +
                  request.getServerPort() + request.getContextPath() + "/view/SignIn.jsp";

                    String subject = "Thông tin đăng nhập học viên";
                    String content = "Xin chào " + user.getFullName() + ",\n\n"
                            + "Hệ thống đã ghi nhận bạn đã thanh toán và tự động tạo tài khoản học viên cho bạn.\n\n"
                            + "👉 Link đăng nhập: " + loginURL + "\n"
                            + "📧 Email: " + user.getEmail() + "\n\n"
                            + "🔑 Mật khẩu: " + user.getPassword() + "\n\n"
                            + "Vui lòng đăng nhập để bắt đầu học tập.\n"
                            + "Trân trọng.";

                    EmailSender.send(user.getEmail(), subject, content);
                }
                // </editor-fold>
            }

            if (enrollmentUpdated || userUpdated) {
                String redirectURL = request.getContextPath() + "/admin/edit-registration?id=" + enrollmentId + "&success=true";
                if (createdAccount) {
                    redirectURL += "&created=true";
                }
                response.sendRedirect(redirectURL);
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
