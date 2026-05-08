import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { API_URL } from "@/lib/config";

let backendDownShown = false;

const showBackendDownToast = () => {
  if (backendDownShown) return;
  backendDownShown = true;
  toast.error("Backend is unavailable", {
    description: "Cannot connect to the server. Please check if the backend is running.",
    duration: 1000,
  });
  setTimeout(() => {
    backendDownShown = false;
  }, 4000);
};

async function apiFetch(endpoint: string, options: RequestInit = {}) {
  const token = localStorage.getItem("fintrack_token");
  const headers = {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {}),
  };
  let response: Response;
  try {
    response = await fetch(`${API_URL}${endpoint}`, { ...options, headers });
  } catch (err) {
    showBackendDownToast();
    throw err;
  }
  if (!response.ok) {
    if (response.status === 401) {
      localStorage.removeItem("fintrack_token");
      localStorage.removeItem("fintrack_user");
    }
    throw new Error(`API Error: ${response.statusText}`);
  }
  if (response.status === 204) return null;
  return response.json();
}

export interface Currency {
  id: number;
  code: string;
  name: string;
  symbol: string;
  decimals: number;
  isActive: boolean;
  exchangeRate: string;
  createdAt: string;
}

export const getListCurrenciesQueryKey = () => ["currencies"];

export function useCurrencies() {
  return useQuery({
    queryKey: getListCurrenciesQueryKey(),
    queryFn: () => apiFetch("/currencies"),
  });
}

export function useCreateCurrency() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (data: any) =>
      apiFetch("/currencies", {
        method: "POST",
        body: JSON.stringify(data),
      }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: getListCurrenciesQueryKey() });
    },
  });
}

export function useUpdateCurrency() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: ({ id, data }: { id: number; data: any }) =>
      apiFetch(`/currencies/${id}`, {
        method: "PUT",
        body: JSON.stringify(data),
      }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: getListCurrenciesQueryKey() });
    },
  });
}

export function useDeleteCurrency() {
  const qc = useQueryClient();
  return useMutation({
    mutationFn: (id: number) =>
      apiFetch(`/currencies/${id}`, { method: "DELETE" }),
    onSuccess: () => {
      qc.invalidateQueries({ queryKey: getListCurrenciesQueryKey() });
    },
  });
}
