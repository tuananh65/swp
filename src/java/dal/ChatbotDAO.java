package dal;

import model.ChatMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatbotDAO extends DBContext {

    // Lưu tin nhắn chat vào database
    public boolean saveChatMessage(ChatMessage message) {
        String sql = "INSERT INTO ChatHistory (SessionId, UserMessage, BotResponse, Intent, Confidence) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = super.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, message.getSessionId());
            stmt.setString(2, message.getUserMessage());
            stmt.setString(3, message.getBotResponse());
            stmt.setString(4, message.getIntent());
            stmt.setDouble(5, message.getConfidence());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving chat message: " + e.getMessage());
            return false;
        }
    }

    // Lấy lịch sử chat theo session
    public List<ChatMessage> getChatHistory(String sessionId) {
        List<ChatMessage> history = new ArrayList<>();
        String sql = "SELECT * FROM ChatHistory WHERE SessionId = ? ORDER BY Timestamp ASC";
        
        try (Connection conn = super.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                ChatMessage message = new ChatMessage();
                message.setId(rs.getInt("Id"));
                message.setSessionId(rs.getString("SessionId"));
                message.setUserMessage(rs.getString("UserMessage"));
                message.setBotResponse(rs.getString("BotResponse"));
                message.setIntent(rs.getString("Intent"));
                message.setConfidence(rs.getDouble("Confidence"));
                message.setTimestamp(rs.getTimestamp("Timestamp"));
                
                history.add(message);
            }
        } catch (SQLException e) {
            System.err.println("Error getting chat history: " + e.getMessage());
        }
        
        return history;
    }
}