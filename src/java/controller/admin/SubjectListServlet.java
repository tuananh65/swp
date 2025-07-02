/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller.admin;

import dal.SubjectDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Subject;

/**
 *
 * @author Administrator
 */
public class SubjectListServlet extends HttpServlet {
    private SubjectDAO subjectDAO;

    @Override
    public void init() throws ServletException {
        subjectDAO = new SubjectDAO();
    }
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
            out.println("<title>Servlet SubjectListServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SubjectListServlet at " + request.getContextPath () + "</h1>");
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
        String action = request.getParameter("action") != null ? request.getParameter("action") : "list";

        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteSubject(request, response);
                break;
            case "list":
            default:
                listSubjects(request, response);
                break;
        }
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
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveSubject(request, response);
        }
    }
    
    private void listSubjects(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int pageSize = 5;
        String category = request.getParameter("category");
        String status = request.getParameter("status");
        String search = request.getParameter("search");

        List<Subject> allSubjects = subjectDAO.getAllSubjects(category, status, search);
        for (Subject subject : allSubjects) {
            int lessonCount = subjectDAO.getLessonCountBySubjectId(subject.getSubjectId());
            subject.setLessonCount(lessonCount);
        }
        
        int totalSubjects = allSubjects.size();
        int totalPages = (int) Math.ceil((double) totalSubjects / pageSize);

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalSubjects);
        List<Subject> paginatedSubjects = allSubjects.subList(start, end);

        request.setAttribute("subjects", paginatedSubjects);
        request.setAttribute("categories", subjectDAO.getAllCategories());
        request.setAttribute("statuses", subjectDAO.getAllStatuses());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("subjectList.jsp").forward(request, response);
    }

    private List<Subject> getFilteredSubjects(String category, String status, String search) {
        List<Subject> allSubjects = new ArrayList<>();
        // This is a simplified implementation. In a real application,
        // you'd want to add proper filtering in the DAO layer
        for (int i = 1; i <= 100; i++) { // Simulate getting subjects from DB
            Subject subject = subjectDAO.getSubjectById(i);
            if (subject != null) {
                boolean matches = true;
                if (category != null && !category.isEmpty() && !subject.getCategoryName().equals(category)) {
                    matches = false;
                }
                if (status != null && !status.isEmpty() && !subject.getStatus().equals(status)) {
                    matches = false;
                }
                if (search != null && !search.isEmpty() && 
                    !subject.getName().toLowerCase().contains(search.toLowerCase())) {
                    matches = false;
                }
                if (matches) {
                    allSubjects.add(subject);
                }
            }
        }
        return allSubjects;
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", subjectDAO.getAllCategories());
        request.getRequestDispatcher("subjectForm.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    int id = Integer.parseInt(request.getParameter("id"));
    response.sendRedirect(request.getContextPath() + "/subjectDetail?subjectId=" + id);
}

    private void saveSubject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Subject subject = new Subject();
        subject.setSubjectId(request.getParameter("subjectId") != null && !request.getParameter("subjectId").isEmpty() ? 
            Integer.parseInt(request.getParameter("subjectId")) : 0);
        subject.setName(request.getParameter("name"));
        subject.setFeatured(Boolean.parseBoolean(request.getParameter("featured")));
        subject.setThumbnail(request.getParameter("thumbnail"));
        subject.setDescription(request.getParameter("description"));
        subject.setOwnerId(Integer.parseInt(request.getParameter("ownerId")));
        subject.setStatus(request.getParameter("status"));
        subject.setCategoryName(request.getParameter("categoryName"));

        subjectDAO.updateSubject(subject);
        response.sendRedirect("subjectList?action=list");
    }

    private void deleteSubject(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        subjectDAO.deleteSubject(id);
        response.sendRedirect("subjectList?action=list");
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
