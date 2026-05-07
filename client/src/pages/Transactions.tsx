import * as React from "react";
import { motion } from "framer-motion";
import { Filter, ArrowUpRight, ArrowDownLeft, RefreshCcw } from "lucide-react";
import { Card } from "@/components/ui/card";
import { Transaction } from "@/lib/api-client";
import { Badge } from "@/components/ui/Badge";
import { useTransactions } from "@/hooks/use-transactions";
import { formatCurrency, formatDate, cn } from "@/lib/utils";
import { Button } from "@/components/ui/Button";

export default function TransactionsPage() {
  const [statusFilter, setStatusFilter] = React.useState<any>("");
  const { data, isLoading } = useTransactions(statusFilter ? { status: statusFilter } : undefined);

  const getTypeIcon = (type: string) => {
    if (type === 'DEPOSIT' || type === 'CREDIT') return <ArrowDownLeft className="w-4 h-4 text-success" />;
    if (type === 'WITHDRAWAL' || type === 'DEBIT') return <ArrowUpRight className="w-4 h-4 text-danger" />;
    return <RefreshCcw className="w-4 h-4 text-info" />;
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between gap-4">
        <div>
          <h1 className="text-3xl font-display font-bold">Transaction Ledger</h1>
          <p className="text-muted-foreground">Comprehensive history of all movements</p>
        </div>
        <div className="flex items-center gap-2">
          <Filter className="w-4 h-4 text-muted-foreground" />
          <select 
            className="h-10 px-4 rounded-xl border border-border bg-card select-reset text-sm focus:ring-2 focus:ring-primary/50 outline-none"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="">All Statuses</option>
            <option value="COMPLETED">Completed</option>
            <option value="PENDING">Pending</option>
            <option value="FAILED">Failed</option>
          </select>
        </div>
      </div>

      <Card className="p-0 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm whitespace-nowrap">
            <thead className="uppercase tracking-wider text-muted-foreground bg-muted/20 text-xs">
              <tr>
                <th className="px-6 py-4 font-medium">TxID / Reference</th>
                <th className="px-6 py-4 font-medium">Type</th>
                <th className="px-6 py-4 font-medium text-right">Amount</th>
                <th className="px-6 py-4 font-medium text-center">Status</th>
                <th className="px-6 py-4 font-medium">Date</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {isLoading ? (
                <tr><td colSpan={5} className="px-6 py-8 text-center text-muted-foreground">Loading ledger...</td></tr>
              ) : !data || data.length === 0 ? (
                <tr><td colSpan={5} className="px-6 py-8 text-center text-muted-foreground">No transactions found.</td></tr>
              ) : (
                data.map((tx: Transaction) => (
                  <tr key={tx.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="px-6 py-4">
                      <p className="font-mono text-foreground font-medium">{tx.reference}</p>
                      <p className="text-xs text-muted-foreground font-mono truncate w-32">{tx.id}</p>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <div className="w-8 h-8 rounded-full bg-background flex items-center justify-center border border-white/5">
                          {getTypeIcon(tx.type)}
                        </div>
                        <span className="font-medium">{tx.type}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-right">
                      <span className={cn(
                        "font-bold font-mono text-base",
                        (tx.type === "DEPOSIT" || tx.type === "CREDIT") ? "text-success" : "text-foreground"
                      )}>
                        {(tx.type === "DEPOSIT" || tx.type === "CREDIT") ? "+" : "-"}{formatCurrency(Number(tx.amount), tx.currency)}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-center">
                      <Badge variant={
                        tx.status === "COMPLETED" ? "success" : 
                        tx.status === "FAILED" ? "danger" : 
                        tx.status === "REFUNDED" ? "outline" : "warning"
                      }>{tx.status}</Badge>
                    </td>
                    <td className="px-6 py-4 text-muted-foreground">{formatDate(tx.createdAt)}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
        
        {/* Pagination mock visual */}
        <div className="p-4 border-t border-border bg-card/50 flex items-center justify-between text-sm text-muted-foreground">
          <span>Showing {data?.length || 0} of {data?.length || 0} entries</span>
          <div className="flex gap-2">
            <Button variant="outline" size="sm" disabled>Previous</Button>
            <Button variant="outline" size="sm" disabled>Next</Button>
          </div>
        </div>
      </Card>
    </motion.div>
  );
}
