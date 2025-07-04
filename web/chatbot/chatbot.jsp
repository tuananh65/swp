<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="chatbot-container" id="chatbotContainer">
    <div class="chatbot-header">
        <div class="header-info">
            <div class="bot-avatar">
                <i class="fas fa-robot"></i>
            </div>
            <div class="bot-details">
                <h3>Course Advisor</h3>
                <span class="status">Online</span>
            </div>
        </div>
        <button class="close-chatbot" onclick="toggleChatbot()">
            <i class="fas fa-times"></i>
        </button>
    </div>
    <div class="chatbot-messages" id="chatbotMessages">
        <div class="chatbot-message bot-message">
            Hello! How can I assist you with courses?
        </div>
        <div class="quick-questions">
            <p>You can ask me:</p>
            <button class="quick-question" onclick="sendQuickQuestion(this)"><i class="fas fa-graduation-cap"></i>Which courses are suitable for beginners?</button>
            <button class="quick-question" onclick="sendQuickQuestion(this)"><i class="fas fa-dollar-sign"></i>Which course has the lowest price?</button>
            <button class="quick-question" onclick="sendQuickQuestion(this)"><i class="fas fa-clock"></i>Which courses should I take now?</button>
        </div>
    </div>
    <div class="chatbot-input">
        <input type="text" id="chatbotInput" placeholder="Enter your question..." onkeypress="handleKeyPress(event)">
        <button class="send-button" onclick="sendMessage()"><i class="fas fa-paper-plane"></i></button>
    </div>
</div>

<button class="chatbot-toggle" onclick="toggleChatbot()">
    <i class="fas fa-robot"></i>
</button>
<script src="https://www.subbot.io.vn/widget.js" api-key="gECK4hfgiHRxdu251RAVuERzz5FV8fkp"></script>
<script>
    async function sendMessageToServlet(message) {
        try {
            const formData = new URLSearchParams();
            formData.append('message', message);
            const response = await fetch('${pageContext.request.contextPath}/courseChatbot', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                },
                body: formData
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error:', error);
            return { 
                text: "Sorry, I can't connect to the system right now.",
                error: error.message 
            };
        }
    }

    async function sendMessage() {
        const input = document.getElementById('chatbotInput');
        const message = input.value.trim();
        if (message) {
            addMessage(message, 'user');
            input.value = '';
            const response = await sendMessageToServlet(message);
            addMessage(response.text, 'bot');
            addQuickQuestions();
        }
    }

    async function sendQuickQuestion(button) {
        const question = button.textContent;
        addMessage(question, 'user');
        const quickQuestions = document.querySelector('.quick-questions');
        if (quickQuestions) quickQuestions.remove();
        const response = await sendMessageToServlet(question);
        addMessage(response.text, 'bot');
        addQuickQuestions();
    }

    function addMessage(text, sender) {
        const messagesDiv = document.getElementById('chatbotMessages');
        const messageDiv = document.createElement('div');
        messageDiv.className = `chatbot-message ${sender}-message`;
        messageDiv.textContent = text;
        messagesDiv.appendChild(messageDiv);
        messagesDiv.scrollTop = messagesDiv.scrollHeight;
    }

    function addQuickQuestions() {
        const messagesDiv = document.getElementById('chatbotMessages');
        const quickQuestions = document.createElement('div');
        quickQuestions.className = 'quick-questions';
        quickQuestions.innerHTML = `
            <p>You can ask me:</p>
            <button class="quick-question" onclick="sendQuickQuestion(this)">Which courses are suitable for beginners?</button>
            <button class="quick-question" onclick="sendQuickQuestion(this)">Which course has the lowest price?</button>
            <button class="quick-question" onclick="sendQuickQuestion(this)">Which courses should I take now?</button>
        `;
        messagesDiv.appendChild(quickQuestions);
    }

    function handleKeyPress(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    }

    function toggleChatbot() {
        const chatbot = document.getElementById('chatbotContainer');
        chatbot.classList.toggle('active');
    }

    document.addEventListener('DOMContentLoaded', function() {
        setTimeout(toggleChatbot, 2000);
    });
</script>
