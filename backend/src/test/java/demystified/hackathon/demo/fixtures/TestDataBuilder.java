package demystified.hackathon.demo.fixtures;

import demystified.hackathon.demo.controller.PromptResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to build test data and mock objects for unit tests.
 */
public class TestDataBuilder {

    /**
     * Creates a mock Watsonx API response
     */
    public static Map<String, Object> createMockWatsonxResponse() {
        return createMockWatsonxResponse("Mocked response content");
    }

    /**
     * Creates a mock Watsonx API response with custom content
     */
    public static Map<String, Object> createMockWatsonxResponse(String content) {
        Map<String, Object> response = new HashMap<>();
        response.put("id", "response-123");
        response.put("model_id", "test-model");
        response.put("created", System.currentTimeMillis());

        Map<String, Object> message = new HashMap<>();
        message.put("content", content);

        Map<String, Object> choice = new HashMap<>();
        choice.put("message", message);

        response.put("choices", List.of(choice));
        return response;
    }

    /**
     * Creates a mock IAM token response
     */
    public static Map<String, Object> createMockIamTokenResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put("access_token", "mock-access-token-12345");
        response.put("expires_in", 3600);
        response.put("token_type", "Bearer");
        return response;
    }

    /**
     * Creates a test PromptResponse object
     */
    public static PromptResponse createTestPromptResponse() {
        return new PromptResponse(
            "Test response content",
            "test-model-id",
            "test-response-id",
            System.currentTimeMillis()
        );
    }

    /**
     * Creates a test PromptResponse with custom content
     */
    public static PromptResponse createTestPromptResponse(String content) {
        return new PromptResponse(
            content,
            "test-model-id",
            "test-response-id",
            System.currentTimeMillis()
        );
    }

    /**
     * Creates CSV test data as string
     */
    public static String createTestCsvData() {
        return "Name,Email,Status\n" +
               "John Doe,john@example.com,Active\n" +
               "Jane Smith,jane@example.com,Inactive\n";
    }

    /**
     * Creates mock CSV response parsed as markdown table
     */
    public static String createExpectedCsvMarkdownTable() {
        return "| Name | Email | Status | \n" +
               "| --- | --- | --- | \n" +
               "| John Doe | john@example.com | Active | \n" +
               "| Jane Smith | jane@example.com | Inactive | \n";
    }

    /**
     * Common test constants
     */
    public static class TestConstants {
        public static final String VALID_EMAIL = "test@example.com";
        public static final String INVALID_EMAIL = "invalid-email";
        public static final String EMPTY_EMAIL = "";
        public static final String TEST_PROMPT = "What is Java?";
        public static final String TEST_API_KEY = "test-api-key-12345";
        public static final String TEST_PROJECT_ID = "test-project-id";
        public static final String TEST_MODEL_ID = "test-model-id";
        public static final String TEST_ENDPOINT = "https://api.test.com";
    }
}
