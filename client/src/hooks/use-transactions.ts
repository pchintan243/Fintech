import {
  useListTransactions as useGenListTransactions,
  useGetTransaction as useGenGetTransaction,
  useGetDashboardStats as useGenGetDashboardStats,
} from "@/lib/api-client";

export function useTransactions() {
  return useGenListTransactions();
}

export function useTransaction(transactionId: string) {
  return useGenGetTransaction(transactionId, {
    query: { enabled: !!transactionId }
  });
}

export function useDashboardStats() {
  return useGenGetDashboardStats();
}
