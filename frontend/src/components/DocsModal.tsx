import { useState } from "react";
import { BookOpen, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";

export const DocsButton = () => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Button
        variant="outline"
        size="sm"
        onClick={() => setOpen(true)}
        className="gap-2"
      >
        <BookOpen className="w-4 h-4" />
        Docs
      </Button>

      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="max-w-2xl max-h-[85vh] overflow-y-auto bg-sidebar border-border">
          <button
            onClick={() => setOpen(false)}
            className="absolute right-4 top-4 text-muted-foreground hover:text-foreground transition-colors"
          >
            <X className="w-5 h-5" />
          </button>

          <div className="space-y-8 pt-4 pr-6">
            {/* Title */}
            <div>
              <h2 className="text-2xl font-bold text-foreground flex items-center gap-3 mb-2">
                <span className="text-primary">◆</span> Prime Neural Labs
              </h2>
              <p className="text-sm text-muted-foreground">
                Democratizing AI agent creation for everyone
              </p>
            </div>

            {/* The Problem */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>📌</span> The Problem
              </h3>
              <p className="text-sm text-muted-foreground leading-relaxed">
                Automation with AI is still inaccessible for many users and
                companies. Developers face repetitive tasks, integration
                challenges, and lack of expertise. Companies struggle with
                governance, security, and control of autonomous agents.
              </p>
            </section>

            {/* The Solution */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>💡</span> The Solution
              </h3>
              <p className="text-sm text-muted-foreground leading-relaxed">
                Prime Neural Labs lets any user (technical or not) create,
                configure, and deploy AI agents from a simple prompt. Our
                guided workflow minimizes technical debt and eliminates the
                need for prompt engineering expertise.
              </p>
            </section>

            {/* Key Benefits */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>✨</span> Key Benefits
              </h3>
              <ul className="space-y-2">
                <li className="text-sm text-muted-foreground flex items-center gap-2">
                  <span className="text-primary">→</span> Describe needs in natural language
                </li>
                <li className="text-sm text-muted-foreground flex items-center gap-2">
                  <span className="text-primary">→</span> Reduce manual agent programming
                </li>
                <li className="text-sm text-muted-foreground flex items-center gap-2">
                  <span className="text-primary">→</span> Reuse efficient agent templates
                </li>
                <li className="text-sm text-muted-foreground flex items-center gap-2">
                  <span className="text-primary">→</span> Simple interface with guided setup
                </li>
                <li className="text-sm text-muted-foreground flex items-center gap-2">
                  <span className="text-primary">→</span> Scalable via containerized agents
                </li>
              </ul>
            </section>

            {/* Tech Stack */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>🛠️</span> Tech Stack
              </h3>
              <div className="grid grid-cols-2 gap-4">
                <div className="bg-card/50 rounded-lg p-3 border border-border/50">
                  <p className="text-xs font-semibold text-primary mb-1">Frontend</p>
                  <p className="text-sm text-foreground">React + TypeScript</p>
                </div>
                <div className="bg-card/50 rounded-lg p-3 border border-border/50">
                  <p className="text-xs font-semibold text-primary mb-1">Backend</p>
                  <p className="text-sm text-foreground">Java + Spring Boot</p>
                </div>
                <div className="bg-card/50 rounded-lg p-3 border border-border/50">
                  <p className="text-xs font-semibold text-primary mb-1">AI Server</p>
                  <p className="text-sm text-foreground">IBM Watsonx</p>
                </div>
                <div className="bg-card/50 rounded-lg p-3 border border-border/50">
                  <p className="text-xs font-semibold text-primary mb-1">Deployment</p>
                  <p className="text-sm text-foreground">Docker Containers</p>
                </div>
              </div>
            </section>

            {/* How It Works */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>🔄</span> How It Works
              </h3>
              <ol className="space-y-2">
                {[
                  "User sends prompt via chat",
                  "Backend augments with master prompt",
                  "Watsonx returns standardized JSON",
                  "Backend parametrizes agent template",
                  "Frontend collects missing data",
                  "Backend deploys agent as container",
                  "User receives agent access info",
                ].map((step, idx) => (
                  <li key={idx} className="text-sm text-muted-foreground flex items-start gap-3">
                    <span className="text-primary font-semibold min-w-6">{idx + 1}.</span>
                    <span>{step}</span>
                  </li>
                ))}
              </ol>
            </section>

            {/* Who Benefits */}
            <section className="border-l-2 border-primary/40 pl-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>👥</span> Who Benefits
              </h3>
              <ul className="space-y-2">
                {[
                  "Startups & SMEs needing automation",
                  "Teams requiring rapid prototyping",
                  "Users wanting custom AI solutions",
                  "Operations & consultants automating workflows",
                ].map((item, idx) => (
                  <li key={idx} className="text-sm text-muted-foreground flex items-center gap-2">
                    <span className="text-primary">✓</span> {item}
                  </li>
                ))}
              </ul>
            </section>

            {/* Getting Started */}
            <section className="border-l-2 border-primary/40 pl-4 pb-4">
              <h3 className="text-lg font-semibold text-foreground mb-3 flex items-center gap-2">
                <span>🚀</span> Getting Started
              </h3>
              <div className="space-y-2">
                <p className="text-sm text-muted-foreground">
                  1. Open the chat panel on the right
                </p>
                <p className="text-sm text-muted-foreground">
                  2. Describe your automation needs
                </p>
                <p className="text-sm text-muted-foreground">
                  3. Follow the guided setup to deploy your agent
                </p>
              </div>
            </section>
          </div>
        </DialogContent>
      </Dialog>
    </>
  );
};
