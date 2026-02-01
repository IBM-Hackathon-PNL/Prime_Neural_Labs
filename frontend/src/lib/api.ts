/**
 * API Service - Frontend
 * Comunicação com backend Java para envio de prompts ao Watsonx
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
 * Envia um prompt para o backend ser processado pela IA Watsonx
 * @param content - O texto/prompt a ser enviado
 * @param email - Email do usuário para notificação
 * @returns Resposta da IA Watsonx
 * @throws Error com mensagem amigável em caso de erro
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

    // Tratamento de erros HTTP
    if (!response.ok) {
      let errorMessage = "Erro ao enviar mensagem";

      if (response.status === 400) {
        errorMessage = "Email inválido ou prompt vazio";
      } else if (response.status === 500) {
        errorMessage = "Erro no servidor - tente novamente mais tarde";
      } else if (response.status === 0) {
        errorMessage = "Não foi possível conectar ao servidor";
      }

      throw new Error(errorMessage);
    }

    const data: PromptResponse = await response.json();
    return data;
  } catch (error) {
    if (error instanceof Error) {
      throw error;
    }
    throw new Error("Erro desconhecido ao conectar com o servidor");
  }
}

/**
 * Valida um endereço de email
 * @param email - Email a ser validado
 * @returns true se válido, false caso contrário
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

/**
 * Valida um prompt/conteúdo
 * @param content - Conteúdo a ser validado
 * @returns true se válido, false caso contrário
 */
export function isValidPrompt(content: string): boolean {
  return content.trim().length > 0 && content.trim().length <= 5000;
}
