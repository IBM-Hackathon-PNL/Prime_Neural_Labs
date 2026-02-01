/**
 * API Service - Frontend
 * Communication with Java backend for sending prompts to Watsonx
 */

interface PromptResponse {
  content: string;
  modelId: string;
  id: string;
  createdAt: number;
}

interface PromptRequest {
  content: string;
  email: string;
}

/**
 * Sends a prompt to the backend to be processed by Watsonx AI
 * @param content - The text/prompt to be sent
 * @param email - User's email for notification
 * @returns Response from Watsonx AI
 * @throws Error with user-friendly message in case of failure
 */
export async function sendPrompt(
  content: string,
  email: string
): Promise<PromptResponse> {
  try {
    const payload: PromptRequest = { content, email };

    const response = await fetch("/api/send-prompt", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(payload),
    });

    // HTTP error handling
    if (!response.ok) {
      let errorMessage = "Failed to send message";

      if (response.status === 400) {
        errorMessage = "Invalid email or empty prompt";
      } else if (response.status === 500) {
        errorMessage = "Server error - please try again later";
      } else if (response.status === 0) {
        errorMessage = "Could not connect to server";
      }

      throw new Error(errorMessage);
    }

    const data: PromptResponse = await response.json();
    return data;
  } catch (error) {
    if (error instanceof Error) {
      throw error;
    }
    throw new Error("Unknown error connecting to server");
  }
}

/**
 * Validates an email address
 * @param email - Email to be validated
 * @returns true if valid, false otherwise
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Validates prompt/content
 * @param content - Content to be validated
 * @returns true if valid, false otherwise
 */
export function isValidPrompt(content: string): boolean {
  return content.trim().length > 0 && content.trim().length <= 5000;
}
