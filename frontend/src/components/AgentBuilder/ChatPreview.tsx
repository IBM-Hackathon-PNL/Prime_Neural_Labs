import { useState } from "react";
import { Bot, Send, Link2, Loader2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { cn } from "@/lib/utils";
import { useToast } from "@/hooks/use-toast";
import { sendPrompt, isValidEmail, isValidPrompt } from "@/lib/api";

interface Message {
  id: string;
  content: string;
  sender: "user" | "agent";
  timestamp: string;
  hasLink?: { text: string; url: string };
}

const initialMessages: Message[] = [
  {
    id: "1",
    content: "Where can I find my invoice for last month?",
    sender: "user",
    timestamp: "Today, 10:23 AM",
  },
  {
    id: "2",
    content: `You can find all your invoices in the Billing section of your dashboard.

1. Go to Settings
2. Click on "Billing & Plans"
3. Scroll down to "Invoice History"`,
    sender: "agent",
    timestamp: "Just now",
    hasLink: { text: "Go to Billing Settings", url: "#" },
  },
];

export const ChatPreview = () => {
  const [messages, setMessages] = useState<Message[]>(initialMessages);
  const [inputValue, setInputValue] = useState("");
  const [emailInput, setEmailInput] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { toast } = useToast();

  const handleSend = async () => {
    // Validação
    if (!inputValue.trim()) {
      toast({
        title: "Erro",
        description: "Por favor, digite uma mensagem",
        variant: "destructive",
      });
      return;
    }

    if (!isValidPrompt(inputValue)) {
      toast({
        title: "Erro",
        description: "A mensagem deve ter entre 1 e 5000 caracteres",
        variant: "destructive",
      });
      return;
    }

    if (!emailInput.trim()) {
      toast({
        title: "Erro",
        description: "Por favor, informe seu email",
        variant: "destructive",
      });
      return;
    }

    if (!isValidEmail(emailInput)) {
      toast({
        title: "Erro",
        description: "Por favor, informe um email válido",
        variant: "destructive",
      });
      return;
    }

    // Adiciona mensagem do usuário
    const newMessage: Message = {
      id: Date.now().toString(),
      content: inputValue,
      sender: "user",
      timestamp: "Just now",
    };

    setMessages((prev) => [...prev, newMessage]);
    setInputValue("");
    setIsLoading(true);

    try {
      // Chamada ao backend
      const response = await sendPrompt(inputValue, emailInput);

      // Adiciona resposta do agente
      const agentResponse: Message = {
        id: response.id,
        content: response.content,
        sender: "agent",
        timestamp: "Just now",
      };
      setMessages((prev) => [...prev, agentResponse]);

      toast({
        title: "Sucesso",
        description: "Mensagem processada com sucesso",
      });
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : "Erro desconhecido";
      toast({
        title: "Erro",
        description: errorMessage,
        variant: "destructive",
      });

      // Remove a mensagem do usuário em caso de erro
      setMessages((prev) => prev.slice(0, -1));
    } finally {
      setIsLoading(false);
    }
  };

  const handleReset = () => {
    setMessages(initialMessages);
  };

  return (
    <aside className="w-full h-full bg-sidebar flex flex-col">
      <div className="p-4 border-b border-sidebar-border flex items-center justify-between">
        <div className="flex items-center gap-2">
          <span className="w-2 h-2 rounded-full bg-success animate-pulse-soft" />
          <span className="text-sm font-medium text-foreground">LIVE PREVIEW</span>
        </div>
        <button
          onClick={handleReset}
          className="text-sm text-primary hover:text-primary/80 transition-colors"
        >
          Reset Chat
        </button>
      </div>

      <div className="p-4 border-b border-sidebar-border">
        <div className="flex items-center gap-3 p-3 bg-card rounded-lg border border-border">
          <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
            <Bot className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h4 className="text-sm font-semibold text-foreground">Customer Success</h4>
            <div className="flex items-center gap-2">
              <span className="text-xs px-2 py-0.5 rounded bg-primary/20 text-primary font-medium">
                SUPPORT
              </span>
              <span className="text-xs text-muted-foreground">v1.0</span>
            </div>
          </div>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-4 scrollbar-thin">
        {messages.map((message, index) => (
          <div
            key={message.id}
            className={cn(
              "animate-fade-in",
              message.sender === "user" ? "flex justify-end" : ""
            )}
          >
            {message.sender === "user" ? (
              <div className="max-w-[85%]">
                {index === 0 && (
                  <p className="text-xs text-muted-foreground text-right mb-2">
                    {message.timestamp}
                  </p>
                )}
                <div className="bg-chat-user text-foreground rounded-2xl rounded-br-md px-4 py-3 relative">
                  <p className="text-sm">{message.content}</p>
                  <div className="absolute -right-1 -bottom-1 w-4 h-4 bg-success rounded-full border-2 border-sidebar" />
                </div>
              </div>
            ) : (
              <div className="max-w-[95%]">
                <div className="flex items-center gap-2 mb-2">
                  <div className="w-6 h-6 rounded-md bg-primary/20 flex items-center justify-center">
                    <Bot className="w-3 h-3 text-primary" />
                  </div>
                  <span className="text-xs font-medium text-foreground">Customer Success</span>
                  <span className="text-xs text-muted-foreground">{message.timestamp}</span>
                </div>
                <div className="bg-chat-agent text-foreground rounded-2xl rounded-tl-md px-4 py-3">
                  <p className="text-sm whitespace-pre-line">{message.content}</p>
                  {message.hasLink && (
                    <a
                      href={message.hasLink.url}
                      className="flex items-center gap-1 text-primary text-sm mt-3 hover:underline"
                    >
                      <Link2 className="w-3 h-3" />
                      {message.hasLink.text}
                    </a>
                  )}
                </div>
              </div>
            )}
          </div>
        ))}
        {isLoading && (
          <div className="flex gap-2 items-center">
            <div className="w-6 h-6 rounded-md bg-primary/20 flex items-center justify-center">
              <Bot className="w-3 h-3 text-primary" />
            </div>
            <div className="flex gap-1">
              <div className="w-2 h-2 rounded-full bg-primary animate-bounce" />
              <div className="w-2 h-2 rounded-full bg-primary animate-bounce" style={{ animationDelay: "0.1s" }} />
              <div className="w-2 h-2 rounded-full bg-primary animate-bounce" style={{ animationDelay: "0.2s" }} />
            </div>
          </div>
        )}
      </div>

      <div className="p-4 border-t border-sidebar-border space-y-3">
        <Input
          value={emailInput}
          onChange={(e) => setEmailInput(e.target.value)}
          placeholder="Seu email..."
          type="email"
          disabled={isLoading}
          className="bg-card border-border text-foreground placeholder:text-muted-foreground"
        />
        <div className="flex items-center gap-2">
          <Input
            value={inputValue}
            onChange={(e) => setInputValue(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && !isLoading && handleSend()}
            placeholder="Ask your agent something..."
            disabled={isLoading}
            className="flex-1 bg-card border-border text-foreground placeholder:text-muted-foreground disabled:opacity-50"
          />
          <Button
            onClick={handleSend}
            size="icon"
            disabled={isLoading}
            className="bg-primary text-primary-foreground hover:bg-primary/90 disabled:opacity-50"
          >
            {isLoading ? (
              <Loader2 className="w-4 h-4 animate-spin" />
            ) : (
              <Send className="w-4 h-4" />
            )}
          </Button>
        </div>
      </div>
    </aside>
  );
};
