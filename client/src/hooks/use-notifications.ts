import { useListNotifications as useGenListNotifications } from "@/lib/api-client";

export function useNotifications() {
  return useGenListNotifications();
}
