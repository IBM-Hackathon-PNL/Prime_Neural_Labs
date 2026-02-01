package demystified.hackathon.demo.controller;

import demystified.hackathon.demo.service.WatsonxService;
import demystified.hackathon.demo.fixtures.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PromptControllerTest {

    @Mock
    private WatsonxService watsonxService;

    @InjectMocks
    private PromptController promptController;

    @BeforeEach
    void setUp() {
        // Setup common mock behavior
    }

    /**
     * Test: sendPrompt should delegate to service and return response
     */
    @Test
    void shouldSendPromptAndReturnResponse() {
        // Arrange
        PromptResponse expectedResponse = TestDataBuilder.createTestPromptResponse(
            "This is a test response"
        );
        when(watsonxService.sendPrompt(anyString(), anyString()))
            .thenReturn(expectedResponse);

        PromptController.PromptRequest request = new PromptController.PromptRequest();
        request.setContent("What is Spring Boot?");
        request.setEmail("test@example.com");

        // Act
        PromptResponse response = promptController.sendPrompt(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("This is a test response");
        assertThat(response.getModelId()).isEqualTo("test-model-id");
    }

    /**
     * Test: sendPrompt without email should work
     */
    @Test
    void shouldSendPromptWithoutEmailSuccessfully() {
        // Arrange
        PromptResponse expectedResponse = TestDataBuilder.createTestPromptResponse(
            "Response without email"
        );
        when(watsonxService.sendPrompt(TestDataBuilder.TestConstants.TEST_PROMPT, null))
            .thenReturn(expectedResponse);

        PromptController.PromptRequest request = new PromptController.PromptRequest();
        request.setContent(TestDataBuilder.TestConstants.TEST_PROMPT);
        request.setEmail(null);

        // Act
        PromptResponse response = promptController.sendPrompt(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("Response without email");
    }

    /**
     * Test: sendPrompt with empty content should still process
     */
    @Test
    void shouldSendPromptWithEmptyContent() {
        // Arrange
        PromptResponse expectedResponse = TestDataBuilder.createTestPromptResponse();
        when(watsonxService.sendPrompt(anyString(), anyString()))
            .thenReturn(expectedResponse);

        PromptController.PromptRequest request = new PromptController.PromptRequest();
        request.setContent("");
        request.setEmail("test@example.com");

        // Act
        PromptResponse response = promptController.sendPrompt(request);

        // Assert
        assertThat(response).isNotNull();
    }

    /**
     * Test: sendPrompt with special characters in content
     */
    @Test
    void shouldSendPromptWithSpecialCharacters() {
        // Arrange
        PromptResponse expectedResponse = TestDataBuilder.createTestPromptResponse(
            "Response with special chars"
        );
        when(watsonxService.sendPrompt(anyString(), anyString()))
            .thenReturn(expectedResponse);

        PromptController.PromptRequest request = new PromptController.PromptRequest();
        request.setContent("Test with special chars: <>&\"");
        request.setEmail("test@example.com");

        // Act
        PromptResponse response = promptController.sendPrompt(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("Response with special chars");
    }

    /**
     * Test: sendPrompt returns correct response structure
     */
    @Test
    void shouldReturnCompleteResponseStructure() {
        // Arrange
        long expectedCreatedAt = System.currentTimeMillis();
        PromptResponse expectedResponse = new PromptResponse(
            "Complete response",
            "model-v1",
            "unique-response-id",
            expectedCreatedAt
        );
        when(watsonxService.sendPrompt(anyString(), anyString()))
            .thenReturn(expectedResponse);

        PromptController.PromptRequest request = new PromptController.PromptRequest();
        request.setContent("Test");
        request.setEmail("test@example.com");

        // Act
        PromptResponse response = promptController.sendPrompt(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("Complete response");
        assertThat(response.getModelId()).isEqualTo("model-v1");
        assertThat(response.getId()).isEqualTo("unique-response-id");
        assertThat(response.getCreatedAt()).isEqualTo(expectedCreatedAt);
    }
}
