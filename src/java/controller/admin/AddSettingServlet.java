// controller/AddSettingServlet.java
package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.Setting;
import dal.SettingListDAO;

@WebServlet(name = "AddSettingServlet", urlPatterns = "/addSetting")
public class AddSettingServlet extends HttpServlet {
    private SettingListDAO settingListDAO;

    @Override
    public void init() throws ServletException {
        settingListDAO = new SettingListDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("admin/addSetting.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        String orderStr = request.getParameter("order");
        String status = request.getParameter("status");
        boolean hasError = false;
        String errorMessage = "";

        if (type == null || type.trim().isEmpty()) {
            hasError = true;
            errorMessage += "Type cannot be empty. ";
        }
        if (key == null || key.trim().isEmpty()) {
            hasError = true;
            errorMessage += "Key cannot be empty. ";
        }
        if (value == null || value.trim().isEmpty()) {
            hasError = true;
            errorMessage += "Value cannot be empty. ";
        }
        if (status == null || status.trim().isEmpty()) {
            hasError = true;
            errorMessage += "Status must be selected. ";
        }

        Integer order = null;
        if (orderStr != null && !orderStr.isEmpty()) {
            try {
                order = Integer.parseInt(orderStr);
            } catch (NumberFormatException e) {
                hasError = true;
                errorMessage += "Order must be a valid number. ";
            }
        }

        if (hasError) {
                request.setAttribute("errorMessage", errorMessage);
                request.setAttribute("type", type);
                request.setAttribute("key", key);
                request.setAttribute("value", value);
                request.setAttribute("order", orderStr);
                request.setAttribute("status", status);
                request.getRequestDispatcher("admin/addSetting.jsp").forward(request, response); // KIỂM TRA VÀ SỬA NẾU CẦN
                return;
        }

        Setting newSetting = new Setting();
        newSetting.setType(type);
        newSetting.setKey(key);
        newSetting.setValue(value);
        newSetting.setOrder(order);
        newSetting.setStatus(status);

        boolean added = settingListDAO.addSetting(newSetting);

        if (added) {
            response.sendRedirect("settinglist?message=Setting added successfully.");
        } else {
            request.setAttribute("errorMessage", "Failed to add new setting to the database.");
            request.setAttribute("type", type);
            request.setAttribute("key", key);
            request.setAttribute("value", value);
            request.setAttribute("order", orderStr);
            request.setAttribute("status", status);
            request.getRequestDispatcher("admin/addSetting.jsp").forward(request, response);
        }
    }
}