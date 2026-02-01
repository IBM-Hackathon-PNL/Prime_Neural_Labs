import { Pencil, FileText, Info } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Textarea } from "@/components/ui/textarea";
import { Slider } from "@/components/ui/slider";
import { useState } from "react";
import { cn } from "@/lib/utils";

interface BehaviorFormProps {
  instructions: string;
  onInstructionsChange: (value: string) => void;
  creativity: number;
  onCreativityChange: (value: number) => void;
  strictness: number;
  onStrictnessChange: (value: number) => void;
}

export const BehaviorForm = ({
  instructions,
  onInstructionsChange,
  creativity,
  onCreativityChange,
  strictness,
  onStrictnessChange,
}: BehaviorFormProps) => {
  const creativityLabels = ["Factual", "Balanced", "Imaginative"];
  const strictnessLabels = ["Flexible", "Moderate", "Strict"];

  const getCreativityLabel = (value: number) => {
    if (value <= 33) return creativityLabels[0];
    if (value <= 66) return creativityLabels[1];
    return creativityLabels[2];
  };

  const getStrictnessLabel = (value: number) => {
    if (value <= 33) return strictnessLabels[0];
    if (value <= 66) return strictnessLabels[1];
    return strictnessLabels[2];
  };

  return (
    <div className="flex-1 p-8 overflow-y-auto scrollbar-thin">
      <div className="max-w-2xl">
        <h1 className="text-3xl font-bold text-foreground mb-3">
          Agent Behavior & Style
        </h1>
        <p className="text-muted-foreground mb-8">
          Define how your agent interacts, its personality traits, and operational
          boundaries using simple instructions.
        </p>

        <div className="border-t border-border pt-6 mb-6">
          <div className="flex items-center justify-between mb-4">
            <h3 className="text-lg font-semibold text-foreground">Core Instructions</h3>
            <div className="flex items-center gap-2">
              <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-foreground">
                <Pencil className="w-4 h-4 mr-2" />
                Improve writing
              </Button>
              <Button variant="ghost" size="sm" className="text-muted-foreground hover:text-foreground">
                <FileText className="w-4 h-4 mr-2" />
                Examples
              </Button>
            </div>
          </div>

          <div className="relative">
            <Textarea
              value={instructions}
              onChange={(e) => onInstructionsChange(e.target.value)}
              placeholder="Describe your agent's role, goals, and personality..."
              className="min-h-[300px] bg-card border-border text-foreground placeholder:text-muted-foreground resize-none"
            />
            <span className="absolute bottom-3 right-3 text-xs text-muted-foreground">
              Optimized for Clarity
            </span>
          </div>
        </div>

        <div className="bg-card border border-border rounded-lg p-6">
          <div className="grid grid-cols-2 gap-8">
            <div>
              <div className="flex items-center gap-2 mb-4">
                <span className="text-sm font-medium text-foreground">Creativity Level</span>
                <Info className="w-4 h-4 text-muted-foreground" />
                <span className="ml-auto text-sm font-medium text-primary">
                  {getCreativityLabel(creativity)}
                </span>
              </div>
              <Slider
                value={[creativity]}
                onValueChange={(v) => onCreativityChange(v[0])}
                max={100}
                step={1}
                className="mb-3"
              />
              <div className="flex justify-between text-xs text-muted-foreground">
                {creativityLabels.map((label) => (
                  <span key={label}>{label}</span>
                ))}
              </div>
            </div>

            <div>
              <div className="flex items-center gap-2 mb-4">
                <span className="text-sm font-medium text-foreground">Strictness</span>
                <Info className="w-4 h-4 text-muted-foreground" />
                <span className="ml-auto text-sm font-medium text-primary">
                  {getStrictnessLabel(strictness)}
                </span>
              </div>
              <Slider
                value={[strictness]}
                onValueChange={(v) => onStrictnessChange(v[0])}
                max={100}
                step={1}
                className="mb-3"
              />
              <div className="flex justify-between text-xs text-muted-foreground">
                {strictnessLabels.map((label) => (
                  <span key={label}>{label}</span>
                ))}
              </div>
            </div>
          </div>
        </div>

        <div className="flex items-center justify-between mt-8 pt-6 border-t border-border">
          <Button variant="outline" className="border-border text-foreground hover:bg-muted">
            Back
          </Button>
          <div className="flex items-center gap-3">
            <Button variant="ghost" className="text-muted-foreground hover:text-foreground">
              Save Draft
            </Button>
            <Button className="bg-primary text-primary-foreground hover:bg-primary/90">
              Save & Continue
              <span className="ml-2">→</span>
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
};
