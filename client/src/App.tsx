import { Switch, Route, Router as WouterRouter } from "wouter";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { Toaster } from "@/components/ui/toaster";
import { TooltipProvider } from "@/components/ui/tooltip";
import { AppLayout } from "@/components/layout/AppLayout";
import NotFound from "@/pages/not-found";

import Dashboard from "@/pages/Dashboard";
import UsersPage from "@/pages/Users";
import WalletsPage from "@/pages/Wallets";
import TransactionsPage from "@/pages/Transactions";
import PaymentsPage from "@/pages/Payments";
import RiskPage from "@/pages/Risk";
import NotificationsPage from "@/pages/Notifications";
import Login from "@/pages/Login";
import { ProtectedRoute } from "@/components/auth/ProtectedRoute";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

function Router() {
  return (
    <Switch>
      <Route path="/login" component={Login} />
      {/* 
          Capture all other routes to be handled by ProtectedRoute and AppLayout.
          The nested Switch then handles the specific sub-pages.
      */}
      <Route>
        <ProtectedRoute>
          <AppLayout>
            <Switch>
              <Route path="/" component={Dashboard} />
              <Route path="/users">
                <ProtectedRoute requiredRole="ROLE_ADMIN">
                  <UsersPage />
                </ProtectedRoute>
              </Route>
              <Route path="/wallets" component={WalletsPage} />
              <Route path="/transactions" component={TransactionsPage} />
              <Route path="/payments" component={PaymentsPage} />
              <Route path="/risk">
                <ProtectedRoute requiredRole="ROLE_ADMIN">
                  <RiskPage />
                </ProtectedRoute>
              </Route>
              <Route path="/notifications" component={NotificationsPage} />
              <Route component={NotFound} />
            </Switch>
          </AppLayout>
        </ProtectedRoute>
      </Route>
    </Switch>
  );
}

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <TooltipProvider>
        <WouterRouter base={import.meta.env.BASE_URL.replace(/\/$/, "")}>
          <Router />
        </WouterRouter>
        <Toaster />
      </TooltipProvider>
    </QueryClientProvider>
  );
}

export default App;
