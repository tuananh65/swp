package controller;

import dal.PackageDAO;
import model.Package;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "PricePackageServlet", urlPatterns = {"/pricePackageList"})
public class PricePackageServlet extends HttpServlet {

    private PackageDAO packageDAO;

    // Khởi tạo DAO khi servlet được tạo
    @Override
    public void init() throws ServletException {
        packageDAO = new PackageDAO();
    }

    // Xử lý GET request: hiển thị danh sách + xử lý xóa + hiển thị form edit
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // -------- XÓA GÓI HỌC (nếu có deleteId trên URL) --------
        String deleteIdStr = request.getParameter("deleteId");
        if (deleteIdStr != null && !deleteIdStr.isEmpty()) {
            try {
                int deleteId = Integer.parseInt(deleteIdStr);
                packageDAO.deletePackage(deleteId); // Xoá trong DB
                response.sendRedirect("pricePackageList"); // Redirect để tránh việc xóa lại khi refresh
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // -------- HIỂN THỊ DANH SÁCH GÓI --------
        List<Package> packages = packageDAO.getAllPackages();
        request.setAttribute("packages", packages);

        // -------- HIỂN THỊ FORM EDIT (nếu có editId trên URL) --------
        String editIdStr = request.getParameter("editId");
        if (editIdStr != null && !editIdStr.isEmpty()) {
            try {
                int editId = Integer.parseInt(editIdStr);
                Package editPkg = packageDAO.getPackageById(editId);
                if (editPkg != null) {
                    request.setAttribute("editPackage", editPkg); // Truyền thông tin gói cần sửa về JSP
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // -------- CHUYỂN HƯỚNG TỚI JSP --------
        request.getRequestDispatcher("admin/PricePackage.jsp").forward(request, response);
    }

    // Xử lý POST request: thêm mới hoặc cập nhật gói
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // -------- LẤY DỮ LIỆU TỪ FORM --------
            String idStr = request.getParameter("packageId");
            int packageId = (idStr == null || idStr.isEmpty()) ? 0 : Integer.parseInt(idStr);
            String name = request.getParameter("name");
            int duration = Integer.parseInt(request.getParameter("durationInDays"));
            double priceModifier = Double.parseDouble(request.getParameter("priceModifier"));

            // -------- TẠO ĐỐI TƯỢNG PACKAGE --------
            Package pkg = new Package();
            pkg.setPackageId(packageId);
            pkg.setName(name);
            pkg.setDurationInDays(duration);
            pkg.setPriceModifier(priceModifier);

            // -------- CHÈN MỚI HOẶC CẬP NHẬT DỮ LIỆU --------
            if (packageId > 0) {
                packageDAO.updatePackage(pkg);
                System.out.println("✔ Đã cập nhật package: " + pkg.getName());
            } else {
                packageDAO.insertPackage(pkg);
                System.out.println("✔ Đã thêm mới package: " + pkg.getName());
            }

            // -------- CHUYỂN HƯỚNG VỀ TRANG DANH SÁCH --------
            response.sendRedirect("pricePackageList");

        } catch (Exception e) {
            // -------- XỬ LÝ LỖI --------
            e.printStackTrace();
            request.setAttribute("errorMessage", "❌ Lỗi khi lưu package: " + e.getMessage());
            doGet(request, response); // Quay lại trang hiển thị kèm thông báo lỗi
        }
    }
}
