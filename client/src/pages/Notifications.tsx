import * as React from "react";
import { motion } from "framer-motion";
import { Bell, AlertCircle, Info, FileText } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { useNotifications } from "@/hooks/use-notifications";
import { formatDate } from "@/lib/utils";
import type { Notification } from "@/lib/api-client";

export default function NotificationsPage() {
  const { data: notifications, isLoading } = useNotifications();

  const getIcon = (type: string) => {
    switch (type) {
      case 'RISK_ALERT': return <AlertCircle className="w-5 h-5 text-danger" />;
      case 'HIGH_VALUE_TRANSACTION': return <FileText className="w-5 h-5 text-warning" />;
      case 'KYC_UPDATE': return <Info className="w-5 h-5 text-primary" />;
      default: return <Bell className="w-5 h-5 text-muted-foreground" />;
    }
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6 max-w-4xl mx-auto">
      <div>
        <h1 className="text-3xl font-display font-bold">System Inbox</h1>
        <p className="text-muted-foreground">Automated platform alerts and updates</p>
      </div>

      <div className="space-y-3">
        {isLoading ? (
          [1,2,3,4].map(i => <Card key={i} className="h-20 animate-pulse bg-muted" />)
        ) : notifications?.length === 0 ? (
          <Card className="text-center py-12">
            <Bell className="w-12 h-12 mx-auto text-muted-foreground mb-4 opacity-50" />
            <h3 className="text-xl font-semibold mb-2">You're all caught up</h3>
            <p className="text-muted-foreground">No new notifications to display.</p>
          </Card>
        ) : (
          (notifications as Notification[])?.map(notif => (
            <Card key={notif.id} className={`p-4 flex items-start gap-4 transition-colors ${notif.isRead ? 'opacity-70 bg-background/50' : 'bg-card border-primary/20'}`}>
              <div className="w-10 h-10 rounded-full bg-background border border-white/5 flex items-center justify-center shrink-0 mt-1">
                {getIcon(notif.type)}
              </div>
              <div className="flex-1">
                <div className="flex justify-between items-start mb-1">
                  <h4 className={`font-bold ${notif.isRead ? 'text-muted-foreground' : 'text-foreground'}`}>
                    {notif.title}
                  </h4>
                  <span className="text-xs text-muted-foreground whitespace-nowrap ml-4">
                    {formatDate(notif.createdAt)}
                  </span>
                </div>
                <p className="text-sm text-muted-foreground">{notif.message}</p>
              </div>
            </Card>
          ))
        )}
      </div>
    </motion.div>
  );
}
