import { 
  useListTransactions as useGenListTransactions, 
  useGetTransaction as useGenGetTransaction,
  useGetDashboardStats as useGenGetDashboardStats,
  type ListTransactionsParams
} from "@/lib/api-client";

export function useTransactions(params?: ListTransactionsParams) {
  return useGenListTransactions(params);
}

export function useTransaction(transactionId: string) {
  return useGenGetTransaction(transactionId, {
    query: { enabled: !!transactionId }
  });
}

export function useDashboardStats() {
  return useGenGetDashboardStats();
}
