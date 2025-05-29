// SettingListServlet.java
package controller;

import dal.SettingListDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Setting;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SettingListServlet", urlPatterns = {"/settingList"})
public class SettingListServlet extends HttpServlet {
    private final SettingListDAO settingListDAO = new SettingListDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchValue = request.getParameter("searchValue");
        String typeFilter = request.getParameter("type");
        String statusFilter = request.getParameter("status");
        String pageParam = request.getParameter("page");
        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                // Handle invalid page number, keep default as 1
            }
        }
        int recordsPerPage = 10; // You can adjust this value

        List<Setting> settingList = settingListDAO.getSettings(searchValue, typeFilter, statusFilter, page, recordsPerPage);
        int totalRecords = settingListDAO.getTotalSettings(searchValue, typeFilter, statusFilter);
        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        List<String> typeList = settingListDAO.getAllTypes();

        request.setAttribute("settingList", settingList);
        request.setAttribute("searchValue", searchValue);
        request.setAttribute("selectedType", typeFilter);
        request.setAttribute("selectedStatus", statusFilter);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("typeList", typeList);

        request.getRequestDispatcher("js/SettingList.jsp").forward(request, response);
    }
}