package controller.student;

import dal.EnrollmentDAO;
import dto.EnrollmentDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import model.User;
import org.json.JSONObject;

public class PaymentServlet extends HttpServlet {

    private static final String CLIENT_ID = "2957b83e-32ed-4389-a526-655f3b82d72f";
    private static final String API_KEY = "61fe5853-3470-4971-a896-e67a4f1db2ea";
    private static final String CHECKSUM_KEY = "4cd3b8adec3b43be9a00b6885017540621497d178ebb5ced4931c7bc321300d7";
    private static final String RETURN_URL = "http://localhost:8080/SoftSkill25/payment-return.jsp";
    private static final String CANCEL_URL = "http://localhost:8080/SoftSkill25/payment-cancel.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enrollmentIdStr = request.getParameter("id");
        if (enrollmentIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing enrollment ID");
            return;
        }

        int enrollmentId = Integer.parseInt(enrollmentIdStr);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        EnrollmentDAO dao = new EnrollmentDAO();
        EnrollmentDTO e = dao.getEnrollmentDTOById(enrollmentId, user.getUserId());

        if (e == null || !"Submitted".equals(e.getStatus())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Enrollment not available or already paid");
            return;
        }

        int amount = e.getTotalPrice().intValue(); // BigDecimal to int
        String returnUrlWithId = RETURN_URL + "?id=" + enrollmentId;

        String rawData = "amount=" + amount +
                         "&cancelUrl=" + CANCEL_URL +
                         "&description=Thanh toán đơn #" + enrollmentId +
                         "&orderCode=" + enrollmentId +
                         "&returnUrl=" + returnUrlWithId;

        String signature = hmacSHA256(rawData, CHECKSUM_KEY);

        JSONObject body = new JSONObject();
        body.put("orderCode", enrollmentId);
        body.put("amount", amount);
        body.put("description", "Thanh toán đơn #" + enrollmentId);
        body.put("returnUrl", returnUrlWithId);
        body.put("cancelUrl", CANCEL_URL);
        body.put("signature", signature);

        // Gọi PayOS API
        URL url = new URL("https://api-merchant.payos.vn/v2/payment-requests");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("x-client-id", CLIENT_ID);
        conn.setRequestProperty("x-api-key", API_KEY);
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.toString().getBytes("UTF-8"));
        }

        String responseText = new BufferedReader(new InputStreamReader(
                conn.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST ?
                        conn.getInputStream() : conn.getErrorStream(), "UTF-8"))
                .lines().collect(Collectors.joining("\n"));

        JSONObject json = new JSONObject(responseText);

        if (json.has("data") && !json.isNull("data")) {
            String checkoutUrl = json.getJSONObject("data").getString("checkoutUrl");
            response.sendRedirect(checkoutUrl);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("<h3>Lỗi tạo thanh toán:</h3>");
            response.getWriter().println("<pre>" + responseText + "</pre>");
        }
    }

    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // Đọc nội dung JSON mà PayOS gửi tới
    String body = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"))
            .lines().collect(Collectors.joining("\n"));
    JSONObject json = new JSONObject(body);

    // Lấy orderCode (chính là enrollmentId)
    int enrollmentId = json.getInt("orderCode");
    String status = json.getString("status"); // PAYOS gửi status = "PAID" nếu thành công

    if ("PAID".equals(status)) {
        EnrollmentDAO dao = new EnrollmentDAO();
        boolean success = dao.updateStatus(enrollmentId, "Confirmed");

        if (success) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("Status updated successfully");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failed to update status");
        }
    } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().println("Invalid payment status: " + status);
    }
}


    private String hmacSHA256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secretKey);
            byte[] hash = sha256_HMAC.doFinal(data.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error creating signature", e);
        }
    }
}
