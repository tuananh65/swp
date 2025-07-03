package controller;

import dal.DimensionDAO;
import model.Dimension;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DimensionServlet", urlPatterns = {"/dimensionList"})
public class DimensionServlet extends HttpServlet {

    private DimensionDAO dimensionDAO;

    @Override
    public void init() throws ServletException {
        dimensionDAO = new DimensionDAO();
    }

    /**
     * Xử lý hiển thị danh sách Dimension, xoá, hoặc chuẩn bị dữ liệu cho sửa
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy subjectId từ URL
            String subjectIdStr = request.getParameter("subjectId");
            int subjectId = (subjectIdStr != null && !subjectIdStr.isEmpty()) 
                    ? Integer.parseInt(subjectIdStr) 
                    : 0;

            // Nếu có tham số deleteId → thực hiện xoá dimension
            String deleteIdStr = request.getParameter("deleteId");
            if (deleteIdStr != null && !deleteIdStr.isEmpty()) {
                int deleteId = Integer.parseInt(deleteIdStr);
                dimensionDAO.deleteDimension(deleteId);

                // Quay lại trang danh sách sau khi xoá
                response.sendRedirect("dimensionList?subjectId=" + subjectId);
                return;
            }

            // Lấy danh sách dimension theo subjectId
            List<Dimension> dimensionList = dimensionDAO.getAllDimensionsBySubjectId(subjectId);
            request.setAttribute("dimensionList", dimensionList);
            request.setAttribute("subjectId", subjectId); // Giữ lại subjectId để dùng lại trong JSP

            // Nếu có tham số editId → lấy thông tin dimension cần sửa
            String editIdStr = request.getParameter("editId");
            if (editIdStr != null && !editIdStr.isEmpty()) {
                int editId = Integer.parseInt(editIdStr);
                Dimension editDimension = dimensionDAO.getDimensionById(editId);
                if (editDimension != null) {
                    request.setAttribute("editDimension", editDimension);
                }
            }

            // Forward đến trang JSP hiển thị
            request.getRequestDispatcher("admin/DimensionList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            // Gửi thông báo lỗi về JSP nếu có lỗi xảy ra
            request.setAttribute("errorMessage", "❌ Lỗi khi xử lý yêu cầu: " + e.getMessage());
            request.getRequestDispatcher("admin/DimensionList.jsp").forward(request, response);
        }
    }

    /**
     * Xử lý thêm mới hoặc cập nhật một Dimension (POST)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Lấy dữ liệu từ form
            String idStr = request.getParameter("dimensionId");
            int dimensionId = (idStr == null || idStr.isEmpty()) ? 0 : Integer.parseInt(idStr);

            int subjectId = Integer.parseInt(request.getParameter("subjectId"));
            String type = request.getParameter("type");
            String dimensionName = request.getParameter("dimensionName");

            // Tạo đối tượng Dimension
            Dimension d = new Dimension();
            d.setDimensionId(dimensionId);
            d.setSubjectId(subjectId);
            d.setType(type);
            d.setDimensionName(dimensionName);

            // Nếu có ID thì cập nhật, nếu không thì thêm mới
            if (dimensionId > 0) {
                dimensionDAO.updateDimension(d);
            } else {
                dimensionDAO.insertDimension(d);
            }

            // Sau khi lưu xong thì redirect về lại danh sách
            response.sendRedirect("dimensionList?subjectId=" + subjectId);

        } catch (Exception e) {
            e.printStackTrace();

            // Nếu lỗi xảy ra khi lưu → hiển thị thông báo lỗi
            request.setAttribute("errorMessage", "❌ Lỗi khi lưu dimension: " + e.getMessage());
            doGet(request, response);
        }
    }
}
