# Prime Neural Labs

A platform that enables anyone to create, configure, and deploy automated AI agents through natural language prompts — without requiring deep technical expertise.

## 🎯 Overview

Prime Neural Labs solves the accessibility gap in AI agent creation. Describe your automation needs in natural language and have a fully functional AI agent deployed within minutes.

**Status:** PoC | **Stack:** React + TypeScript | Java + Spring Boot | IBM Watsonx | Docker

---

## 🚀 Key Features

- **Natural Language Agent Creation** — Describe what you need; we build it
- **Guided Configuration Workflow** — Step-by-step setup with intelligent field validation
- **Standardized JSON-Based Templates** — Ensures consistency, governance, and reusability
- **Containerized Deployment** — Isolated, scalable agent execution via Docker
- **Enterprise-Ready Governance** — Built-in controls for security, auditability, and transparency
- **CSV Context Support** — Enhance AI responses with custom data context
- **GitHub-Flavored Markdown Responses** — Structured, readable AI outputs

---

## 💡 The Problem

Creating AI automation is still inaccessible for most users and organizations:

- **Developers** struggle with repetitive tasks, integration challenges, and lack of AI expertise
- **Enterprises** face governance, security, and control concerns with autonomous agents
- **Non-technical users** cannot leverage AI for automation without hiring specialists

**Result:** Despite being promising, fully autonomous AI agent creation remains technical, expensive, and out of reach for many.

---

## ✅ The Solution

Prime Neural Labs abstracts complexity through a **master prompt + template-driven architecture**:

1. **User sends a prompt** → Describes automation needs in natural language
2. **Backend augments prompt** → Adds standardized instructions for consistency
3. **Watsonx returns structured JSON** → Defines agent type, parameters, and required fields
4. **Backend parametrizes template** → Fills template with AI-generated configuration
5. **Frontend collects missing data** → Guides user through required fields (if any)
6. **Backend deploys container** → Spins up isolated Docker instance with final agent
7. **User receives access info** → Ready to use immediately

**Result:** From prompt to production in minutes, with zero-to-minimal technical debt.

---

## 🎯 Who Benefits

✅ **Startups & SMEs** — Automate workflows without large dev teams  
✅ **Product Teams** — Rapid prototyping and deployment of agent-based features  
✅ **Non-Technical Users** — Create custom AI solutions using natural language  
✅ **Operations & Consultants** — Automate common business scenarios at scale  

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| **Frontend** | React + TypeScript |
| **Backend** | Java + Spring Boot 4.0.2 |
| **AI Engine** | IBM Watsonx (13B model) |
| **Templates** | Python |
| **Deployment** | Docker & Docker Compose |
| **Build** | Maven |
| **HTTP Client** | Spring RestClient |
| **CSV Processing** | OpenCSV |

---

## 📋 Project Structure

```
Prime_Neural_Labs/
├── frontend/                          # React + TypeScript UI
│   ├── src/
│   │   ├── components/
│   │   │   ├── AgentBuilder/         # Agent creation workflow
│   │   │   │   ├── AgentBuilderLayout.tsx
│   │   │   │   ├── BehaviorForm.tsx
│   │   │   │   ├── ChatPreview.tsx
│   │   │   │   ├── Header.tsx
│   │   │   │   └── Sidebar.tsx
│   │   │   ├── ChatPreview.tsx       # Real-time agent testing
│   │   │   ├── DocsModal.tsx         # Documentation & help
│   │   │   ├── NavLink.tsx           # Navigation component
│   │   │   └── ui/                   # Shadcn UI components
│   │   ├── lib/
│   │   │   ├── api.ts                # Backend communication
│   │   │   └── utils.ts              # Utility functions
│   │   ├── pages/
│   │   │   ├── Index.tsx             # Main page
│   │   │   └── NotFound.tsx          # 404 page
│   │   ├── hooks/
│   │   │   └── use-toast.ts          # Toast notifications
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── package.json
│   ├── vite.config.ts
│   └── tsconfig.json
│
├── backend/                           # Spring Boot REST API
│   ├── src/main/java/demystified/hackathon/demo/
│   │   ├── DemoApplication.java      # Spring Boot entry point
│   │   ├── config/
│   │   │   ├── WatsonxConfig.java    # Watsonx credentials & endpoints
│   │   │   └── RestTemplateConfig.java
│   │   ├── controller/
│   │   │   ├── PromptController.java # REST endpoints
│   │   │   └── PromptResponse.java   # Response model
│   │   └── service/
│   │       ├── WatsonxService.java   # Core business logic
│   │       └── EmailService.java     # Notification service
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   ├── endpoints.md                  # API documentation
│   └── readme.md                     # Backend-specific docs
│
└── README.md                          # This file
```

---

## 🔌 API Endpoints

### POST `/api/send-prompt`

Send a user prompt to generate an AI agent configuration.

**Request:**
```json
{
  "content": "Create an agent that reads CSV sales data daily and posts summaries to Slack",
  "email": "user@example.com"
}
```

**Response:**
```json
{
  "content": "Agent configuration with markdown formatting...",
  "modelId": "ibm-watsonx-13b",
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": 1738420800000
}
```

### POST `/api/send-prompt-with-csv`

Send a prompt with CSV context data for enhanced AI responses.

**Parameters:**
- `prompt` (string) — The user's automation request
- `email` (string) — User email for notifications
- `csvFile` (file) — CSV file with context data

**Response:** Same as above

---

## 🔄 Workflow Example

**User Request:**
```
"Create a daily agent that reads our sales CSV, generates a summary report, 
and posts it to our Slack channel #reports"
```

**System Response (JSON):**
```json
{
  "agentType": "csvSummary",
  "parameters": {
    "fileUrl": "s3://bucket/sales.csv",
    "schedule": "daily",
    "outputChannel": "slack",
    "slackWebhook": ""
  },
  "missingFields": ["slackWebhook"]
}
```

**User Provides:**
```
Slack Webhook: "https://hooks.slack.com/services/XXX/YYY/ZZZ"
```

**System Deploys:**
- Backend validates all parameters
- Python agent template is parametrized
- Docker container is created and started
- User receives: `agent-id`, `endpoint`, `usage instructions`

---

## ⚙️ Getting Started

### Prerequisites

- **Java 17+** (for backend)
- **Maven 3.6+** (for backend)
- **Node.js 18+** (for frontend)
- **Docker** (for agent deployment)
- **Watsonx credentials** (API key, project ID, model ID, endpoint)

### Backend Setup

```bash
# Navigate to backend directory
cd backend

# Set environment variables
export WATSONX_APIKEY=<your-api-key>
export WATSONX_PROJECT_ID=<your-project-id>
export WATSONX_MODEL_ID=<your-model-id>
export WATSONX_ENDPOINT=<your-endpoint>

# Build and run
mvn clean install
mvn spring-boot:run
```

Backend will start on `http://localhost:8080`

### Frontend Setup

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will start on `http://localhost:5173`

---

## 🎨 Core Components

### Frontend

- **AgentBuilder** — Multi-step wizard for creating agents
- **BehaviorForm** — Configure agent instructions and behavior
- **ChatPreview** — Test agent responses in real-time
- **DocsModal** — Built-in documentation and help

### Backend

- **PromptController** — Exposes REST endpoints for prompt submission
- **WatsonxService** — Orchestrates API calls, token management, and response processing
- **WatsonxConfig** — Manages Watsonx credentials and endpoints (environment-based)
- **EmailService** — Sends notifications to users upon completion
- **RestTemplateConfig** — Configures HTTP client for Watsonx API

---

## 🔐 Response Format & Governance

All AI responses are formatted using **GitHub-flavored Markdown**:

- Headings, bold, italic text
- Links and tables
- Lists and code blocks (with language specification)
- Blockquotes and HTML tags (wrapped safely)

This ensures:
- **Consistency** across all agent responses
- **Auditability** — Structured, readable output
- **Safety** — HTML properly escaped and contextualized

---

## 📊 Comparison: Traditional vs. Prime Neural Labs

| Challenge | Traditional Approach | Prime Neural Labs |
|-----------|---------------------|-------------------|
| **Time to Deploy** | Weeks (develop + test) | Minutes (prompt → agent) |
| **Technical Barrier** | High (requires AI/DevOps expertise) | Low (natural language) |
| **Governance** | Manual, ad-hoc | Built-in, standardized |
| **Scalability** | Limited by team size | Container-based, unlimited |
| **Cost** | High specialist salaries | Low (platform-driven) |

---

## 🧪 Development & Testing

### Run Backend Tests
```bash
cd backend
mvn test
```

### Run Frontend Tests
```bash
cd frontend
npm run test
```

### Build for Production

**Backend:**
```bash
cd backend
mvn clean install -DskipTests
```

**Frontend:**
```bash
cd frontend
npm run build
```

---

## 🐛 Troubleshooting

### Authentication Errors
- Verify all environment variables are set correctly
- Check API key validity and permissions
- Confirm project ID and model ID match your Watsonx configuration

### Connection Issues
- Verify Watsonx endpoint URL is accessible
- Check network connectivity
- Review application logs for detailed error messages

### CSV Parsing Errors
- Ensure CSV file is properly formatted (UTF-8 encoding recommended)
- Check for special characters or unusual delimiters
- Verify file size is within acceptable limits

---

## 🔮 Future Enhancements

- Support for multiple AI models (Claude, GPT-4, local LLMs)
- Response caching for improved performance
- Comprehensive request/response logging and audit trails
- Additional file format support (JSON, XML, Excel)
- Rate limiting and usage analytics
- Custom template library management
- Team collaboration features
- Advanced monitoring and observability

---

## 📝 Key Libraries & Dependencies

- **Spring Boot 4.0.2** — Modern, auto-configured REST framework
- **RestClient** — Simplified HTTP communication with Watsonx
- **OpenCSV** — Robust CSV parsing and processing
- **React Hooks** — Functional component state management
- **TypeScript** — Type-safe frontend development
- **Docker** — Container-based agent isolation and deployment
- **Vite** — Lightning-fast frontend build tool

---

## 📖 Documentation

- [Backend Documentation](./backend/readme.md)
- [API Endpoints](./backend/endpoints.md)

---

**Prime Neural Labs** — *Democratizing AI agent creation for everyone.*
