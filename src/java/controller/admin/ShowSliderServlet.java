package controller.admin; // Đảm bảo package đúng

import dal.SliderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ShowSliderServlet", urlPatterns = {"/showSlider"})
public class ShowSliderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sliderIdStr = request.getParameter("sliderId");
        
        if (sliderIdStr != null && !sliderIdStr.trim().isEmpty()) {
            try {
                int sliderId = Integer.parseInt(sliderIdStr);
                SliderDAO sliderDAO = new SliderDAO();
                
                boolean success = sliderDAO.updateSliderStatus(sliderId, "active");
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/sliderlist?message=Slider shown successfully!");
                } else {
                    response.sendRedirect(request.getContextPath() + "/sliderlist?error=Failed to show slider. Please try again.");
                }
                
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/sliderlist?error=Invalid slider ID format.");
                e.printStackTrace();
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/sliderlist?error=Slider ID is missing.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/sliderlist"); // Chuyển hướng về trang danh sách
    }
}