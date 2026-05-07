import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import {
  useInitiateDeposit as useGenInitiateDeposit,
  useInitiateWithdrawal as useGenInitiateWithdrawal,
  useTransferFunds as useGenTransferFunds,
  getListTransactionsQueryKey,
  getListWalletsQueryKey,
  getGetDashboardStatsQueryKey,
} from "@/lib/api-client";

function handleBackendError(err: unknown): string {
  if (err instanceof Error) {
    // Try to parse backend error message from fetch error
    const msg = err.message;
    if (msg.includes("API Error:")) {
      return msg.replace("API Error: ", "");
    }
    return msg;
  }
  return "Something went wrong";
}

export function useDeposit() {
  const qc = useQueryClient();
  return useGenInitiateDeposit({
    mutation: {
      onSuccess: () => {
        toast.success("Deposit successful", { description: "Funds have been credited to your wallet." });
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      },
      onError: (err: unknown) => {
        toast.error("Deposit failed", { description: handleBackendError(err) });
      },
    },
  });
}

export function useWithdrawal() {
  const qc = useQueryClient();
  return useGenInitiateWithdrawal({
    mutation: {
      onSuccess: () => {
        toast.success("Withdrawal successful", { description: "Funds have been debited from your wallet." });
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      },
      onError: (err: unknown) => {
        toast.error("Withdrawal failed", { description: handleBackendError(err) });
      },
    },
  });
}

export function useTransfer() {
  const qc = useQueryClient();
  return useGenTransferFunds({
    mutation: {
      onSuccess: () => {
        toast.success("Transfer successful", { description: "Funds have been transferred." });
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      },
      onError: (err: unknown) => {
        toast.error("Transfer failed", { description: handleBackendError(err) });
      },
    },
  });
}
