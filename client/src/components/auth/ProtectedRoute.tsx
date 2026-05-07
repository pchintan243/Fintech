import { Redirect } from "wouter";
import { useAuth, UserRole } from "@/hooks/use-auth";

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: UserRole;
}

export function ProtectedRoute({ children, requiredRole }: ProtectedRouteProps) {
  const { isAuthenticated, user } = useAuth();

  // Not logged in — redirect to /login immediately (no spinner delay)
  if (!isAuthenticated) {
    return <Redirect to="/login" />;
  }

  // Logged in but wrong role — redirect to dashboard
  if (requiredRole && user?.role !== requiredRole && user?.role !== "ROLE_ADMIN") {
    return <Redirect to="/" />;
  }

  return <>{children}</>;
}
