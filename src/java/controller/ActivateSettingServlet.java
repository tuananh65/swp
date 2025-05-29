package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.SettingListDAO;
import java.io.IOException;

@WebServlet("/activateSetting")
public class ActivateSettingServlet extends HttpServlet {
    private SettingListDAO settingDAO;

    @Override
    public void init() throws ServletException {
        settingDAO = new SettingListDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int settingId = Integer.parseInt(request.getParameter("id"));
        settingDAO.activateSetting(settingId);
        response.sendRedirect(request.getContextPath() + "/settingList"); // Chuyển hướng về trang setting list
    }
}