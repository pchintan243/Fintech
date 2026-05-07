import * as React from "react";
import { motion } from "framer-motion";
import { ArrowDownCircle, ArrowUpCircle, RefreshCw } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { useWallets } from "@/hooks/use-wallets";
import { useDeposit, useWithdrawal, useTransfer } from "@/hooks/use-payments";
import { cn, formatCurrency } from "@/lib/utils";
import type { Wallet } from "@/lib/api-client";

export default function PaymentsPage() {
  const [activeTab, setActiveTab] = React.useState<"deposit" | "withdraw" | "transfer">("deposit");
  const { data: rawWallets } = useWallets();
  const wallets = (rawWallets ?? []) as Wallet[];

  const { mutate: deposit, isPending: depPending } = useDeposit();
  const { mutate: withdraw, isPending: wdwPending } = useWithdrawal();
  const { mutate: transfer, isPending: trxPending } = useTransfer();

  const [walletId, setWalletId] = React.useState("");
  const [toWalletId, setToWalletId] = React.useState("");
  const [amount, setAmount] = React.useState("");
  const [desc, setDesc] = React.useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const numAmount = parseFloat(amount);
    if (!walletId || isNaN(numAmount)) return;

    const onSuccess = () => {
      setAmount("");
      setDesc("");
    };

    if (activeTab === "deposit") deposit({ walletId: Number(walletId), amount: String(numAmount), currency: "USD", description: desc }, { onSuccess });
    if (activeTab === "withdraw") withdraw({ walletId: Number(walletId), amount: String(numAmount), currency: "USD", description: desc }, { onSuccess });
    if (activeTab === "transfer" && toWalletId) transfer({ fromWalletId: Number(walletId), toWalletId: Number(toWalletId), amount: String(numAmount), currency: "USD", description: desc }, { onSuccess });
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6 max-w-3xl mx-auto">
      <div>
        <h1 className="text-3xl font-display font-bold">Payments Engine</h1>
        <p className="text-muted-foreground">Execute manual ledger entries and transfers</p>
      </div>

      <div className="flex p-1 bg-muted rounded-xl gap-1">
        {[
          { id: "deposit", label: "Deposit", icon: ArrowDownCircle },
          { id: "withdraw", label: "Withdraw", icon: ArrowUpCircle },
          { id: "transfer", label: "Transfer", icon: RefreshCw },
        ].map(t => (
          <button
            key={t.id}
            onClick={() => setActiveTab(t.id as any)}
            className={cn(
              "flex-1 flex items-center justify-center gap-2 py-3 text-sm font-semibold rounded-lg transition-all",
              activeTab === t.id ? "bg-card text-foreground shadow-sm" : "text-muted-foreground hover:text-foreground"
            )}
          >
            <t.icon className="w-4 h-4" /> {t.label}
          </button>
        ))}
      </div>

      <Card className="relative overflow-hidden">
        <div className="absolute top-0 right-0 w-64 h-64 bg-primary/10 rounded-full blur-3xl -mr-20 -mt-20 pointer-events-none" />
        
        <form onSubmit={handleSubmit} className="space-y-6 relative z-10">
          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-1">
                {activeTab === "transfer" ? "From Wallet" : "Select Wallet"}
              </label>
              <select 
                required
                className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
                value={walletId}
                onChange={(e) => setWalletId(e.target.value)}
              >
                <option value="">Select a wallet...</option>
                {wallets.map(w => (
                  <option key={w.id} value={w.id}>
                    {w.walletNumber} • {formatCurrency(w.balance, w.currencyCode)}
                  </option>
                ))}
              </select>
            </div>

            {activeTab === "transfer" && (
              <div>
                <label className="block text-sm font-medium mb-1">To Wallet</label>
                <select 
                  required
                  className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
                  value={toWalletId}
                  onChange={(e) => setToWalletId(e.target.value)}
                >
                  <option value="">Select destination wallet...</option>
                  {wallets.filter(w => w.id !== Number(walletId)).map(w => (
                    <option key={w.id} value={w.id}>
                      {w.walletNumber} • {w.currencyCode}
                    </option>
                  ))}
                </select>
              </div>
            )}

            <div>
              <label className="block text-sm font-medium mb-1">Amount</label>
              <div className="relative">
                <span className="absolute left-4 top-1/2 -translate-y-1/2 text-muted-foreground font-mono">$</span>
                <Input 
                  required 
                  type="number" 
                  step="0.01" 
                  min="0"
                  className="pl-8 text-xl font-mono" 
                  placeholder="0.00" 
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Description (Optional)</label>
              <Input 
                placeholder="e.g. Account funding" 
                value={desc}
                onChange={(e) => setDesc(e.target.value)}
              />
            </div>
          </div>

          <Button 
            type="submit" 
            size="lg" 
            className="w-full text-lg"
            isLoading={depPending || wdwPending || trxPending}
          >
            Execute {activeTab.charAt(0).toUpperCase() + activeTab.slice(1)}
          </Button>
        </form>
      </Card>
    </motion.div>
  );
}
