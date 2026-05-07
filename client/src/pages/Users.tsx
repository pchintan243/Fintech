import * as React from "react";
import { motion } from "framer-motion";
import { Search, Edit, Plus, User as UserIcon } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Badge } from "@/components/ui/Badge";
import { Modal } from "@/components/ui/Modal";
import { useUsers, useUpdateUser, useCreateUser } from "@/hooks/use-users";
import { formatDate } from "@/lib/utils";
import type { User, UpdateUserRequest, CreateUserRequest } from "@workspace/api-client-react";

export default function UsersPage() {
  const { data: users, isLoading } = useUsers();
  const [searchTerm, setSearchTerm] = React.useState("");
  
  const [selectedUser, setSelectedUser] = React.useState<User | null>(null);
  const [isEditModalOpen, setIsEditModalOpen] = React.useState(false);
  const [isCreateModalOpen, setIsCreateModalOpen] = React.useState(false);

  const filteredUsers = React.useMemo(() => {
    if (!users) return [];
    return users.filter(u => 
      u.email.toLowerCase().includes(searchTerm.toLowerCase()) || 
      u.fullName.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [users, searchTerm]);

  const getKycBadge = (status: string) => {
    switch (status) {
      case "PREMIUM": return <Badge variant="success">PREMIUM</Badge>;
      case "BASIC": return <Badge variant="info">BASIC</Badge>;
      case "PENDING": return <Badge variant="warning">PENDING</Badge>;
      case "REJECTED": return <Badge variant="danger">REJECTED</Badge>;
      default: return <Badge variant="default">UNVERIFIED</Badge>;
    }
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between gap-4">
        <div>
          <h1 className="text-3xl font-display font-bold">Users & KYC</h1>
          <p className="text-muted-foreground">Manage user identities, limits, and verifications</p>
        </div>
        <Button onClick={() => setIsCreateModalOpen(true)}>
          <Plus className="w-4 h-4 mr-2" /> Add User
        </Button>
      </div>

      <Card className="p-0 overflow-hidden">
        <div className="p-4 border-b border-border bg-card/50">
          <Input 
            icon={<Search className="w-4 h-4" />} 
            placeholder="Search by name or email..." 
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-md bg-background"
          />
        </div>
        
        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm whitespace-nowrap">
            <thead className="uppercase tracking-wider text-muted-foreground bg-muted/20 text-xs">
              <tr>
                <th className="px-6 py-4 font-medium">User</th>
                <th className="px-6 py-4 font-medium">KYC Status</th>
                <th className="px-6 py-4 font-medium">Tier</th>
                <th className="px-6 py-4 font-medium">Joined</th>
                <th className="px-6 py-4 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {isLoading ? (
                <tr><td colSpan={5} className="px-6 py-8 text-center text-muted-foreground">Loading users...</td></tr>
              ) : filteredUsers.length === 0 ? (
                <tr><td colSpan={5} className="px-6 py-8 text-center text-muted-foreground">No users found.</td></tr>
              ) : (
                filteredUsers.map(user => (
                  <tr key={user.id} className="hover:bg-white/[0.02] transition-colors">
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-full bg-primary/20 flex items-center justify-center text-primary font-bold">
                          {user.fullName.charAt(0)}
                        </div>
                        <div>
                          <p className="font-semibold text-foreground">{user.fullName}</p>
                          <p className="text-xs text-muted-foreground">{user.email}</p>
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4">{getKycBadge(user.kycStatus)}</td>
                    <td className="px-6 py-4">
                      <span className="text-xs font-medium text-muted-foreground border border-border px-2 py-1 rounded-md bg-background/50">
                        {user.accountTier}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-muted-foreground">{formatDate(user.createdAt)}</td>
                    <td className="px-6 py-4">
                      <Button variant="ghost" size="sm" onClick={() => { setSelectedUser(user); setIsEditModalOpen(true); }}>
                        <Edit className="w-4 h-4 mr-2" /> Edit
                      </Button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </Card>

      <EditUserModal 
        user={selectedUser} 
        isOpen={isEditModalOpen} 
        onClose={() => { setIsEditModalOpen(false); setSelectedUser(null); }} 
      />
      <CreateUserModal 
        isOpen={isCreateModalOpen} 
        onClose={() => setIsCreateModalOpen(false)} 
      />
    </motion.div>
  );
}

function EditUserModal({ user, isOpen, onClose }: { user: User | null, isOpen: boolean, onClose: () => void }) {
  const { mutate: updateUser, isPending } = useUpdateUser();
  const [status, setStatus] = React.useState<any>("UNVERIFIED");
  const [tier, setTier] = React.useState<any>("FREE");

  React.useEffect(() => {
    if (user) {
      setStatus(user.kycStatus);
      setTier(user.accountTier);
    }
  }, [user]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;
    updateUser({
      id: user.id,
      data: { kycStatus: status, accountTier: tier }
    }, {
      onSuccess: () => onClose()
    });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Edit User Status" description={user?.email}>
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">KYC Status</label>
          <select 
            className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="UNVERIFIED">Unverified</option>
            <option value="PENDING">Pending</option>
            <option value="BASIC">Basic</option>
            <option value="PREMIUM">Premium</option>
            <option value="REJECTED">Rejected</option>
          </select>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Account Tier</label>
          <select 
            className="w-full h-12 px-4 rounded-xl border border-border bg-background select-reset focus:ring-2 focus:ring-primary/50 outline-none"
            value={tier}
            onChange={(e) => setTier(e.target.value)}
          >
            <option value="FREE">Free</option>
            <option value="BASIC">Basic</option>
            <option value="PREMIUM">Premium</option>
          </select>
        </div>
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>Cancel</Button>
          <Button type="submit" isLoading={isPending}>Save Changes</Button>
        </div>
      </form>
    </Modal>
  );
}

function CreateUserModal({ isOpen, onClose }: { isOpen: boolean, onClose: () => void }) {
  const { mutate: createUser, isPending } = useCreateUser();
  const [data, setData] = React.useState({ fullName: "", email: "", phone: "", country: "" });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createUser(data, { onSuccess: () => { onClose(); setData({ fullName: "", email: "", phone: "", country: "" }); } });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Create New User" description="Provision a new identity profile.">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm font-medium mb-1">Full Name</label>
          <Input required value={data.fullName} onChange={(e) => setData({...data, fullName: e.target.value})} placeholder="John Doe" />
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Email</label>
          <Input required type="email" value={data.email} onChange={(e) => setData({...data, email: e.target.value})} placeholder="john@example.com" />
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Phone</label>
            <Input value={data.phone} onChange={(e) => setData({...data, phone: e.target.value})} placeholder="+1 234 567 890" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Country</label>
            <Input value={data.country} onChange={(e) => setData({...data, country: e.target.value})} placeholder="US" />
          </div>
        </div>
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>Cancel</Button>
          <Button type="submit" isLoading={isPending}>Create User</Button>
        </div>
      </form>
    </Modal>
  );
}
