package controller.admin; // Đảm bảo package đúng

import dal.SliderDAO;
import model.Slider; // Đảm bảo đã import model.Slider

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet; // Thêm nếu bạn muốn dùng Annotation cho Servlet này
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Thêm annotation nếu bạn muốn định tuyến bằng annotation
@WebServlet(name = "SliderDetailServlet", urlPatterns = {"/sliderdetail"})
public class SliderDetailServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Slider> sliders = null;
        String errorMessage = null;

        try {
            SliderDAO sliderDAO = new SliderDAO();
            // Thay đổi từ getAllSliders() sang getAllActiveSliders()
            sliders = sliderDAO.getAllActiveSliders(); //

            if (sliders == null || sliders.isEmpty()) {
                errorMessage = "No active sliders found.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "Error loading sliders: " + e.getMessage();
        } finally {
            request.setAttribute("sliders", sliders);
            request.setAttribute("errorMessage", errorMessage);
            // Đảm bảo đường dẫn đến JSP là chính xác
            request.getRequestDispatcher("/SliderDetail.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}