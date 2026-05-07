import * as React from "react";
import { Link, useLocation } from "wouter";
import { cn } from "@/lib/utils";
import {
  LayoutDashboard,
  Users,
  DollarSign,
  Wallet,
  ArrowLeftRight,
  CreditCard,
  ShieldAlert,
  Bell,
  LogOut,
  Menu,
  X
} from "lucide-react";
import { motion } from "framer-motion";
import { useAuth } from "@/hooks/use-auth";

const navItems = [
  { name: "Dashboard", href: "/", icon: LayoutDashboard },
  { name: "Users & KYC", href: "/users", icon: Users, role: "ROLE_ADMIN" },
  { name: "Currencies", href: "/currencies", icon: DollarSign, role: "ROLE_ADMIN" },
  { name: "Wallets", href: "/wallets", icon: Wallet },
  { name: "Transactions", href: "/transactions", icon: ArrowLeftRight },
  { name: "Payments", href: "/payments", icon: CreditCard },
  { name: "Risk & Fraud", href: "/risk", icon: ShieldAlert, role: "ROLE_ADMIN" },
  { name: "Notifications", href: "/notifications", icon: Bell },
];

export function AppLayout({ children }: { children: React.ReactNode }) {
  const [location] = useLocation();
  const [isMobileOpen, setIsMobileOpen] = React.useState(false);
  const { user, logout, isAdmin } = useAuth();

  const closeMobile = () => setIsMobileOpen(false);

  return (
    <div className="min-h-screen bg-background flex flex-col md:flex-row">
      {/* Mobile Header */}
      <div className="md:hidden flex items-center justify-between p-4 border-b border-border bg-card/50 backdrop-blur-md sticky top-0 z-40">
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-lg bg-primary/20 flex items-center justify-center">
            <div className="w-4 h-4 bg-primary rounded-sm rotate-45" />
          </div>
          <span className="font-display font-bold text-lg">FinTrack</span>
        </div>
        <button onClick={() => setIsMobileOpen(true)} className="p-2 text-foreground">
          <Menu className="w-6 h-6" />
        </button>
      </div>

      {/* Sidebar */}
      <div className={cn(
        "fixed inset-y-0 left-0 z-50 w-72 bg-card border-r border-border transform transition-transform duration-300 ease-in-out md:relative md:translate-x-0 flex flex-col",
        isMobileOpen ? "translate-x-0" : "-translate-x-full"
      )}>
        <div className="flex items-center justify-between p-6">
          <div className="flex items-center gap-3">
            <img src={`${import.meta.env.BASE_URL}images/logo.png`} alt="Logo" className="w-10 h-10 object-contain" />
            <span className="font-display font-bold text-2xl tracking-tight bg-gradient-to-r from-white to-white/60 bg-clip-text text-transparent">FinTrack</span>
          </div>
          <button onClick={closeMobile} className="md:hidden p-2 text-muted-foreground">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="px-6 py-2">
          <p className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-4">Menu</p>
          <nav className="space-y-1">
            {navItems.filter(item => !item.role || (item.role === "ROLE_ADMIN" && isAdmin)).map((item) => {
              const isActive = location === item.href;
              return (
                <Link key={item.name} href={item.href} onClick={closeMobile}>
                  <div className={cn(
                    "flex items-center gap-3 px-4 py-3 rounded-xl cursor-pointer transition-all duration-200 group relative",
                    isActive ? "text-primary font-medium" : "text-muted-foreground hover:text-foreground hover:bg-white/5"
                  )}>
                    {isActive && (
                      <motion.div 
                        layoutId="activeTab" 
                        className="absolute inset-0 bg-primary/10 border border-primary/20 rounded-xl"
                        initial={false}
                        transition={{ type: "spring", stiffness: 400, damping: 30 }}
                      />
                    )}
                    <item.icon className={cn("w-5 h-5 relative z-10", isActive ? "text-primary" : "text-muted-foreground group-hover:text-foreground")} />
                    <span className="relative z-10">{item.name}</span>
                  </div>
                </Link>
              );
            })}
          </nav>
        </div>

        <div className="mt-auto p-6 space-y-4">
          <div className="bg-muted/50 rounded-2xl p-4 border border-white/5 flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-gradient-to-tr from-primary to-primary/50 flex items-center justify-center text-white font-bold text-sm">
              {user?.fullName.charAt(0)}
            </div>
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-foreground truncate">{user?.fullName}</p>
              <p className="text-xs text-muted-foreground truncate lowercase">{user?.role.replace('ROLE_', '')}</p>
            </div>
          </div>
          
          <button 
            onClick={logout}
            className="w-full flex items-center gap-3 px-4 py-3 rounded-xl text-muted-foreground hover:text-destructive hover:bg-destructive/10 transition-all duration-200 group"
          >
            <LogOut className="w-5 h-5 group-hover:rotate-12 transition-transform" />
            <span className="font-medium">Sign Out</span>
          </button>
        </div>
      </div>

      {/* Main Content */}
      <main className="flex-1 min-w-0 overflow-auto bg-[radial-gradient(ellipse_at_top,_var(--tw-gradient-stops))] from-primary/5 via-background to-background">
        <div className="max-w-7xl mx-auto p-4 sm:p-6 lg:p-8">
          {children}
        </div>
      </main>

      {/* Mobile Overlay */}
      {isMobileOpen && (
        <div 
          className="fixed inset-0 bg-black/60 backdrop-blur-sm z-40 md:hidden" 
          onClick={closeMobile}
        />
      )}
    </div>
  );
}
