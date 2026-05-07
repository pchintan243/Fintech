import { 
  useListRiskFlags as useGenListRiskFlags, 
  useResolveRiskFlag as useGenResolveRiskFlag,
  getListRiskFlagsQueryKey,
  getGetDashboardStatsQueryKey
} from "@/lib/api-client";
import { useQueryClient } from "@tanstack/react-query";

export function useRiskFlags() {
  return useGenListRiskFlags();
}

export function useResolveRiskFlag() {
  const qc = useQueryClient();
  return useGenResolveRiskFlag({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListRiskFlagsQueryKey() });
        qc.invalidateQueries({ queryKey: getGetDashboardStatsQueryKey() });
      }
    }
  });
}
