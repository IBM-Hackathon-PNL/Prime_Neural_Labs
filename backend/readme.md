# Watsonx Backend Application

## Overview

This is a Spring Boot backend application that provides REST API endpoints for integrating with IBM Watsonx AI services. The application allows users to send prompts to the Watsonx AI model and optionally provide CSV context data to enhance the responses.

## Features

- **Prompt Processing**: Send text prompts to the Watsonx AI model
- **CSV Context Support**: Enhance prompts with CSV file data for better context
- **Authentication**: Automatic token-based authentication with Watsonx API
- **Markdown Response Formatting**: AI responses are formatted with GitHub-flavored markdown
- **RESTful API**: Simple and intuitive REST endpoints for integration

## Technology Stack

- **Framework**: Spring Boot 4.0.2
- **Java Version**: 17
- **REST Client**: Spring RestClient
- **CSV Processing**: OpenCSV
- **Build Tool**: Maven
- **Container**: Docker & Docker Compose

## Project Structure

```
src/
├── main/
│   ├── java/demystified/hackathon/demo/
│   │   ├── DemoApplication.java         # Main Spring Boot application entry point
│   │   ├── config/
│   │   │   ├── WatsonxConfig.java      # Configuration properties for Watsonx
│   │   │   └── RestTemplateConfig.java # REST client configuration
│   │   ├── controller/
│   │   │   ├── PromptController.java   # REST API endpoints
│   │   │   └── PromptResponse.java     # Response model
│   │   └── service/
│   │       └── WatsonxService.java     # Business logic for Watsonx integration
│   └── resources/
│       └── application.properties       # Application configuration
└── test/
    └── java/...                        # Unit tests
```

## API Endpoints

### 1. Send Prompt

**Endpoint**: `POST /api/send-prompt`

**Description**: Sends a text prompt to the Watsonx AI model for processing.

**Request Body**:
```json
{
  "content": "Your prompt text here",
  "email": "user@example.com"
}
```

**Response**:
```json
{
  "response": "AI generated response with markdown formatting",
  "timestamp": "2026-02-01T10:30:00Z"
}
```

### 2. Send Prompt with CSV Context

**Endpoint**: `POST /api/send-prompt-with-csv`

**Description**: Sends a prompt with CSV file data as context to enhance the AI response.

**Request Parameters**:
- `prompt` (string): The prompt text
- `email` (string): User email address
- `csvFile` (file): CSV file containing context data

**Response**: Same as above

## Configuration

### Environment Variables

The application requires the following environment variables to be set:

| Variable | Description |
|----------|-------------|
| `WATSONX_APIKEY` | API key for Watsonx authentication |
| `WATSONX_PROJECT_ID` | Watsonx project ID |
| `WATSONX_MODEL_ID` | Model ID to use (e.g., `ibm-llama2-70b`) |
| `WATSONX_ENDPOINT` | Watsonx API endpoint URL |

### Configuration File

See [application.properties](src/main/resources/application.properties) for property definitions.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerized deployment)

### Installation & Running

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd Backend_Watsonx
   ```

2. **Set environment variables**:
   ```bash
   export WATSONX_APIKEY=<your-api-key>
   export WATSONX_PROJECT_ID=<your-project-id>
   export WATSONX_MODEL_ID=<your-model-id>
   export WATSONX_ENDPOINT=<your-endpoint>
   ```

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

   The application will start on `http://localhost:8080`

### Using Docker

1. **Build the Docker image**:
   ```bash
   docker build -t watsonx-backend .
   ```

2. **Run with Docker Compose**:
   ```bash
   docker-compose up
   ```

## Core Components

### WatsonxConfig
Handles configuration management for Watsonx API credentials and endpoints. Properties are loaded from environment variables during application startup.

### WatsonxService
Main business logic service that:
- Authenticates with Watsonx API
- Processes prompts with optional CSV context
- Handles API communication
- Manages token generation and refresh

### PromptController
REST controller that exposes two main endpoints:
- Simple prompt submission
- Prompt submission with CSV context support

### RestTemplateConfig
Configures the Spring RestTemplate bean for making HTTP requests to the Watsonx API.

## Response Format

All AI responses are formatted using GitHub-flavored markdown, including:
- Headings
- Bold and italic text
- Links
- Tables
- Lists
- Code blocks (with language specification)
- Blockquotes
- HTML tags (wrapped in blockquotes)

## Building

To build the project, run:
```bash
mvn clean install
```

To skip tests during build:
```bash
mvn clean install -DskipTests
```

## Testing

Run the unit tests with:
```bash
mvn test
```

## Troubleshooting

### Authentication Errors
- Verify that all required environment variables are properly set
- Check that the API key is valid and has appropriate permissions
- Ensure the project ID and model ID match your Watsonx configuration

### Connection Issues
- Verify that the Watsonx endpoint URL is correct and accessible
- Check network connectivity to the Watsonx API
- Review logs for detailed error messages

## Development Notes

- The application uses Spring Boot 4.0.2 which provides out-of-the-box support for RestClient
- CSV parsing is handled using the OpenCSV library for robust file processing
- Token management is handled automatically by the service
- All responses are structured as PromptResponse objects for consistency

## Future Enhancements

- Add support for multiple AI models
- Implement response caching
- Add request/response logging
- Support for additional file formats (JSON, XML, etc.)
- Rate limiting and usage analytics

## Support

For issues or questions regarding this application, please contact the development team or open an issue in the repository.

---

**Application Name**: Watsonx Backend Demo  
**Version**: 0.0.1-SNAPSHOT  
**Last Updated**: February 2026
