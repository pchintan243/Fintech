import { useQuery, useMutation } from "@tanstack/react-query";
import { navigate } from "wouter/use-browser-location";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";

// Helper for API calls
async function apiFetch(endpoint: string, options: RequestInit = {}) {
  const token = localStorage.getItem("fintrack_token");
  const headers = {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...(options.headers || {}),
  };

  const response = await fetch(`${API_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    if (response.status === 401) {
      localStorage.removeItem("fintrack_token");
      localStorage.removeItem("fintrack_user");
      navigate("/login");
    }
    throw new Error(`API Error: ${response.statusText}`);
  }

  if (response.status === 204) return null;
  return response.json();
}

// Types based on Java Entities
export interface User {
  id: number;
  email: string;
  fullName: string;
  phone: string;
  kycStatus: string;
  accountTier: string;
  transactionLimit: string;
  country: string;
  createdAt: string;
}

export interface Wallet {
  id: number;
  user: User;
  userId: number;
  currency: string;
  walletNumber: string;
  balance: string;
  availableBalance: string;
  status: string;
}

export interface Transaction {
  id: number;
  walletId: number;
  type: string;
  amount: string;
  currency: string;
  status: string;
  description: string;
  reference: string;
  counterpartyName?: string;
  counterpartyAccount?: string;
  balanceBefore: string;
  balanceAfter: string;
  createdAt: string;
}

export interface RiskFlag {
  id: number;
  userId: number;
  type: string;
  severity: string;
  status: string;
  description: string;
  createdAt: string;
}

export interface Notification {
  id: number;
  type: string;
  title: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export type ListTransactionsParams = any;

// Query Keys
export const getListUsersQueryKey = () => ["users"];
export const getGetUserQueryKey = (id: string | number) => ["users", id];
export const getListWalletsQueryKey = () => ["wallets"];
export const getGetWalletQueryKey = (id: string | number) => ["wallets", id];
export const getListTransactionsQueryKey = () => ["transactions"];
export const getListRiskFlagsQueryKey = () => ["risk-flags"];
export const getListNotificationsQueryKey = () => ["notifications"];
export const getGetDashboardStatsQueryKey = () => ["dashboard-stats"];

// Hooks - Users
export const useListUsers = (options: any = {}) => 
  useQuery({ queryKey: getListUsersQueryKey(), queryFn: () => apiFetch("/users"), ...options.query });

export const useGetUser = (id: string | number, options: any = {}) => 
  useQuery({ queryKey: getGetUserQueryKey(id), queryFn: () => apiFetch(`/users/${id}`), ...options.query });

export const useCreateUser = (options: any = {}) => 
  useMutation({ mutationFn: (payload: any) => apiFetch("/users", { method: "POST", body: JSON.stringify(payload.data || payload) }), ...options.mutation });

export const useUpdateUser = (options: any = {}) => 
  useMutation({ mutationFn: (payload: any) => apiFetch(`/users/${payload.userId || payload.id}`, { method: "PUT", body: JSON.stringify(payload.data || payload) }), ...options.mutation });

// Hooks - Wallets
export const useListWallets = (options: any = {}) => 
  useQuery({ queryKey: getListWalletsQueryKey(), queryFn: () => apiFetch("/wallets"), ...options.query });

export const useGetWallet = (id: string | number, options: any = {}) => 
  useQuery({ queryKey: getGetWalletQueryKey(id), queryFn: () => apiFetch(`/wallets/${id}`), ...options.query });

export const useCreateWallet = (options: any = {}) => 
  useMutation({ mutationFn: (data: any) => apiFetch("/wallets", { method: "POST", body: JSON.stringify(data) }), ...options.mutation });

// Hooks - Transactions
export const useListTransactions = (params: ListTransactionsParams = {}) => 
  useQuery({ queryKey: getListTransactionsQueryKey(), queryFn: () => apiFetch("/transactions") });

export const useGetTransaction = (id: string | number, options: any = {}) => 
  useQuery({ queryKey: ["transactions", id], queryFn: () => apiFetch(`/transactions/${id}`), ...options.query });

// Hooks - Risk
export const useListRiskFlags = () => 
  useQuery({ queryKey: getListRiskFlagsQueryKey(), queryFn: () => apiFetch("/risk") });

export const useResolveRiskFlag = (options: any = {}) => 
  useMutation({ mutationFn: (id: string | number) => apiFetch(`/risk/${id}/resolve`, { method: "POST" }), ...options.mutation });

// Hooks - Notifications
export const useListNotifications = () => 
  useQuery({ queryKey: getListNotificationsQueryKey(), queryFn: () => apiFetch("/notifications") });

export const useMarkNotificationRead = (options: any = {}) => 
  useMutation({ mutationFn: (id: string | number) => apiFetch(`/notifications/${id}/read`, { method: "POST" }), ...options.mutation });

// Payments
export const useInitiateDeposit = (options: any = {}) => 
  useMutation({ mutationFn: (data: any) => apiFetch("/payments/deposit", { method: "POST", body: JSON.stringify(data) }), ...options.mutation });

export const useInitiateWithdrawal = (options: any = {}) => 
  useMutation({ mutationFn: (data: any) => apiFetch("/payments/withdrawal", { method: "POST", body: JSON.stringify(data) }), ...options.mutation });

export const useTransferFunds = (options: any = {}) => 
  useMutation({ mutationFn: (data: any) => apiFetch("/payments/transfer", { method: "POST", body: JSON.stringify(data) }), ...options.mutation });

// Stats
export const useGetDashboardStats = () => 
  useQuery({ queryKey: getGetDashboardStatsQueryKey(), queryFn: () => apiFetch("/transactions/stats") });
