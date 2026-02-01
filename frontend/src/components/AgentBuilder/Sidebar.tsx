import { Check, Settings, Wrench, BookOpen, Lightbulb } from "lucide-react";
import { cn } from "@/lib/utils";

interface Step {
  id: string;
  label: string;
  description: string;
  icon: React.ReactNode;
  completed?: boolean;
  active?: boolean;
}

interface SidebarProps {
  currentStep: string;
  onStepChange: (step: string) => void;
}

const steps: Step[] = [
  {
    id: "identity",
    label: "Identity",
    description: "Name & Role",
    icon: <Check className="w-4 h-4" />,
    completed: true,
  },
  {
    id: "behavior",
    label: "Behavior",
    description: "Instructions & Style",
    icon: <Settings className="w-4 h-4" />,
    active: true,
  },
  {
    id: "tools",
    label: "Tools",
    description: "Email, Search, Integrations",
    icon: <Wrench className="w-4 h-4" />,
  },
  {
    id: "knowledge",
    label: "Knowledge",
    description: "Files & Guidelines",
    icon: <BookOpen className="w-4 h-4" />,
  },
];

export const Sidebar = ({ currentStep, onStepChange }: SidebarProps) => {
  return (
    <aside className="w-64 bg-sidebar border-r border-sidebar-border flex flex-col">
      <div className="p-6">
        <h2 className="text-lg font-semibold text-foreground">Create Agent</h2>
        <p className="text-sm text-muted-foreground">Visual Builder</p>
      </div>

      <nav className="flex-1 px-3">
        {steps.map((step) => {
          const isActive = currentStep === step.id;
          const isCompleted = step.completed;

          return (
            <button
              key={step.id}
              onClick={() => onStepChange(step.id)}
              className={cn(
                "w-full flex items-center gap-3 px-3 py-3 rounded-lg mb-1 transition-all text-left",
                isActive
                  ? "bg-sidebar-accent"
                  : "hover:bg-sidebar-accent/50"
              )}
            >
              <div
                className={cn(
                  "w-8 h-8 rounded-lg flex items-center justify-center transition-colors",
                  isCompleted
                    ? "bg-primary/20 text-primary"
                    : isActive
                    ? "bg-primary text-primary-foreground"
                    : "bg-muted text-muted-foreground"
                )}
              >
                {isCompleted ? <Check className="w-4 h-4" /> : step.icon}
              </div>
              <div>
                <p
                  className={cn(
                    "text-sm font-medium",
                    isActive ? "text-primary" : "text-foreground"
                  )}
                >
                  {step.label}
                </p>
                <p className="text-xs text-muted-foreground">{step.description}</p>
              </div>
            </button>
          );
        })}
      </nav>

      <div className="p-4 m-4 bg-card rounded-lg border border-border">
        <div className="flex items-start gap-3">
          <div className="w-8 h-8 rounded-full bg-warning/20 flex items-center justify-center flex-shrink-0">
            <Lightbulb className="w-4 h-4 text-warning" />
          </div>
          <p className="text-xs text-muted-foreground leading-relaxed">
            Tip: Use natural language. Describe the agent's job like you're hiring a human.
          </p>
        </div>
      </div>
    </aside>
  );
};
