import * as React from "react";
import { motion } from "framer-motion";
import { CheckCircle, AlertTriangle } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { Button } from "@/components/ui/Button";
import { Modal } from "@/components/ui/Modal";
import { Input } from "@/components/ui/Input";
import { useRiskFlags, useResolveRiskFlag } from "@/hooks/use-risk";
import { formatDate, cn } from "@/lib/utils";
import type { RiskFlag } from "@/lib/api-client";

export default function RiskPage() {
  const { data: flags, isLoading } = useRiskFlags();
  const [selectedFlag, setSelectedFlag] = React.useState<RiskFlag | null>(null);

  const getSeverityColor = (sev: string) => {
    switch (sev) {
      case 'CRITICAL': return "text-danger bg-danger/10 border-danger/20";
      case 'HIGH': return "text-warning bg-warning/10 border-warning/20";
      case 'MEDIUM': return "text-primary bg-primary/10 border-primary/20";
      default: return "text-muted-foreground bg-muted border-border";
    }
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div>
        <h1 className="text-3xl font-display font-bold">Risk Operations</h1>
        <p className="text-muted-foreground">Monitor and resolve compliance flags</p>
      </div>

      <div className="grid grid-cols-1 gap-4">
        {isLoading ? (
          [1,2,3].map(i => <Card key={i} className="h-24 animate-pulse bg-muted" />)
        ) : flags?.length === 0 ? (
          <Card className="text-center py-12">
            <CheckCircle className="w-12 h-12 mx-auto text-success mb-4 opacity-50" />
            <h3 className="text-xl font-semibold mb-2">No active risk flags</h3>
            <p className="text-muted-foreground">The platform is operating within safe parameters.</p>
          </Card>
        ) : (
          flags?.map((flag: RiskFlag) => (
            <Card key={flag.id} className="flex flex-col sm:flex-row sm:items-center justify-between gap-6 hover:bg-white/[0.02]">
              <div className="flex items-start gap-4">
                <div className={cn("w-10 h-10 rounded-full flex items-center justify-center border shrink-0", getSeverityColor(flag.severity))}>
                  <AlertTriangle className="w-5 h-5" />
                </div>
                <div>
                  <div className="flex items-center gap-2 mb-1">
                    <h4 className="font-bold text-lg">{flag.type.replace('_', ' ')}</h4>
                    <span className={cn("text-[10px] uppercase font-bold px-2 py-0.5 rounded-full border", getSeverityColor(flag.severity))}>
                      {flag.severity}
                    </span>
                    <Badge variant={flag.status === 'RESOLVED' || flag.status === 'FALSE_POSITIVE' ? 'outline' : 'warning'}>
                      {flag.status}
                    </Badge>
                  </div>
                  <p className="text-muted-foreground text-sm max-w-2xl">{flag.description}</p>
                  <div className="mt-2 flex gap-4 text-xs font-mono text-muted-foreground">
                    <span>User: {flag.user?.email || flag.userId}</span>
                    <span>Created: {formatDate(flag.createdAt)}</span>
                  </div>
                </div>
              </div>

              <div className="shrink-0">
                {(flag.status === 'OPEN' || flag.status === 'INVESTIGATING') ? (
                  <Button onClick={() => setSelectedFlag(flag)}>Resolve Case</Button>
                ) : (
                  <div className="text-sm text-right">
                    <p className="text-success font-medium">Resolved</p>
                    <p className="text-muted-foreground text-xs">{flag.resolution}</p>
                  </div>
                )}
              </div>
            </Card>
          ))
        )}
      </div>

      <ResolveModal flag={selectedFlag} onClose={() => setSelectedFlag(null)} />
    </motion.div>
  );
}

function ResolveModal({ flag, onClose }: { flag: RiskFlag | null, onClose: () => void }) {
  const { mutate: resolve, isPending } = useResolveRiskFlag();
  const [status, setStatus] = React.useState<any>("RESOLVED");
  const [resolution, setResolution] = React.useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!flag) return;
    resolve({ id: flag.id, data: { status, resolution } }, { onSuccess: onClose });
  };

  return (
    <Modal isOpen={!!flag} onClose={onClose} title="Resolve Risk Case" description={flag?.type}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">Outcome</label>
          <select
            className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="RESOLVED">Resolved (Action Taken)</option>
            <option value="FALSE_POSITIVE">False Positive (Safe)</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Resolution Notes</label>
          <Input
            required
            placeholder="Explain action taken..."
            value={resolution}
            onChange={(e) => setResolution(e.target.value)}
          />
        </div>
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>Cancel</Button>
          <Button type="submit" isLoading={isPending}>Submit Resolution</Button>
        </div>
      </form>
    </Modal>
  );
}
