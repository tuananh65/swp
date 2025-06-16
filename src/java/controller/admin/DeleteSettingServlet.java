package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import dal.SettingListDAO;

@WebServlet(name = "DeleteSettingServlet", urlPatterns = "/deleteSetting")
public class DeleteSettingServlet extends HttpServlet {
    private SettingListDAO settinglistDAO;

    @Override
    public void init() throws ServletException {
        settinglistDAO = new SettingListDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int settingID = Integer.parseInt(request.getParameter("settingID"));
        boolean deleted = settinglistDAO.deleteSetting(settingID);

        if (deleted) {
            response.sendRedirect("settinglist?message=Setting deleted successfully");
        } else {
            response.sendRedirect("settinglist?error=Failed to delete setting");
        }
    }
}