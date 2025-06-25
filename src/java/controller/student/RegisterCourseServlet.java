/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.student;

import dal.CourseDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.EnrollmentDAO;
import dal.PackageDAO;
import dal.UserDAO;
import jakarta.servlet.http.HttpSession;
import model.Enrollment;
import model.Package;
import model.User;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Calendar;
/**
 *
 * @author Administrator
 */
public class RegisterCourseServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterCourseServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterCourseServlet at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User currentUser = (User) session.getAttribute("currentUser");

        try {
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            String basePriceStr = request.getParameter("basePrice");

            // Kiểm tra gói học
            PackageDAO packageDAO = new PackageDAO();
            Package selectedPackage = packageDAO.getPackageById(packageId);
            if (selectedPackage == null) {
                session.setAttribute("error", "Gói học không hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + courseId);
                return;
            }

            // Tính giá
            BigDecimal basePrice = new BigDecimal(basePriceStr);
            BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(selectedPackage.getPriceModifier()));

            // Ngày bắt đầu/hết hạn
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            Date validFrom = now;
            cal.add(Calendar.DAY_OF_MONTH, selectedPackage.getDurationInDays());
            Date validTo = cal.getTime();

            // Lấy updatedByUserId từ creator của course
            CourseDAO courseDAO = new CourseDAO();
            Integer instructorId = courseDAO.getCourseCreatorId(courseId);

            if (currentUser == null) {
                // Trường hợp chưa đăng nhập: Xử lý qua pop-up
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String gender = request.getParameter("gender");

                // Kiểm tra thông tin người dùng
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserByEmail(email);
                if (user == null || !user.getFullName().equalsIgnoreCase(fullName) ||
                    (phone != null && !phone.isEmpty() && !phone.equals(user.getPhone())) ||
                    (gender != null && !gender.isEmpty() && !gender.equals(user.getGender()))) {
                    // Thông tin không khớp
                    session.setAttribute("error", "Thông tin không khớp với tài khoản nào. Vui lòng nhập lại hoặc đăng ký tài khoản.");
                    session.setAttribute("courseId", courseId);
                    response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + courseId);
                    return;
                }

                // Tạo bản ghi Enrollment
                Enrollment enrollment = new Enrollment();
                enrollment.setUserId(user.getUserId());
                enrollment.setCourseId(courseId);
                enrollment.setEnrollmentDate(now);
                enrollment.setPackageId(packageId);
                enrollment.setTotalPrice(totalPrice);
                enrollment.setStatus("Submitted");
                enrollment.setValidFrom(validFrom);
                enrollment.setValidTo(validTo);
                enrollment.setUpdatedByUserId(instructorId);
                enrollment.setOrderId(null);
                enrollment.setNote(null);

                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                boolean success = enrollmentDAO.insertEnrollment(enrollment);

                if (success) {
                    session.setAttribute("message", "Đăng ký khóa học thành công!");
                    session.setAttribute("pendingRegister_courseId", courseId);
                    session.setAttribute("pendingRegister_packageId", packageId);
                    session.setAttribute("pendingRegister_basePrice", basePriceStr);
                    response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + courseId);
                } else {
                    session.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
                    response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + courseId);
                }
            } else {
                // Trường hợp đã đăng nhập: Giữ logic ban đầu
                Enrollment enrollment = new Enrollment();
                enrollment.setUserId(currentUser.getUserId());
                enrollment.setCourseId(courseId);
                enrollment.setEnrollmentDate(now);
                enrollment.setPackageId(packageId);
                enrollment.setTotalPrice(totalPrice);
                enrollment.setStatus("Submitted");
                enrollment.setValidFrom(validFrom);
                enrollment.setValidTo(validTo);
                enrollment.setUpdatedByUserId(instructorId);
                enrollment.setOrderId(null);
                enrollment.setNote(null);

                EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
                boolean success = enrollmentDAO.insertEnrollment(enrollment);

                if (success) {
                    session.setAttribute("message", "Đăng ký khóa học thành công!");
                    response.sendRedirect(request.getContextPath() + "/student/dashboard");
                } else {
                    session.setAttribute("error", "Đăng ký thất bại. Vui lòng thử lại.");
                    response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + courseId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Đã xảy ra lỗi trong quá trình đăng ký.");
            response.sendRedirect(request.getContextPath() + "/CourseDetailServlet?courseId=" + request.getParameter("courseId"));
        }
    }



    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
