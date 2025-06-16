<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    </head>
    <body>

        <!-- Nút mở chatbot -->
        <div id="chat-toggle-button" class="fixed bottom-6 right-6 z-50">
            <button onclick="toggleChatbox()" class="w-14 h-14 bg-gradient-to-br from-indigo-500 to-purple-500 rounded-full shadow-lg text-white flex items-center justify-center text-2xl hover:scale-105 transition-all">
                💬
            </button>
        </div>

        <!-- Hộp chat -->
        <div id="chatbox" class="fixed bottom-24 right-6 w-80 bg-white border border-gray-200 rounded-xl shadow-2xl flex flex-col overflow-hidden z-50 hidden">
            <!-- Header -->
            <div class="bg-gradient-to-r from-indigo-500 to-purple-500 text-white px-4 py-3 font-semibold text-sm flex justify-between items-center">
                <span>Trợ lý Khóa học</span>
                <button onclick="toggleChatbox()" class="text-white text-lg font-bold">×</button>
            </div>

            <!-- Tin nhắn -->
            <div id="messages" class="flex flex-col overflow-y-auto p-4 space-y-2 text-sm text-gray-800 bg-gray-50 h-64 max-h-[300px]">
                <!-- Tin nhắn sẽ được chèn tại đây -->
            </div>

            <!-- Nhập tin nhắn -->
            <div class="p-3 border-t border-gray-200 bg-white flex gap-2">
                <input id="userInput" type="text" placeholder="Nhập câu hỏi..." class="flex-1 border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring focus:border-indigo-400" onkeydown="if (event.key === 'Enter')
                            sendMessage()">
                <button onclick="sendMessage()" class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-lg text-sm">Gửi</button>
            </div>
        </div>

        <!-- Biến contextPath để dùng fetch -->
        <script>
            const ctx = "${pageContext.request.contextPath}";

            function toggleChatbox() {
                document.getElementById("chatbox").classList.toggle("hidden");
            }

            function escapeHTML(str) {
                return str.replace(/&/g, "&amp;")
                        .replace(/</g, "&lt;")
                        .replace(/>/g, "&gt;");
            }

            function sendMessage() {
                const input = document.getElementById("userInput");
                const rawMessage = input.value.trim();
                if (!rawMessage)
                    return;

                const messages = document.getElementById("messages");

                // 💬 Tin nhắn người dùng
                const userDiv = document.createElement("div");
                userDiv.className = "flex justify-end";

                const userMsg = document.createElement("div");
                userMsg.className = "bg-indigo-100 text-indigo-800 px-4 py-2 rounded-xl max-w-[75%] whitespace-pre-line border border-indigo-300";
                userMsg.textContent = rawMessage;

                userDiv.appendChild(userMsg);
                messages.appendChild(userDiv);

                input.value = "";

                fetch(ctx + "/chatbot", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: "message=" + encodeURIComponent(rawMessage)
                })
                        .then(res => res.text())
                        .then(replyText => {
                            console.log("→ Bot trả:", replyText);

                            // 💬 Tin nhắn từ bot
                            const botDiv = document.createElement("div");
                            botDiv.className = "flex justify-start";

                            const botMsg = document.createElement("div");
                            botMsg.className = "bg-gray-200 text-gray-800 px-4 py-2 rounded-xl max-w-[75%] whitespace-pre-line border border-gray-300";
                            botMsg.textContent = replyText;

                            botDiv.appendChild(botMsg);
                            messages.appendChild(botDiv);

                            messages.scrollTop = messages.scrollHeight;
                        })
                        .catch(err => {
                            const errDiv = document.createElement("div");
                            errDiv.className = "text-red-600 px-2 py-1";
                            errDiv.textContent = "Lỗi: " + err.message;
                            messages.appendChild(errDiv);
                        });
            }
        </script>





    </body>
</html>

