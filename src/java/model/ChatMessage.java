package model;

import java.sql.Timestamp;

public class ChatMessage {
    private int id;
    private String sessionId;
    private String userMessage;
    private String botResponse;
    private String intent;
    private double confidence;
    private Timestamp timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String sessionId, String userMessage, String botResponse, 
                      String intent, double confidence) {
        this.sessionId = sessionId;
        this.userMessage = userMessage;
        this.botResponse = botResponse;
        this.intent = intent;
        this.confidence = confidence;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    
    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }
    
    public String getBotResponse() { return botResponse; }
    public void setBotResponse(String botResponse) { this.botResponse = botResponse; }
    
    public String getIntent() { return intent; }
    public void setIntent(String intent) { this.intent = intent; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}