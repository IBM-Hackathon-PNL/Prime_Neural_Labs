import { Header } from "./Header";
import { ChatPreview } from "./ChatPreview";

export const AgentBuilderLayout = () => {
  return (
    <div className="h-screen flex flex-col bg-background">
      <Header />
      <div className="flex-1 overflow-hidden">
        <ChatPreview />
      </div>
    </div>
  );
};
