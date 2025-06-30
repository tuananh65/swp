/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.student;

import dal.CourseDAO;
import dal.EnrollmentDAO;
import dal.PackageDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Enrollment;
import model.Package;
import model.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class EditStudentRegistration extends HttpServlet {
   
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
            out.println("<title>Servlet EditStudentRegistration</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet EditStudentRegistration at " + request.getContextPath () + "</h1>");
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
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        PackageDAO packageDAO = new PackageDAO();
        List<Package> packageList = packageDAO.getAllPackages();
        request.setAttribute("packageList", packageList);
        
        User currentUser = (User) session.getAttribute("currentUser");
        try {
            int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));
            int courseId = Integer.parseInt(request.getParameter("courseId"));

            // Kiểm tra gói học
            Package selectedPackage = packageDAO.getPackageById(packageId);
            if (selectedPackage == null) {
                session.setAttribute("error", "Invalid package selected.");
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
                return;
            }

            // Lấy SalePrice từ CourseDAO
            CourseDAO courseDAO = new CourseDAO();
            BigDecimal salePrice = courseDAO.getCourseBasePrice(courseId);
            if (salePrice == null) {
                session.setAttribute("error", "Course sale price not found.");
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
                return;
            }

            // Tính toán total price và valid dates
            BigDecimal totalPrice = salePrice.multiply(BigDecimal.valueOf(selectedPackage.getPriceModifier()));
            Date now = new Date(); // Current time for EnrollmentDate
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            Date validFrom = now;
            cal.add(Calendar.DAY_OF_MONTH, selectedPackage.getDurationInDays());
            Date validTo = cal.getTime();

            // Lấy updatedByUserId từ CourseDAO
            Integer updatedByUserId = courseDAO.getCourseCreatorId(courseId);

            // Tạo đối tượng Enrollment để cập nhật
            Enrollment enrollment = new Enrollment();
            enrollment.setEnrollmentId(enrollmentId);
            enrollment.setUserId(currentUser.getUserId());
            enrollment.setCourseId(courseId);
            enrollment.setEnrollmentDate(now); // Update EnrollmentDate to current time
            enrollment.setPackageId(packageId);
            enrollment.setTotalPrice(totalPrice); // Update TotalPrice
            enrollment.setStatus("Submitted");
            enrollment.setValidFrom(validFrom);
            enrollment.setValidTo(validTo);
            enrollment.setUpdatedByUserId(updatedByUserId);
            enrollment.setOrderId(null);
            enrollment.setNote(null);

            // Cập nhật bản ghi
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            boolean success = enrollmentDAO.updateEnrollment(enrollment);
            
            if (success) {
                session.setAttribute("message", "Registration updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update registration.");
            }
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "An error occurred while updating registration.");
            response.sendRedirect(request.getContextPath() + "/student/dashboard");
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
