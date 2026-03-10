package Service;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    private static final String fromEmail = "nguyenthituyettram21@gmail.com";
    private static final String password = "lxub trxz pbkn woqr"; 

    public static void sendEmail(String toEmail, String subject, String content, String filePath) throws IOException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);

            // 🧠 tạo multipart (nội dung + file)
            Multipart multipart = new MimeMultipart();

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html; charset=UTF-8");
            multipart.addBodyPart(messageBodyPart);

            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(filePath);
            multipart.addBodyPart(attachmentPart);
            message.setContent(multipart);
            
            Transport.send(message);
        } 
        catch (MessagingException e) {
            e.printStackTrace();
            throw new IOException("Lỗi khi gửi email");
        }
    } 
}