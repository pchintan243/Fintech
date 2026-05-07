import * as React from "react";
import { motion } from "framer-motion";
import { Wallet, Plus, CreditCard, Shield } from "lucide-react";
import { Card } from "@/components/ui/card";
import { Button } from "@/components/ui/Button";
import { Badge } from "@/components/ui/Badge";
import { Modal } from "@/components/ui/Modal";
import { Input } from "@/components/ui/Input";
import { useWallets, useCreateWallet } from "@/hooks/use-wallets";
import { useUsers } from "@/hooks/use-users";
import { Wallet as WalletType, User as UserType } from "@/lib/api-client";
import { formatCurrency, formatDate } from "@/lib/utils";

export default function WalletsPage() {
  const { data: wallets, isLoading, error, refetch } = useWallets();
  const [isCreateOpen, setIsCreateOpen] = React.useState(false);

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between gap-4">
        <div>
          <h1 className="text-3xl font-display font-bold">Wallets</h1>
          <p className="text-muted-foreground">Manage digital wallets and balances</p>
        </div>
        <Button onClick={() => setIsCreateOpen(true)}>
          <Plus className="w-4 h-4 mr-2" /> Provision Wallet
        </Button>
      </div>

      {isLoading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1,2,3].map(i => <div key={i} className="h-48 bg-muted animate-pulse rounded-2xl" />)}
        </div>
      ) : error ? (
        <Card className="text-center py-12 border-destructive/20 bg-destructive/5">
          <Shield className="w-12 h-12 mx-auto text-destructive mb-4 opacity-50" />
          <h3 className="text-xl font-semibold mb-2">Failed to load wallets</h3>
          <p className="text-muted-foreground mb-4">There was an issue connecting to the server.</p>
          <Button variant="outline" onClick={() => refetch()}>Try Again</Button>
        </Card>
      ) : (wallets as WalletType[])?.length === 0 ? (
        <Card className="text-center py-12">
          <Wallet className="w-12 h-12 mx-auto text-muted-foreground mb-4 opacity-50" />
          <h3 className="text-xl font-semibold mb-2">No wallets found</h3>
          <p className="text-muted-foreground mb-4">Provision a wallet to start managing funds.</p>
          <Button onClick={() => setIsCreateOpen(true)}>Create First Wallet</Button>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {(wallets as WalletType[])?.map((wallet) => (
            <Card key={wallet.id} className="relative overflow-hidden group flex flex-col justify-between h-56">
              {/* Credit card aesthetic BG */}
              <div className="absolute inset-0 bg-gradient-to-br from-primary/10 to-transparent opacity-50" />
              <div className="absolute -right-12 -top-12 w-32 h-32 bg-primary/20 rounded-full blur-3xl" />
              
              <div className="relative z-10">
                <div className="flex justify-between items-start mb-4">
                  <div className="w-10 h-10 rounded-lg bg-background/50 flex items-center justify-center border border-white/5">
                    <CreditCard className="w-5 h-5 text-primary" />
                  </div>
                  <Badge variant={wallet.status === 'ACTIVE' ? 'success' : wallet.status === 'FROZEN' ? 'warning' : 'danger'}>
                    {wallet.status}
                  </Badge>
                </div>
                
                <h4 className="text-3xl font-display font-bold tracking-tight mb-1">
                  {formatCurrency(Number(wallet.balance), wallet.currency)}
                </h4>
                <p className="text-sm font-mono text-muted-foreground">
                  {wallet.currency} • Available: {formatCurrency(Number(wallet.availableBalance), wallet.currency)}
                </p>
              </div>

              <div className="relative z-10 pt-4 border-t border-white/5 flex justify-between items-end">
                <div>
                  <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Wallet ID</p>
                  <p className="text-sm font-mono">{wallet.walletNumber}</p>
                </div>
                {wallet.user && (
                  <div className="text-right">
                    <p className="text-xs text-muted-foreground uppercase tracking-wider mb-1">Owner</p>
                    <p className="text-sm font-medium">{wallet.user.fullName}</p>
                  </div>
                )}
              </div>
            </Card>
          ))}
        </div>
      )}

      <CreateWalletModal isOpen={isCreateOpen} onClose={() => setIsCreateOpen(false)} />
    </motion.div>
  );
}

function CreateWalletModal({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) {
  const { data: users } = useUsers();
  const { mutate: createWallet, isPending } = useCreateWallet();
  const [userId, setUserId] = React.useState("");
  const [currency, setCurrency] = React.useState("USD");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!userId) return;
    createWallet({ data: { userId, currency } }, { onSuccess: onClose });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Provision Wallet">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">Owner (User)</label>
          <select 
            required
            className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
            value={userId}
            onChange={(e) => setUserId(e.target.value)}
          >
            <option value="">Select a user...</option>
            {(users as UserType[])?.map(u => (
              <option key={u.id} value={u.id}>{u.fullName} ({u.email})</option>
            ))}
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Currency</label>
          <select 
            required
            className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
            value={currency}
            onChange={(e) => setCurrency(e.target.value)}
          >
            <option value="USD">USD - US Dollar</option>
            <option value="EUR">EUR - Euro</option>
            <option value="GBP">GBP - British Pound</option>
          </select>
        </div>
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>Cancel</Button>
          <Button type="submit" isLoading={isPending}>Create Wallet</Button>
        </div>
      </form>
    </Modal>
  );
}
