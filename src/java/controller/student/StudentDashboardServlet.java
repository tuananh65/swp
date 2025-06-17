/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.student;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.EnrollmentDAO;
import dto.EnrollmentDTO;
import jakarta.servlet.*;
import model.User;
import jakarta.servlet.http.*;
import java.util.List;
/**
 *
 * @author Administrator
 */
public class StudentDashboardServlet extends HttpServlet {
   
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
            out.println("<title>Servlet StudentDashboardServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet StudentDashboardServlet at " + request.getContextPath () + "</h1>");
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

        // Lấy session hiện tại, không tạo mới nếu chưa có
        HttpSession session = request.getSession(false);
        String search = request.getParameter("search");
        String category = request.getParameter("category");


        // Kiểm tra người dùng đã đăng nhập hay chưa
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        // Lấy người dùng hiện tại từ session
        User currentUser = (User) session.getAttribute("currentUser");
        int userId = currentUser.getUserId();

        // Xử lý huỷ nếu có tham số action=cancel
        String action = request.getParameter("action");
        String enrollmentIdParam = request.getParameter("id");
        if ("cancel".equals(action) && enrollmentIdParam != null) {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdParam);
                EnrollmentDAO dao = new EnrollmentDAO();

                boolean deleted = dao.deleteEnrollment(enrollmentId, userId);
                if (deleted) {
                    session.setAttribute("message", "Enrollment cancelled successfully.");
                } else {
                    session.setAttribute("error", "Failed to cancel enrollment.");
                }

                // Redirect lại để tránh submit lại khi refresh
                response.sendRedirect("dashboard");
                return;

            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid enrollment ID.");
                response.sendRedirect("dashboard");
                return;
            }
        } else if ("confirm".equals(action) && enrollmentIdParam != null) {
            try {
                int enrollmentId = Integer.parseInt(enrollmentIdParam);
                EnrollmentDAO dao = new EnrollmentDAO();
                boolean updated = dao.updateStatusToConfirmed(enrollmentId, userId);
                if (updated) {
                    session.setAttribute("message", "Enrollment confirmed successfully.");
                } else {
                    session.setAttribute("error", "Failed to confirm enrollment.");
                }
                response.sendRedirect("dashboard");
                return;
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid enrollment ID.");
                response.sendRedirect("dashboard");
                return;
            }
        }

        // Gọi DAO để lấy danh sách khóa học đã đăng ký
        EnrollmentDAO dao = new EnrollmentDAO();
        List<EnrollmentDTO> enrollments = dao.getListEnrollmentsWithFilterByUserId(
            userId, 
            category != null && !category.trim().isEmpty() ? category.trim() : null, // Lọc theo category
            search != null && !search.trim().isEmpty() ? search.trim() : null, // Lọc theo search
            null, null, null, null, null, null,
            "EnrollmentID", "desc",
            0, 50
        );

        // Đưa danh sách vào request để truyền sang JSP
        request.setAttribute("myEnrollments", enrollments);
        request.setAttribute("search", search);
        request.setAttribute("category", category);

        // Forward sang giao diện dashboard.jsp để hiển thị
        request.getRequestDispatcher("/student/dashboard.jsp").forward(request, response);
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
        processRequest(request, response);
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
