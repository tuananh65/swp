package controller.ai;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.sql.*;
import org.json.JSONException;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {

    private static final String API_KEY = "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";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        JSONArray messages = (JSONArray) session.getAttribute("chatHistory");
        if (messages == null) {
            messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system")
                    .put("content", "Bạn là trợ lý tư vấn khóa học online. Trả lời bằng tiếng Việt, văn phong thân thiện."));
        }

        String userMessage = request.getParameter("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            response.getWriter().print("Bạn chưa nhập nội dung.");
            return;
        }

        messages.put(new JSONObject().put("role", "user").put("content", userMessage));
        String reply = "";

        try {
            URL url = new URL("https://openrouter.ai/api/v1/chat/completions");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("HTTP-Referer", "http://localhost:8080/SoftSkill25");
            conn.setRequestProperty("X-Title", "SoftSkill Chatbot");
            conn.setDoOutput(true);

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "openai/gpt-3.5-turbo");
            requestBody.put("messages", messages);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(requestBody.toString().getBytes("utf-8"));
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line.trim());
            }

            JSONObject obj = new JSONObject(result.toString());
            JSONArray choices = obj.getJSONArray("choices");

            reply = choices.getJSONObject(0).getJSONObject("message").optString("content", "[BOT]: Không có phản hồi từ AI.");

            messages.put(new JSONObject().put("role", "assistant").put("content", reply));
            session.setAttribute("chatHistory", messages);

// In ra nội dung đã chuyển dòng
            String htmlReply = reply == null ? "[BOT]: Lỗi phản hồi." : reply.replaceAll("\n", "<br>");
            response.getWriter().print(htmlReply);

        } catch (IOException | JSONException e) {
            response.getWriter().print("Lỗi: " + e.getMessage());
        }
    }

    private void saveToDatabase(String userMessage, String reply) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection dbConn = DriverManager.getConnection(
                    "jdbc:sqlserver://DESKTOP-74CEB32\\KTEAM:1433;databaseName=SoftSkillLearning;encrypt=false;trustServerCertificate=true",
                    "sa", "123"
            )) {
                PreparedStatement ps = dbConn.prepareStatement("INSERT INTO chat_history (user_message, bot_reply) VALUES (?, ?)");
                ps.setString(1, userMessage);
                ps.setString(2, reply);
                ps.executeUpdate();
                ps.close();
            }
        } catch (ClassNotFoundException | SQLException ex) {
        }
    }
}
