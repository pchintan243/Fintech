import { 
  useListUsers as useGenListUsers, 
  useCreateUser as useGenCreateUser,
  useGetUser as useGenGetUser,
  useUpdateUser as useGenUpdateUser,
  getListUsersQueryKey,
  getGetUserQueryKey
} from "@/lib/api-client";
import { useQueryClient } from "@tanstack/react-query";

export function useUsers() {
  return useGenListUsers();
}

export function useUser(userId: string) {
  return useGenGetUser(userId, {
    query: { enabled: !!userId }
  });
}

export function useCreateUser() {
  const qc = useQueryClient();
  return useGenCreateUser({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListUsersQueryKey() });
      }
    }
  });
}

export function useUpdateUser() {
  const qc = useQueryClient();
  return useGenUpdateUser({
    mutation: {
      onSuccess: () => {
        qc.invalidateQueries({ queryKey: getListUsersQueryKey() });
      }
    }
  });
}
