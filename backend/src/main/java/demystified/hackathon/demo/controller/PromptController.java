package demystified.hackathon.demo.controller;

import demystified.hackathon.demo.service.WatsonxService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class PromptController {
    private final WatsonxService watsonxService;

    public PromptController(WatsonxService watsonxService) {
        this.watsonxService = watsonxService;
    }

    @PostMapping("/send-prompt")
    public PromptResponse sendPrompt(@RequestBody PromptRequest request) {
        return watsonxService.sendPrompt(request.getContent(), request.getEmail());
    }

    @PostMapping("/send-prompt-with-csv")
    public PromptResponse sendPromptWithCsv(@RequestParam("prompt") String prompt, 
                                            @RequestParam("email") String email,
                                            @RequestParam("csvFile") MultipartFile csvFile) {
        return watsonxService.sendPromptWithCsvContext(prompt, email, csvFile);
    }

    public static class PromptRequest {
        private String content;
        private String email;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
