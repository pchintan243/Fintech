import { 
  useInitiateDeposit as useGenInitiateDeposit, 
  useInitiateWithdrawal as useGenInitiateWithdrawal,
  useTransferFunds as useGenTransferFunds,
  getListTransactionsQueryKey,
  getListWalletsQueryKey,
  getGetDashboardStatsQueryKey
} from "@/lib/api-client";
import { useQueryClient } from "@tanstack/react-query";

export function useDeposit() {
  const qc = useQueryClient();
  return useGenInitiateDeposit({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      }
    }
  });
}

export function useWithdrawal() {
  const qc = useQueryClient();
  return useGenInitiateWithdrawal({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      }
    }
  });
}

export function useTransfer() {
  const qc = useQueryClient();
  return useGenTransferFunds({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListTransactionsQueryKey() });
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      }
    }
  });
}
