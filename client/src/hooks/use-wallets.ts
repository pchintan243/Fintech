import { 
  useListWallets as useGenListWallets, 
  useCreateWallet as useGenCreateWallet,
  useGetWallet as useGenGetWallet,
  getListWalletsQueryKey,
  getGetWalletQueryKey
} from "@/lib/api-client";
import { useQueryClient } from "@tanstack/react-query";

export function useWallets() {
  return useGenListWallets();
}

export function useWallet(walletId: string) {
  return useGenGetWallet(walletId, {
    query: { enabled: !!walletId }
  });
}

export function useCreateWallet() {
  const qc = useQueryClient();
  return useGenCreateWallet({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListWalletsQueryKey() });
      }
    }
  });
}
