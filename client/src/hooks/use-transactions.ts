import { useQuery } from "@tanstack/react-query";
import { useGetDashboardStats as useGenGetDashboardStats, Transaction } from "@/lib/api-client";
import { API_URL } from "@/lib/config";

async function apiFetchTransactions(params?: { status?: string }): Promise<Transaction[]> {
  const token = localStorage.getItem("fintrack_token");
  const base = `${API_URL}/transactions`;
  const url = params?.status ? `${base}?status=${params.status}` : base;
  const res = await fetch(url, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
  });
  if (!res.ok) throw new Error(`API Error: ${res.statusText}`);
  return res.json();
}

export function useTransactions(params?: { status?: string }) {
  return useQuery({
    queryKey: params?.status ? ["transactions", params.status] : ["transactions"],
    queryFn: () => apiFetchTransactions(params),
  });
}

export function useTransaction(transactionId: string) {
  return useQuery({
    queryKey: ["transactions", transactionId],
    queryFn: async () => {
      const token = localStorage.getItem("fintrack_token");
      const res = await fetch(`${API_URL}/transactions/${transactionId}`, {
        headers: {
          "Content-Type": "application/json",
          ...(token ? { Authorization: `Bearer ${token}` } : {}),
        },
      });
      if (!res.ok) throw new Error(`API Error: ${res.statusText}`);
      return res.json();
    },
    enabled: !!transactionId,
  });
}

export function useDashboardStats() {
  return useGenGetDashboardStats();
}
