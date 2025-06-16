package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import dal.SettingListDAO;

@WebServlet(name = "ToggleSettingStatusServlet", urlPatterns = "/toggleSettingStatus")
public class ToggleSettingStatusServlet extends HttpServlet {
    private SettingListDAO settinglistDAO;

    @Override
    public void init() throws ServletException {
        settinglistDAO = new SettingListDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int settingID = Integer.parseInt(request.getParameter("settingID"));
        String currentStatus = request.getParameter("currentStatus");
        String newStatus = currentStatus.equals("active") ? "inactive" : "active";

        boolean updated = settinglistDAO.updateSettingStatus(settingID, newStatus);

        if (updated) {
            response.sendRedirect("settinglist?page=" + request.getParameter("page") + (request.getParameter("search") != null ? "&search=" + request.getParameter("search") : ""));
        } else {
            // Xử lý lỗi nếu cập nhật không thành công
            response.getWriter().println("Failed to update setting status.");
        }
    }
}