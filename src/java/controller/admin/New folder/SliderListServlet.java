package controller.admin; // Đảm bảo package đúng

import dal.SliderDAO;
import model.Slider;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "SliderListServlet", urlPatterns = {"/sliderlist"})
public class SliderListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SliderDAO sliderDAO = new SliderDAO();

        // Lấy các tham số phân trang, tìm kiếm, lọc
        String pageStr = request.getParameter("page");
        String search = request.getParameter("search");
        String filterStatus = request.getParameter("filterStatus");

        int page = 1;
        int pageSize = 5; // Số lượng slider mỗi trang, có thể điều chỉnh

        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1; // Mặc định về trang 1 nếu có lỗi
            }
        }

        List<Slider> sliders;
        int totalSliders;

        // Sử dụng phương thức getPaginatedSliders() đã được cải tiến
        sliders = sliderDAO.getPaginatedSliders(page, pageSize, search, filterStatus);
        totalSliders = sliderDAO.getTotalSliders(search, filterStatus);

        int totalPages = (int) Math.ceil((double) totalSliders / pageSize);

        request.setAttribute("sliders", sliders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("search", search);
        request.setAttribute("filterStatus", filterStatus);

        // Chuyển tiếp đến JSP
        request.getRequestDispatcher("/admin/SliderList.jsp").forward(request, response);
        // Đảm bảo đường dẫn tới JSP đúng
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}