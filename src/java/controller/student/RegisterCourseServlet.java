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

        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("currentUser");
        
        if (currentUser == null) {
            // Lưu thông tin đăng ký vào session
            session.setAttribute("pendingRegister_courseId", request.getParameter("courseId"));
            session.setAttribute("pendingRegister_packageId", request.getParameter("packageId"));
            session.setAttribute("pendingRegister_basePrice", request.getParameter("basePrice"));

            // Gắn redirect sau đăng nhập về lại servlet này
            session.setAttribute("redirectAfterLogin", request.getRequestURI());

            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        try {
            int userId = currentUser.getUserId();
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));

            // Lấy thông tin gói học
            PackageDAO packageDAO = new PackageDAO();
            Package selectedPackage = packageDAO.getPackageById(packageId);
            // Lấy updatedByUserId từ creator của course
            CourseDAO courseDAO = new CourseDAO();
            Integer instructorId = courseDAO.getCourseCreatorId(courseId);


            if (selectedPackage == null) {
                request.setAttribute("error", "Gói học không hợp lệ.");
                request.getRequestDispatcher("/course-detail.jsp?courseId=" + courseId).forward(request, response);
                return;
            }

            // Tính giá theo hệ số gói học
            BigDecimal basePrice = new BigDecimal(request.getParameter("basePrice")); // Giá gốc truyền từ form
            BigDecimal totalPrice = basePrice.multiply(BigDecimal.valueOf(selectedPackage.getPriceModifier()));

            // Ngày bắt đầu/hết hạn
            Date now = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            Date validFrom = now;
            cal.add(Calendar.DAY_OF_MONTH, selectedPackage.getDurationInDays());
            Date validTo = cal.getTime();

            // Tạo bản ghi Enrollment
            Enrollment enrollment = new Enrollment();
            enrollment.setUserId(userId);
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
                session.setAttribute("message", "Course registration successful!");
            } else {
                session.setAttribute("error", "Registration failed. Please try again.");
            }

            response.sendRedirect("student/dashboard.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred while registering.");
            response.sendRedirect("student/dashboard");
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
