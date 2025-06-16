package controller.admin;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import model.Setting;
import dal.SettingListDAO;
import java.util.ArrayList;

@WebServlet(name ="SettingListServlet", urlPatterns = "/settinglist")
public class SettingListServlet extends HttpServlet {
    private SettingListDAO settinglistDAO;

    @Override
    public void init() throws ServletException {
        settinglistDAO = new SettingListDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchKeyword = request.getParameter("search");
        String filterType = request.getParameter("filterType");
        String filterStatus = request.getParameter("filterStatus");
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Setting> settingList = new ArrayList<>();
        int totalRecords = 0;

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            settingList = settinglistDAO.searchSettings(searchKeyword, (page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = settinglistDAO.getTotalSearchResults(searchKeyword);
            request.setAttribute("search", searchKeyword);
        } else if (filterType != null && !filterType.isEmpty()) {
            settingList = settinglistDAO.filterSettingsByType(filterType, (page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = settinglistDAO.getTotalFilteredResultsByType(filterType);
            request.setAttribute("selectedType", filterType);
        } else if (filterStatus != null && !filterStatus.isEmpty()) {
            settingList = settinglistDAO.filterSettingsByStatus(filterStatus, (page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = settinglistDAO.getTotalFilteredResultsByStatus(filterStatus);
            request.setAttribute("selectedStatus", filterStatus);
        } else {
            // chỉ mục lấy từ 0
            settingList = settinglistDAO.getSettingsByPage((page - 1) * recordsPerPage, recordsPerPage);
            totalRecords = settinglistDAO.getTotalSettingCount();
            request.removeAttribute("search");
        }

        int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);

        // Get unique setting types for the filter dropdown
        Set<String> uniqueTypesSet = new HashSet<>();
        List<Setting> allSettingsForTypes = settinglistDAO.getSettingsByPage(0, Integer.MAX_VALUE);
        for (Setting setting : allSettingsForTypes) {
            uniqueTypesSet.add(setting.getType());
        }
        List<String> uniqueTypes = new ArrayList<>(uniqueTypesSet);
        request.setAttribute("uniqueTypes", uniqueTypes);
        request.setAttribute("selectedType", filterType);

        request.setAttribute("settingList", settingList);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.getRequestDispatcher("admin/SettingList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}