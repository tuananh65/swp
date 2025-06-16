package controller.student;

import dal.CourseDAO;
import dal.EnrollmentDAO;
import dal.PackageDAO;
import dto.EnrollmentDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.Course;
import model.Enrollment;
import model.Package;
import model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "MyRegistrationsServlet", urlPatterns = {"/myRegistrations"})
public class MyRegistrationsServlet extends HttpServlet {

    private final EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private final CourseDAO courseDAO = new CourseDAO();
    private final PackageDAO packageDAO = new PackageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        int userId = currentUser.getUserId();

        List<EnrollmentDTO> registrations = enrollmentDAO.getListEnrollmentsWithFilterByUserId(
                userId, null, null, null, null, null, null, "EnrollmentDate", "DESC", 0, Integer.MAX_VALUE
        );

        request.setAttribute("registrations", registrations);
        request.getRequestDispatcher("/student/myRegistrations.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");

        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/view/SignIn.jsp");
            return;
        }

        int userId = currentUser.getUserId();
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "update":
                    handleUpdate(request, response, userId);
                    break;

                case "register":
                    handleRegister(request, response, userId);
                    break;

                default:
                    response.sendRedirect(request.getContextPath() + "/myRegistrations");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/myRegistrations");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {

        try {
            int enrollmentId = Integer.parseInt(request.getParameter("enrollmentId"));
            Enrollment enrollment = enrollmentDAO.getEnrollmentById(enrollmentId);

            if (enrollment != null && enrollment.getUserId() == userId && "submitted".equals(enrollment.getStatus())) {
                enrollment.setPackageId(Integer.parseInt(request.getParameter("packageId")));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                // validFrom
                String validFromStr = request.getParameter("validFrom");
                if (validFromStr != null && !validFromStr.isEmpty()) {
                    Date validFrom = new Date(sdf.parse(validFromStr).getTime());
                    enrollment.setValidFrom(validFrom);
                }

                // validTo
                String validToStr = request.getParameter("validTo");
                if (validToStr != null && !validToStr.isEmpty()) {
                    Date validTo = new Date(sdf.parse(validToStr).getTime());
                    enrollment.setValidTo(validTo);
                }

                // note
                enrollment.setNote(request.getParameter("note"));

                enrollmentDAO.updateEnrollment(enrollment);
            }

        } catch (NumberFormatException | ParseException e) {
            request.setAttribute("errorMessage", "Invalid input or date format.");
            request.getRequestDispatcher("/student/myRegistrations.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/myRegistrations");
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response, int userId)
            throws IOException {

        try {
            int courseId = Integer.parseInt(request.getParameter("courseId"));
            int packageId = Integer.parseInt(request.getParameter("packageId"));

            Course course = courseDAO.getCourseById(courseId);
            List<Package> packages = packageDAO.getAllPackages();

            Package selectedPackage = packages.stream()
                    .filter(p -> p.getPackageId() == packageId)
                    .findFirst()
                    .orElse(null);

            if (course == null || selectedPackage == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid course or package");
                return;
            }

            // Ngày hiện tại
            java.util.Date now = new java.util.Date();
            java.sql.Date validFrom = new java.sql.Date(now.getTime());

            Calendar cal = Calendar.getInstance();
            cal.setTime(validFrom);
            cal.add(Calendar.DAY_OF_MONTH, selectedPackage.getDurationInDays());
            java.sql.Date validTo = new java.sql.Date(cal.getTimeInMillis());

            double price = course.getSalePrice() * selectedPackage.getPriceModifier();
            BigDecimal totalPrice = BigDecimal.valueOf(price).setScale(2, BigDecimal.ROUND_HALF_UP);

            Enrollment enrollment = new Enrollment();
            enrollment.setUserId(userId);
            enrollment.setCourseId(courseId);
            enrollment.setPackageId(packageId);
            enrollment.setEnrollmentDate(new java.sql.Timestamp(now.getTime()));
            enrollment.setStatus("submitted");
            enrollment.setNote("Initial registration");
            enrollment.setValidFrom(validFrom);
            enrollment.setValidTo(validTo);
            enrollment.setTotalPrice(totalPrice);

            enrollmentDAO.insertEnrollment(enrollment);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid input");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/myRegistrations");
    }

    @Override
    public String getServletInfo() {
        return "MyRegistrationsServlet for displaying and managing user registrations";
    }
}
