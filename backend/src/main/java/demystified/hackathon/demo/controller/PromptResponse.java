package demystified.hackathon.demo.controller;

public class PromptResponse {
    private String content;
    private String modelId;
    private String id;
    private long createdAt;

    public PromptResponse(String content, String modelId, String id, long createdAt) {
        this.content = content;
        this.modelId = modelId;
        this.id = id;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
