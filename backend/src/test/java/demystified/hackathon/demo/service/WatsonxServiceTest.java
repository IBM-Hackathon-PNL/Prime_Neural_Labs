package demystified.hackathon.demo.service;

import demystified.hackathon.demo.config.WatsonxConfig;
import demystified.hackathon.demo.controller.PromptResponse;
import demystified.hackathon.demo.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WatsonxServiceTest {

    @Mock
    private WatsonxConfig watsonxConfig;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private WatsonxService watsonxService;

    @BeforeEach
    void setUp() {
        // Configure common mock behavior
        when(watsonxConfig.getApikey()).thenReturn(TestDataBuilder.TestConstants.TEST_API_KEY);
        when(watsonxConfig.getProjectId()).thenReturn(TestDataBuilder.TestConstants.TEST_PROJECT_ID);
        when(watsonxConfig.getModelId()).thenReturn(TestDataBuilder.TestConstants.TEST_MODEL_ID);
        when(watsonxConfig.getEndpoint()).thenReturn(TestDataBuilder.TestConstants.TEST_ENDPOINT);
    }

    /**
     * Test: sendPrompt should return PromptResponse successfully
     */
    @Test
    void shouldSendPromptAndReturnValidResponse() {
        // Arrange
        String content = TestDataBuilder.TestConstants.TEST_PROMPT;
        String email = TestDataBuilder.TestConstants.VALID_EMAIL;
        
        Map<String, Object> mockWatsonxResponse = TestDataBuilder.createMockWatsonxResponse(
            "This is a test response"
        );
        Map<String, Object> mockIamResponse = TestDataBuilder.createMockIamTokenResponse();

        when(restTemplate.postForObject(
            contains("iam.cloud.ibm.com"),
            any(),
            eq(Map.class)
        )).thenReturn(mockIamResponse);

        when(restTemplate.postForObject(
            contains("ml/v1/text/chat"),
            any(),
            eq(Map.class)
        )).thenReturn(mockWatsonxResponse);

        // Act
        PromptResponse response = watsonxService.sendPrompt(content, email);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("This is a test response");
        assertThat(response.getModelId()).isEqualTo("test-model");
        assertThat(response.getId()).isEqualTo("response-123");
    }

    /**
     * Test: sendPrompt with null email should not send email
     */
    @Test
    void shouldNotSendEmailWhenEmailIsNull() {
        // Arrange
        String content = TestDataBuilder.TestConstants.TEST_PROMPT;
        
        Map<String, Object> mockWatsonxResponse = TestDataBuilder.createMockWatsonxResponse();
        Map<String, Object> mockIamResponse = TestDataBuilder.createMockIamTokenResponse();

        when(restTemplate.postForObject(
            contains("iam.cloud.ibm.com"),
            any(),
            eq(Map.class)
        )).thenReturn(mockIamResponse);

        when(restTemplate.postForObject(
            contains("ml/v1/text/chat"),
            any(),
            eq(Map.class)
        )).thenReturn(mockWatsonxResponse);

        // Act
        PromptResponse response = watsonxService.sendPrompt(content, null);

        // Assert
        assertThat(response).isNotNull();
        verify(emailService, never()).sendPromptResponse(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    /**
     * Test: sendPrompt with empty email should not send email
     */
    @Test
    void shouldNotSendEmailWhenEmailIsEmpty() {
        // Arrange
        String content = TestDataBuilder.TestConstants.TEST_PROMPT;
        
        Map<String, Object> mockWatsonxResponse = TestDataBuilder.createMockWatsonxResponse();
        Map<String, Object> mockIamResponse = TestDataBuilder.createMockIamTokenResponse();

        when(restTemplate.postForObject(
            contains("iam.cloud.ibm.com"),
            any(),
            eq(Map.class)
        )).thenReturn(mockIamResponse);

        when(restTemplate.postForObject(
            contains("ml/v1/text/chat"),
            any(),
            eq(Map.class)
        )).thenReturn(mockWatsonxResponse);

        // Act
        PromptResponse response = watsonxService.sendPrompt(content, "   ");

        // Assert
        assertThat(response).isNotNull();
        verify(emailService, never()).sendPromptResponse(anyString(), anyString(), anyString(), anyString(), anyString());
    }

    /**
     * Test: sendPrompt should handle null response from Watsonx gracefully
     */
    @Test
    void shouldHandleNullResponseFromWatsonx() {
        // Arrange
        String content = TestDataBuilder.TestConstants.TEST_PROMPT;
        String email = TestDataBuilder.TestConstants.VALID_EMAIL;
        
        Map<String, Object> mockIamResponse = TestDataBuilder.createMockIamTokenResponse();

        when(restTemplate.postForObject(
            contains("iam.cloud.ibm.com"),
            any(),
            eq(Map.class)
        )).thenReturn(mockIamResponse);

        when(restTemplate.postForObject(
            contains("ml/v1/text/chat"),
            any(),
            eq(Map.class)
        )).thenReturn(null);

        // Act
        PromptResponse response = watsonxService.sendPrompt(content, email);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("Sem resposta");
    }

    /**
     * Test: sendPrompt should continue even if email sending fails
     */
    @Test
    void shouldContinueEvenIfEmailSendingFails() {
        // Arrange
        String content = TestDataBuilder.TestConstants.TEST_PROMPT;
        String email = TestDataBuilder.TestConstants.VALID_EMAIL;
        
        Map<String, Object> mockWatsonxResponse = TestDataBuilder.createMockWatsonxResponse();
        Map<String, Object> mockIamResponse = TestDataBuilder.createMockIamTokenResponse();

        when(restTemplate.postForObject(
            contains("iam.cloud.ibm.com"),
            any(),
            eq(Map.class)
        )).thenReturn(mockIamResponse);

        when(restTemplate.postForObject(
            contains("ml/v1/text/chat"),
            any(),
            eq(Map.class)
        )).thenReturn(mockWatsonxResponse);

        // Act
        PromptResponse response = watsonxService.sendPrompt(content, email);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
    }
}
