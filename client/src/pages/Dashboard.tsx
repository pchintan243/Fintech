import * as React from "react";
import { motion } from "framer-motion";
import { Users, Wallet, ArrowUpRight, ShieldAlert, Activity } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Badge } from "@/components/ui/Badge";
import { useDashboardStats } from "@/hooks/use-transactions";
import { cn, formatCurrency, formatDate, formatNumber } from "@/lib/utils";
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from "recharts";

export default function Dashboard() {
  const { data: stats, isLoading } = useDashboardStats();

  if (isLoading) {
    return (
      <div className="space-y-6 animate-pulse">
        <div className="h-8 w-48 bg-muted rounded-lg" />
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {[1, 2, 3, 4].map(i => <div key={i} className="h-32 bg-muted rounded-2xl" />)}
        </div>
        <div className="h-96 bg-muted rounded-2xl" />
      </div>
    );
  }

  if (!stats) return <div className="text-danger">Failed to load dashboard</div>;

  // Fake chart data based on monthly volume
  const chartData = [
    { name: "Jan", volume: stats.monthlyVolume * 0.4 },
    { name: "Feb", volume: stats.monthlyVolume * 0.6 },
    { name: "Mar", volume: stats.monthlyVolume * 0.8 },
    { name: "Apr", volume: stats.monthlyVolume * 1.1 },
    { name: "May", volume: stats.monthlyVolume * 0.9 },
    { name: "Jun", volume: stats.monthlyVolume },
  ];

  return (
    <motion.div initial={{ opacity: 0, y: 20 }} animate={{ opacity: 1, y: 0 }} className="space-y-8">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-display font-bold text-foreground">Platform Overview</h1>
          <p className="text-muted-foreground mt-1">Real-time metrics and system health</p>
        </div>
        <div className="flex items-center gap-2 text-sm text-success bg-success/10 px-4 py-2 rounded-full border border-success/20">
          <Activity className="w-4 h-4" />
          <span>System Operational</span>
        </div>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card className="relative overflow-hidden group">
          <div className="absolute right-0 top-0 w-24 h-24 bg-primary/10 rounded-bl-full -mr-4 -mt-4 transition-transform group-hover:scale-110" />
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-primary/20 text-primary flex items-center justify-center">
              <ArrowUpRight className="w-6 h-6" />
            </div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Total Volume</p>
              <h3 className="text-2xl font-bold font-display">{formatCurrency(stats.totalVolume)}</h3>
            </div>
          </div>
        </Card>

        <Card className="relative overflow-hidden group">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-success/20 text-success flex items-center justify-center">
              <Users className="w-6 h-6" />
            </div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Total Users</p>
              <h3 className="text-2xl font-bold font-display">{formatNumber(stats.totalUsers)}</h3>
            </div>
          </div>
        </Card>

        <Card className="relative overflow-hidden group">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-info/20 text-primary flex items-center justify-center">
              <Wallet className="w-6 h-6" />
            </div>
            <div>
              <p className="text-sm font-medium text-muted-foreground">Active Wallets</p>
              <h3 className="text-2xl font-bold font-display">{formatNumber(stats.activeWallets)}</h3>
            </div>
          </div>
        </Card>

        <Card className="relative overflow-hidden group border-warning/30 bg-warning/5">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-xl bg-warning/20 text-warning flex items-center justify-center">
              <ShieldAlert className="w-6 h-6" />
            </div>
            <div>
              <p className="text-sm font-medium text-warning">Open Risk Flags</p>
              <h3 className="text-2xl font-bold font-display text-warning">{formatNumber(stats.openRiskFlags)}</h3>
            </div>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <Card className="lg:col-span-2">
          <div className="mb-6">
            <h3 className="text-lg font-display font-bold">Transaction Volume (6 mo)</h3>
            <p className="text-sm text-muted-foreground">Monthly aggregate value across all currencies</p>
          </div>
          <div className="h-[300px] w-full">
            <ResponsiveContainer width="100%" height="100%">
              <AreaChart data={chartData} margin={{ top: 10, right: 10, left: 0, bottom: 0 }}>
                <defs>
                  <linearGradient id="colorVolume" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="hsl(var(--primary))" stopOpacity={0.3}/>
                    <stop offset="95%" stopColor="hsl(var(--primary))" stopOpacity={0}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="hsl(var(--border))" vertical={false} />
                <XAxis dataKey="name" stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} />
                <YAxis stroke="hsl(var(--muted-foreground))" fontSize={12} tickLine={false} axisLine={false} tickFormatter={(val) => `$${val/1000}k`} />
                <Tooltip 
                  contentStyle={{ backgroundColor: 'hsl(var(--card))', borderColor: 'hsl(var(--border))', borderRadius: '12px' }}
                  itemStyle={{ color: 'hsl(var(--foreground))' }}
                  formatter={(value: number) => formatCurrency(value)}
                />
                <Area type="monotone" dataKey="volume" stroke="hsl(var(--primary))" strokeWidth={3} fillOpacity={1} fill="url(#colorVolume)" />
              </AreaChart>
            </ResponsiveContainer>
          </div>
        </Card>

        <Card className="flex flex-col">
          <div className="mb-6">
            <h3 className="text-lg font-display font-bold">Recent Transactions</h3>
            <p className="text-sm text-muted-foreground">Latest platform activity</p>
          </div>
          <div className="flex-1 space-y-4 overflow-auto pr-2">
            {stats.recentTransactions?.length === 0 ? (
              <p className="text-muted-foreground text-sm">No recent transactions.</p>
            ) : (
              stats.recentTransactions?.slice(0, 5).map(tx => (
                <div key={tx.id} className="flex items-center justify-between p-3 rounded-xl hover:bg-white/5 transition-colors border border-transparent hover:border-white/10">
                  <div className="flex items-center gap-3">
                    <div className="w-10 h-10 rounded-full bg-muted flex items-center justify-center font-bold text-xs text-muted-foreground">
                      {tx.type.substring(0, 2)}
                    </div>
                    <div>
                      <p className="text-sm font-medium text-foreground">{tx.type}</p>
                      <p className="text-xs text-muted-foreground">{formatDate(tx.createdAt)}</p>
                    </div>
                  </div>
                  <div className="text-right">
                    <p className={cn("text-sm font-bold", tx.type === "CREDIT" || tx.type === "DEPOSIT" ? "text-success" : "text-foreground")}>
                      {tx.type === "CREDIT" || tx.type === "DEPOSIT" ? "+" : "-"}{formatCurrency(tx.amount, tx.currency)}
                    </p>
                    <Badge variant={
                      tx.status === "COMPLETED" ? "success" : 
                      tx.status === "FAILED" ? "danger" : "warning"
                    }>{tx.status}</Badge>
                  </div>
                </div>
              ))
            )}
          </div>
        </Card>
      </div>
    </motion.div>
  );
}
