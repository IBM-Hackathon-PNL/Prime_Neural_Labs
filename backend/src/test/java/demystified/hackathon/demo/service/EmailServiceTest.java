package demystified.hackathon.demo.service;

import demystified.hackathon.demo.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import jakarta.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    private MimeMessage mockMessage;

    @BeforeEach
    void setUp() {
        mockMessage = mock(MimeMessage.class);
    }

    /**
     * Test: Valid email should return true
     */
    @Test
    void shouldReturnTrueWhenEmailSentSuccessfully() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);

        // Act
        boolean result = emailService.sendPromptResponse(
            TestDataBuilder.TestConstants.VALID_EMAIL,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response content",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "response-id"
        );

        // Assert
        assertThat(result).isTrue();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    /**
     * Test: Invalid email format should return false
     */
    @Test
    void shouldReturnFalseForInvalidEmailFormat() {
        // Arrange
        String invalidEmail = "not-an-email";

        // Act
        boolean result = emailService.sendPromptResponse(
            invalidEmail,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response content",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "response-id"
        );

        // Assert
        assertThat(result).isFalse();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    /**
     * Test: Null email should return false
     */
    @Test
    void shouldReturnFalseForNullEmail() {
        // Act
        boolean result = emailService.sendPromptResponse(
            null,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response content",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "response-id"
        );

        // Assert
        assertThat(result).isFalse();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    /**
     * Test: Empty email should return false
     */
    @Test
    void shouldReturnFalseForEmptyEmail() {
        // Act
        boolean result = emailService.sendPromptResponse(
            TestDataBuilder.TestConstants.EMPTY_EMAIL,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response content",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "response-id"
        );

        // Assert
        assertThat(result).isFalse();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    /**
     * Test: Email with whitespace only should return false
     */
    @Test
    void shouldReturnFalseForWhitespaceOnlyEmail() {
        // Act
        boolean result = emailService.sendPromptResponse(
            "   ",
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response content",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "response-id"
        );

        // Assert
        assertThat(result).isFalse();
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    /**
     * Test: Multiple valid emails should all send successfully
     */
    @Test
    void shouldSendMultipleEmails() {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);

        String email1 = "user1@example.com";
        String email2 = "user2@example.com";

        // Act
        boolean result1 = emailService.sendPromptResponse(
            email1,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response 1",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "id-1"
        );

        boolean result2 = emailService.sendPromptResponse(
            email2,
            TestDataBuilder.TestConstants.TEST_PROMPT,
            "Response 2",
            TestDataBuilder.TestConstants.TEST_MODEL_ID,
            "id-2"
        );

        // Assert
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        verify(mailSender, times(2)).send(any(MimeMessage.class));
    }
}
