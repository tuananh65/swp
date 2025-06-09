package controller;

import dal.CourseDAO;
import model.Course;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "CourseListServlet", urlPatterns = {"/coursesList", "/courses"})
public class CourseListServlet extends HttpServlet {

    private CourseDAO courseDAO;

    @Override
    public void init() throws ServletException {
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Set encoding
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            // Get parameters
            String searchTerm = request.getParameter("search");
            String categoryFilter = request.getParameter("category");
            String sortBy = request.getParameter("sort");
            String pageParam = request.getParameter("page");
            
            // Pagination
            int page = 1;
            int pageSize = 9; // 3x3 grid
            
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }
            
            // Get all courses
            List<Course> allCourses = courseDAO.getAllCourses();
            
            // Create mock data if database is empty
            if (allCourses.isEmpty()) {
                allCourses = createMockCourses();
            }
            
            // Filter courses
            List<Course> filteredCourses = filterCourses(allCourses, searchTerm, categoryFilter);
            
            // Sort courses
            sortCourses(filteredCourses, sortBy);
            
            // Pagination
            int totalCourses = filteredCourses.size();
            int totalPages = (int) Math.ceil((double) totalCourses / pageSize);
            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalCourses);
            
            List<Course> paginatedCourses = new ArrayList<>();
            if (startIndex < totalCourses) {
                paginatedCourses = filteredCourses.subList(startIndex, endIndex);
            }
            
            // Set attributes
            request.setAttribute("courses", paginatedCourses);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalCourses", totalCourses);
            request.setAttribute("searchTerm", searchTerm != null ? searchTerm : "");
            request.setAttribute("categoryFilter", categoryFilter != null ? categoryFilter : "");
            request.setAttribute("sortBy", sortBy != null ? sortBy : "latest");
            
            // Categories
            String[] categories = {
                "Tất cả danh mục", "Kỹ năng mềm", "Quản lý thời gian", 
                "Lãnh đạo", "Giao tiếp", "Thuyết trình", "Làm việc nhóm"
            };
            request.setAttribute("categories", categories);
            
            // Forward to JSP
            request.getRequestDispatcher("/courselist.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Có lỗi xảy ra: " + e.getMessage());
            request.getRequestDispatcher("/courselist.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    private List<Course> createMockCourses() {
        List<Course> courses = new ArrayList<>();
        
        for (int i = 1; i <= 12; i++) {
            Course course = new Course();
            course.setCourseID(i);
            course.setCourseName("Time Management");
            course.setTagLine("Quản lý thời gian hiệu quả");
            course.setBriefInfo("Học cách quản lý thời gian hiệu quả để tăng năng suất và đạt được sự cân bằng trong cuộc sống.");
            course.setOriginalPrice(250000);
            course.setSalePrice(200000);
            course.setCourseThumbnail("images/instructor.jpg");
            course.setFeatured(i == 2); // Make second course featured
            course.setCreatedAt(new java.util.Date());
            course.setUpdatedAt(new java.util.Date());
            courses.add(course);
        }
        
        return courses;
    }
    
    private List<Course> filterCourses(List<Course> courses, String searchTerm, String categoryFilter) {
        return courses.stream()
                .filter(course -> {
                    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                        String search = searchTerm.toLowerCase();
                        return course.getCourseName().toLowerCase().contains(search) ||
                               course.getTagLine().toLowerCase().contains(search) ||
                               course.getBriefInfo().toLowerCase().contains(search);
                    }
                    return true;
                })
                .filter(course -> {
                    if (categoryFilter != null && !categoryFilter.equals("Tất cả danh mục") && !categoryFilter.isEmpty()) {
                        return course.getCourseName().toLowerCase().contains(categoryFilter.toLowerCase());
                    }
                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
    }
    
    private void sortCourses(List<Course> courses, String sortBy) {
        if (sortBy == null) sortBy = "latest";
        
        switch (sortBy) {
            case "price_low":
                courses.sort((c1, c2) -> Double.compare(c1.getSalePrice(), c2.getSalePrice()));
                break;
            case "price_high":
                courses.sort((c1, c2) -> Double.compare(c2.getSalePrice(), c1.getSalePrice()));
                break;
            case "name":
                courses.sort((c1, c2) -> c1.getCourseName().compareToIgnoreCase(c2.getCourseName()));
                break;
            case "latest":
            default:
                courses.sort((c1, c2) -> c2.getUpdatedAt().compareTo(c1.getUpdatedAt()));
                break;
        }
    }
}