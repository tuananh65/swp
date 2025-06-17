package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;

    private static final String USERNAME = "globalhelcurt14092005@gmail.com";  // ✅ Email gửi đi
    private static final String PASSWORD = "phje juhq pksr xndj";               // ✅ App password

    public static boolean send(String to, String subject, String content) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // ✅ Dùng MimeMessage (khác Message)
            MimeMessage message = new MimeMessage(session);

            // ✅ Đặt người gửi có tên hiển thị + charset UTF-8
            message.setFrom(new InternetAddress(USERNAME, "Hệ thống đào tạo kỹ năng mềm", "UTF-8"));

            // ✅ Đặt người nhận
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // ✅ Tiêu đề & nội dung đều UTF-8
            message.setSubject(subject, "UTF-8");
            message.setText(content, "UTF-8");

            Transport.send(message);
            System.out.println("[EmailSender] ✅ Email đã gửi tới: " + to);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[EmailSender] ❌ Gửi email thất bại: " + e.getMessage());
            return false;
        }
    }
}


