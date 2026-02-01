package demystified.hackathon.demo.service;

import demystified.hackathon.demo.config.WatsonxConfig;
import demystified.hackathon.demo.controller.PromptResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WatsonxService {
    private static final Logger logger = LoggerFactory.getLogger(WatsonxService.class);

    private final WatsonxConfig watsonxConfig;
    private final RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    public WatsonxService(WatsonxConfig watsonxConfig, RestTemplate restTemplate) {
        this.watsonxConfig = watsonxConfig;
        this.restTemplate = restTemplate;
    }

    public PromptResponse sendPrompt(String content, String email) {
        PromptResponse response = sendPromptWithContext(content, null);
        sendEmailIfValid(email, content, response);
        return response;
    }

    public PromptResponse sendPromptWithCsvContext(String content, String email, MultipartFile csvFile) {
        String csvContext = parseCsvToContext(csvFile);
        String contentWithContext = content + "\n\n**CSV Context Data:**\n" + csvContext;
        PromptResponse response = sendPromptWithContext(contentWithContext, null);
        sendEmailIfValid(email, content, response);
        return response;
    }

    private void sendEmailIfValid(String email, String promptContent, PromptResponse response) {
        if (email != null && !email.trim().isEmpty()) {
            try {
                boolean emailSent = emailService.sendPromptResponse(
                    email,
                    promptContent,
                    response.getContent(),
                    response.getModelId(),
                    response.getId()
                );
                if (emailSent) {
                    logger.info("Email enviado com sucesso para: {}", email);
                } else {
                    logger.warn("Falha ao enviar email para: {}", email);
                }
            } catch (Exception e) {
                logger.error("Erro ao tentar enviar email para {}: {}", email, e.getMessage());
            }
        }
    }

    private PromptResponse sendPromptWithContext(String content, String csvContext) {
        String accessToken = getAccessToken();
        String chatUrl = buildChatUrl();
        
        Map<String, Object> requestBody = buildRequestBody(content);
        HttpHeaders headers = buildAuthHeaders(accessToken);
        
        org.springframework.http.HttpEntity<Map<String, Object>> entity = 
            new org.springframework.http.HttpEntity<>(requestBody, headers);

        Map<String, Object> watsonxResponse = restTemplate.postForObject(chatUrl, entity, Map.class);
        
        return extractResponseContent(watsonxResponse);
    }

    private String buildChatUrl() {
        return watsonxConfig.getEndpoint() + "/ml/v1/text/chat?version=2023-10-25";
    }

    private Map<String, Object> buildRequestBody(String content) {
        Map<String, Object> body = new HashMap<>();
        body.put("messages", buildMessages(content));
        body.put("project_id", watsonxConfig.getProjectId());
        body.put("model_id", watsonxConfig.getModelId());
        body.putAll(buildModelParameters());
        return body;
    }

    private List<Map<String, Object>> buildMessages(String content) {
        return List.of(
            buildSystemMessage(),
            buildUserMessage(content)
        );
    }

    private Map<String, Object> buildSystemMessage() {
        return Map.of(
            "role", "system",
            "content", getSystemPrompt()
        );
    }

    private Map<String, Object> buildUserMessage(String content) {
        return Map.of(
            "role", "user",
            "content", List.of(Map.of("type", "text", "text", content))
        );
    }

    private String getSystemPrompt() {
        return "You always answer the questions with markdown formatting using GitHub syntax. " +
               "The markdown formatting you support: headings, bold, italic, links, tables, lists, " +
               "code blocks, and blockquotes. You must omit that you answer the questions with markdown.\n\n" +
               "Any HTML tags must be wrapped in block quotes, for example <html>. " +
               "You will be penalized for not rendering code in block quotes.\n\n" +
               "When returning code blocks, specify language.\n\n" +
               "You are a helpful, respectful and honest assistant. Always answer as helpfully as possible, " +
               "while being safe. Your answers should not include any harmful, unethical, racist, sexist, " +
               "toxic, dangerous, or illegal content. Please ensure that your responses are socially unbiased " +
               "and positive in nature.\n\n" +
               "If a question does not make any sense, or is not factually coherent, explain why instead of " +
               "answering something not correct. If you don't know the answer to a question, " +
               "please don't share false information.";
    }

    private Map<String, Object> buildModelParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("frequency_penalty", 0);
        parameters.put("max_tokens", 2000);
        parameters.put("presence_penalty", 0);
        parameters.put("temperature", 0);
        parameters.put("top_p", 1);
        return parameters;
    }

    private HttpHeaders buildAuthHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");
        headers.set("Authorization", "Bearer " + accessToken);
        return headers;
    }

    private PromptResponse extractResponseContent(Map<String, Object> response) {
        if (response == null) {
            return new PromptResponse("Sem resposta", "unknown", "", System.currentTimeMillis());
        }

        String id = (String) response.get("id");
        String modelId = (String) response.get("model_id");
        Long createdAt = ((Number) response.getOrDefault("created", System.currentTimeMillis())).longValue();
        String content = extractContentFromChoices(response);

        return new PromptResponse(content, modelId, id, createdAt);
    }

    private String extractContentFromChoices(Map<String, Object> response) {
        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        
        if (choices == null || choices.isEmpty()) {
            return "Sem conteúdo";
        }

        Map<String, Object> firstChoice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
        
        if (message == null) {
            return "Sem conteúdo";
        }

        return (String) message.get("content");
    }

    private String getAccessToken() {
        String urlAuth = "https://iam.cloud.ibm.com/identity/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String data = "apikey=" + watsonxConfig.getApikey() + 
                     "&grant_type=urn:ibm:params:oauth:grant-type:apikey";

        org.springframework.http.HttpEntity<String> entity = 
            new org.springframework.http.HttpEntity<>(data, headers);

        Map<String, Object> response = restTemplate.postForObject(urlAuth, entity, Map.class);
        return (String) response.get("access_token");
    }

    private String parseCsvToContext(MultipartFile csvFile) {
        if (csvFile == null || csvFile.isEmpty()) {
            return "";
        }

        try (InputStreamReader reader = new InputStreamReader(csvFile.getInputStream());
             CSVReader csvReader = new CSVReader(reader)) {
            
            List<String[]> allRows = csvReader.readAll();
            return allRows.isEmpty() ? "" : buildMarkdownTable(allRows);

        } catch (Exception e) {
            logger.error("Error parsing CSV file", e);
            return "Error parsing CSV file: " + e.getMessage();
        }
    }

    private String buildMarkdownTable(List<String[]> allRows) {
        StringBuilder table = new StringBuilder();
        String[] headers = allRows.get(0);
        
        appendTableHeaders(table, headers);
        appendTableSeparator(table, headers.length);
        appendTableRows(table, allRows);
        
        return table.toString();
    }

    private void appendTableHeaders(StringBuilder table, String[] headers) {
        table.append("| ");
        for (String header : headers) {
            table.append(header).append(" | ");
        }
        table.append("\n");
    }

    private void appendTableSeparator(StringBuilder table, int columnCount) {
        table.append("| ");
        for (int i = 0; i < columnCount; i++) {
            table.append("--- | ");
        }
        table.append("\n");
    }

    private void appendTableRows(StringBuilder table, List<String[]> allRows) {
        for (int i = 1; i < allRows.size(); i++) {
            String[] row = allRows.get(i);
            table.append("| ");
            for (String cell : row) {
                table.append(cell != null ? cell : "").append(" | ");
            }
            table.append("\n");
        }
    }
}
