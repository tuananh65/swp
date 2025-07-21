package controller.ai;

import dal.ChatbotDAO;
import dal.CourseDAO;
import model.ChatMessage;
import model.Course;
import org.json.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;

public class CourseChatServlet extends HttpServlet {
    private ChatbotDAO chatbotDAO;
    private CourseDAO courseDAO;
    private static final String OPENROUTER_API_KEY = "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";
    private static final String OPENROUTER_API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private static final String SITE_URL = "http://localhost:8080"; // Your site URL
    private static final String SITE_NAME = "Course Advisor"; // Your site name

    @Override
    public void init() throws ServletException {
        chatbotDAO = new ChatbotDAO();
        courseDAO = new CourseDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set CORS headers
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            String userMessage = request.getParameter("message");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                throw new IllegalArgumentException("Message cannot be empty");
            }

            String sessionId = request.getSession().getId();
            JSONObject botResponse = processMessage(userMessage);

            // Save chat message to database
            ChatMessage chatMessage = new ChatMessage(
                sessionId,
                userMessage,
                botResponse.getString("text"),
                botResponse.getString("intent"),
                botResponse.getDouble("confidence")
            );
            chatbotDAO.saveChatMessage(chatMessage);

            // Send response
            PrintWriter out = response.getWriter();
            out.print(botResponse.toString());
            out.flush();
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", "An error occurred: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(error.toString());
        }
    }

    private JSONObject processMessage(String message) throws IOException {
        // Fetch course data for context
        List<Course> courses = courseDAO.getAllCourses();
        StringBuilder courseContext = new StringBuilder();
        courseContext.append("List of courses:\n");
        for (Course course : courses) {
            courseContext.append("- ")
                         .append(course.getCourseName())
                         .append(": ")
                         .append(course.getBriefInfo())
                         .append(" (Price: ")
                         .append(course.getSalePrice())
                         .append(", Tag: ")
                         .append(course.getTagLine())
                         .append(")\n");
        }

        // Construct prompt
        String prompt = "You are a course advisor assistant. Based on the course information below, answer the user's question in English, concisely and to the point, listing up to 2 relevant courses when needed, providing details and explanations only when the user requests them (avoid introductory or lengthy explanations):\n\n" +
                        courseContext.toString() + "\n\nUser question: " + message + "\n\nResponse:";

        // Call OpenRouter API
        String openRouterResponse = callOpenRouterApi(prompt);
        JSONObject responseJson = new JSONObject(openRouterResponse);
        String responseText = responseJson.getJSONArray("choices")
                                 .getJSONObject(0)
                                 .getJSONObject("message")
                                 .getString("content");

        // Determine intent and confidence (simplified for OpenRouter/DeepSeek)
        String intent = determineIntent(message);
        double confidence = 0.9; // Placeholder; OpenRouter doesn't provide confidence scores

        JSONObject response = new JSONObject();
        response.put("text", responseText.trim());
        response.put("intent", intent);
        response.put("confidence", confidence);
        return response;
    }

    private String callOpenRouterApi(String prompt) throws IOException {
        URL url = new URL(OPENROUTER_API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + OPENROUTER_API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("HTTP-Referer", SITE_URL);
        conn.setRequestProperty("X-Title", SITE_NAME);
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        // Construct JSON payload
        JSONObject payload = new JSONObject();
        payload.put("model", "gpt-4-1106-preview");
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", prompt);
        messages.put(message);
        payload.put("messages", messages);
        payload.put("max_tokens", 500);
        payload.put("temperature", 0.7);

        // Send request
        try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
            out.print(payload.toString());
            out.flush();
        }

        // Read response
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(conn.getInputStream(), "UTF-8")) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        } catch (IOException e) {
            // Handle error response
            if (conn.getErrorStream() != null) {
                try (Scanner scanner = new Scanner(conn.getErrorStream(), "UTF-8")) {
                    while (scanner.hasNextLine()) {
                        response.append(scanner.nextLine());
                    }
                }
                throw new IOException("OpenRouter API error: " + response.toString());
            }
            throw e;
        } finally {
            conn.disconnect();
        }
        return response.toString();
    }

    private String determineIntent(String message) {
        // Simplified intent detection based on keywords
        message = message.toLowerCase();
        if (message.contains("popular") || message.contains("featured")) {
            return "list_courses";
        } else if (message.contains("cheap") || message.contains("lowest")) {
            return "cheap_courses";
        } else if (message.contains("beginner") || message.contains("start")) {
            return "beginner_courses";
        } else if (message.contains("info") || message.contains("detail")) {
            return "course_info";
        } else {
            return "default";
        }
    }
}
