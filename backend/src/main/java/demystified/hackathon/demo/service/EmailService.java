package demystified.hackathon.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private static final String EMAIL_FROM = "watsonx-bot@noreply.com";
    private static final String EMAIL_SUBJECT = "Watsonx Response - Your Prompt";
    private static final String CHARSET = "UTF-8";
    private static final String DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
    
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendPromptResponse(String userEmail, String promptContent, String responseContent, 
                                     String modelId, String responseId) {
        if (!isValidEmail(userEmail)) {
            logger.warn("Invalid email provided: {}", userEmail);
            return false;
        }

        try {
            MimeMessage message = createEmailMessage(userEmail, promptContent, responseContent, modelId, responseId);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", userEmail);
            return true;
        } catch (MessagingException e) {
            logger.error("Error sending email to {}", userEmail, e);
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error sending email to {}", userEmail, e);
            return false;
        }
    }

    private MimeMessage createEmailMessage(String userEmail, String promptContent, String responseContent,
                                          String modelId, String responseId) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, CHARSET);

        helper.setTo(userEmail);
        helper.setFrom(EMAIL_FROM);
        helper.setSubject(EMAIL_SUBJECT);

        String htmlContent = buildHtmlEmail(promptContent, responseContent, modelId, responseId);
        helper.setText(htmlContent, true);

        return message;
    }

    /**
     * Builds the HTML content for the email with the formatted response
     */
    private String buildHtmlEmail(String promptContent, String responseContent, String modelId, String responseId) {
        String currentDateTime = getCurrentDateTime();
        
        return "<html>" +
            buildEmailHead() +
            buildEmailBody(promptContent, responseContent, modelId, responseId, currentDateTime) +
            "</html>";
    }

    private String buildEmailHead() {
        return "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "<style>" +
            getEmailStyles() +
            "</style>" +
            "</head>";
    }

    private String buildEmailBody(String promptContent, String responseContent, String modelId, 
                                 String responseId, String currentDateTime) {
        return "<body>" +
            "<div class=\"container\">" +
            buildEmailHeader() +
            buildEmailContent(promptContent, responseContent, modelId, responseId, currentDateTime) +
            buildEmailFooter() +
            "</div>" +
            "</body>";
    }

    private String buildEmailHeader() {
        return "<div class=\"header\">" +
            "<h1>Watsonx Response</h1>" +
            "</div>";
    }

    private String buildEmailContent(String promptContent, String responseContent, String modelId,
                                    String responseId, String currentDateTime) {
        return "<div class=\"content\">" +
            buildPromptSection(promptContent) +
            buildResponseSection(responseContent, modelId, responseId, currentDateTime) +
            "</div>";
    }

    private String buildPromptSection(String promptContent) {
        return "<div class=\"section\">" +
            "<div class=\"section-title\">Your Prompt</div>" +
            "<div class=\"prompt-box\">" + htmlEscape(promptContent) + "</div>" +
            "</div>";
    }

    private String buildResponseSection(String responseContent, String modelId, String responseId, String currentDateTime) {
        return "<div class=\"section\">" +
            "<div class=\"section-title\">Response</div>" +
            "<div class=\"response-box\">" + responseContent + "</div>" +
            buildMetadata(modelId, responseId, currentDateTime) +
            "</div>";
    }

    private String buildMetadata(String modelId, String responseId, String currentDateTime) {
        return "<div class=\"metadata\">" +
            "<strong>Model:</strong> " + htmlEscape(modelId) + "<br>" +
            "<strong>Response ID:</strong> " + htmlEscape(responseId) + "<br>" +
            "<strong>Date:</strong> " + currentDateTime +
            "</div>";
    }

    private String buildEmailFooter() {
        return "<div class=\"footer\">" +
            "<p>This is an automated response from the Watsonx system. Do not reply to this email.</p>" +
            "</div>";
    }

    private String getEmailStyles() {
        return "body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif; line-height: 1.6; color: #333; background-color: #f5f5f5; padding: 20px; }" +
            ".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); overflow: hidden; }" +
            ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 30px; text-align: center; }" +
            ".header h1 { margin: 0; font-size: 24px; }" +
            ".content { padding: 30px; }" +
            ".section { margin-bottom: 25px; }" +
            ".section-title { font-size: 14px; font-weight: bold; color: #667eea; text-transform: uppercase; margin-bottom: 10px; border-bottom: 2px solid #667eea; padding-bottom: 8px; }" +
            ".prompt-box { background-color: #f9f9f9; border-left: 4px solid #667eea; padding: 15px; border-radius: 4px; font-style: italic; color: #555; }" +
            ".response-box { background-color: #fafafa; border-left: 4px solid #764ba2; padding: 15px; border-radius: 4px; }" +
            ".response-box code { background-color: #f0f0f0; padding: 2px 6px; border-radius: 3px; font-family: 'Monaco', 'Menlo', monospace; font-size: 13px; }" +
            ".response-box pre { background-color: #2d2d2d; color: #f8f8f2; padding: 15px; border-radius: 4px; overflow-x: auto; font-family: 'Monaco', 'Menlo', monospace; font-size: 12px; }" +
            ".footer { background-color: #f5f5f5; padding: 20px; text-align: center; border-top: 1px solid #e0e0e0; font-size: 12px; color: #666; }" +
            ".metadata { font-size: 12px; color: #999; margin-top: 10px; padding-top: 10px; border-top: 1px solid #e0e0e0; }";
    }

    private String getCurrentDateTime() {
        return java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern(DATE_PATTERN));
    }

    /**
     * Validates if the email is in a valid format
     */
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches(EMAIL_REGEX);
    }

    /**
     * Escapes special HTML characters to prevent injection
     */
    private String htmlEscape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#39;");
    }
}
