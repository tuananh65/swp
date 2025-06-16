package controller.admin; // Đảm bảo package đúng

import dal.SliderDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteSliderServlet", urlPatterns = {"/deleteSlider"})
public class DeleteSliderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String sliderIdStr = request.getParameter("sliderId");
        
        if (sliderIdStr != null && !sliderIdStr.trim().isEmpty()) {
            try {
                int sliderId = Integer.parseInt(sliderIdStr);
                SliderDAO sliderDAO = new SliderDAO();
                
                boolean success = sliderDAO.deleteSlider(sliderId);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/sliderlist?message=Slider deleted successfully!");
                } else {
                    response.sendRedirect(request.getContextPath() + "/sliderlist?error=Failed to delete slider. It might be in use or not exist.");
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