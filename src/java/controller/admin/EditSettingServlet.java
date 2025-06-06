// controller/EditSettingServlet.java
package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import model.Setting;
import dal.SettingListDAO;

@WebServlet(name = "EditSettingServlet", urlPatterns = "/editSetting")
public class EditSettingServlet extends HttpServlet {
    private SettingListDAO settingListDAO;

    @Override
    public void init() throws ServletException {
        settingListDAO = new SettingListDAO();
    }

@Override
protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String settingIDStr = request.getParameter("settingID");
    if (settingIDStr != null && !settingIDStr.isEmpty()) {
        try {
            int settingID = Integer.parseInt(settingIDStr);
            Setting setting = settingListDAO.getSettingById(settingID);
            if (setting != null) {
                request.setAttribute("setting", setting);
                request.getRequestDispatcher("admin/editSetting.jsp").forward(request, response); // ĐÃ SỬA ĐƯỜNG DẪN
            } else {
                response.sendRedirect("settinglist?error=Setting not found.");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("settinglist?error=Invalid Setting ID.");
        }
    } else {
        response.sendRedirect("settinglist?error=Missing Setting ID.");
    }
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String settingIDStr = request.getParameter("settingID");
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
         try {
             int settingID = Integer.parseInt(settingIDStr);
             Setting existingSetting = new Setting();
             existingSetting.setSettingID(settingID);
             existingSetting.setType(type);
             existingSetting.setKey(key);
             existingSetting.setValue(value);
             existingSetting.setOrder(order);
             existingSetting.setStatus(status);
             request.setAttribute("errorMessage", errorMessage);
             request.setAttribute("setting", existingSetting);
             request.getRequestDispatcher("admin/editSetting.jsp").forward(request, response); // ĐÃ SỬA ĐƯỜNG DẪN
             return;
         } catch (NumberFormatException e) {
             response.sendRedirect("settinglist?error=Invalid Setting ID for edit.");
             return;
         }
     }

     if (settingIDStr != null && !settingIDStr.isEmpty()) {
         try {
             int settingID = Integer.parseInt(settingIDStr);
             Setting updatedSetting = new Setting();
             updatedSetting.setSettingID(settingID);
             updatedSetting.setType(type);
             updatedSetting.setKey(key);
             updatedSetting.setValue(value);
             updatedSetting.setOrder(order);
             updatedSetting.setStatus(status);

             boolean updated = settingListDAO.updateSetting(updatedSetting);

             if (updated) {
                 response.sendRedirect("settinglist?message=Setting updated successfully.");
             } else {
                 request.setAttribute("errorMessage", "Failed to update setting in the database.");
                 request.setAttribute("setting", updatedSetting);
                 request.getRequestDispatcher("admin/editSetting.jsp").forward(request, response); // ĐÃ SỬA ĐƯỜNG DẪN
             }

         } catch (NumberFormatException e) {
             response.sendRedirect("settinglist?error=Invalid Setting ID for update.");
         }
     } else {
         response.sendRedirect("settinglist?error=Missing Setting ID for update.");
     }
    }
}