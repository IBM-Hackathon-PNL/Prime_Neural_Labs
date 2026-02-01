import { Bot, HelpCircle } from "lucide-react";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

export const Header = () => {
  return (
    <header className="h-16 border-b border-border bg-sidebar flex items-center justify-between px-6">
      <div className="flex items-center gap-3">
        <div className="w-9 h-9 rounded-lg bg-primary/20 flex items-center justify-center">
          <Bot className="w-5 h-5 text-primary" />
        </div>
        <span className="text-lg font-semibold text-foreground">Agent Factory</span>
      </div>

      <div className="flex items-center gap-6">
        <button className="flex items-center gap-2 text-muted-foreground hover:text-foreground transition-colors">
          <HelpCircle className="w-4 h-4" />
          <span className="text-sm">Docs</span>
        </button>

        <div className="flex items-center gap-3">
          <div className="text-right">
            <p className="text-sm font-medium text-foreground">Alex Morgan</p>
            <p className="text-xs text-muted-foreground">No-Code Workspace</p>
          </div>
          <Avatar className="w-10 h-10 border-2 border-primary/30">
            <AvatarImage src="https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face" />
            <AvatarFallback className="bg-primary/20 text-primary">AM</AvatarFallback>
          </Avatar>
        </div>
      </div>
    </header>
  );
};
