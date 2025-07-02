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

    @Override
    public void init() throws ServletException {
        packageDAO = new PackageDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy danh sách tất cả các gói từ DAO
            List<Package> packages = packageDAO.getAllPackages();

            // Đặt danh sách vào thuộc tính request để chuyển sang JSP
            request.setAttribute("packages", packages);

            // Forward đến JSP để hiển thị
            request.getRequestDispatcher("admin/PricePackage.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu có lỗi thì chuyển đến trang lỗi hoặc in lỗi
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách packages.");
        }
    }
}
