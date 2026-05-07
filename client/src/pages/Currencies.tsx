import * as React from "react";
import { motion } from "framer-motion";
import { DollarSign, Plus, Edit, Trash2, Search } from "lucide-react";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Badge } from "@/components/ui/Badge";
import { Modal } from "@/components/ui/Modal";
import {
  useCurrencies,
  useCreateCurrency,
  useUpdateCurrency,
  useDeleteCurrency,
  Currency,
} from "@/hooks/use-currencies";

export default function CurrenciesPage() {
  const { data: currencies, isLoading } = useCurrencies();
  const { mutate: deleteCurrency } = useDeleteCurrency();
  const [searchTerm, setSearchTerm] = React.useState("");
  const [selectedCurrency, setSelectedCurrency] = React.useState<Currency | null>(null);
  const [isEditOpen, setIsEditOpen] = React.useState(false);
  const [isCreateOpen, setIsCreateOpen] = React.useState(false);

  const filtered = React.useMemo(() => {
    if (!currencies) return [];
    return (currencies as Currency[]).filter(
      (c) =>
        c.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
        c.name.toLowerCase().includes(searchTerm.toLowerCase())
    );
  }, [currencies, searchTerm]);

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="space-y-6">
      <div className="flex flex-col sm:flex-row justify-between gap-4">
        <div>
          <h1 className="text-3xl font-display font-bold">Currencies</h1>
          <p className="text-muted-foreground">
            Manage supported currencies and exchange rates
          </p>
        </div>
        <Button onClick={() => setIsCreateOpen(true)}>
          <Plus className="w-4 h-4 mr-2" /> Add Currency
        </Button>
      </div>

      <Card className="p-0 overflow-hidden">
        <div className="p-4 border-b border-border bg-card/50">
          <Input
            icon={<Search className="w-4 h-4" />}
            placeholder="Search by code or name..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="max-w-md bg-background"
          />
        </div>

        <div className="overflow-x-auto">
          <table className="w-full text-left text-sm whitespace-nowrap">
            <thead className="uppercase tracking-wider text-muted-foreground bg-muted/20 text-xs">
              <tr>
                <th className="px-6 py-4 font-medium">Code</th>
                <th className="px-6 py-4 font-medium">Name</th>
                <th className="px-6 py-4 font-medium">Symbol</th>
                <th className="px-6 py-4 font-medium">Decimals</th>
                <th className="px-6 py-4 font-medium">Exchange Rate</th>
                <th className="px-6 py-4 font-medium">Status</th>
                <th className="px-6 py-4 font-medium">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {isLoading ? (
                <tr>
                  <td colSpan={7} className="px-6 py-8 text-center text-muted-foreground">
                    Loading...
                  </td>
                </tr>
              ) : filtered.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-6 py-8 text-center text-muted-foreground">
                    No currencies found.
                  </td>
                </tr>
              ) : (
                filtered.map((currency) => (
                  <tr
                    key={currency.id}
                    className="hover:bg-white/[0.02] transition-colors"
                  >
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-lg bg-primary/20 flex items-center justify-center">
                          <DollarSign className="w-5 h-5 text-primary" />
                        </div>
                        <span className="font-mono font-bold">{currency.code}</span>
                      </div>
                    </td>
                    <td className="px-6 py-4 font-medium">{currency.name}</td>
                    <td className="px-6 py-4 font-mono">{currency.symbol}</td>
                    <td className="px-6 py-4 text-muted-foreground">
                      {currency.decimals}
                    </td>
                    <td className="px-6 py-4 font-mono">
                      {Number(currency.exchangeRate).toFixed(6)}
                    </td>
                    <td className="px-6 py-4">
                      <Badge variant={currency.isActive ? "success" : "danger"}>
                        {currency.isActive ? "Active" : "Inactive"}
                      </Badge>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => {
                            setSelectedCurrency(currency);
                            setIsEditOpen(true);
                          }}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          className="text-destructive hover:text-destructive"
                          onClick={() => {
                            if (confirm(`Delete ${currency.code}?`)) {
                              deleteCurrency(currency.id);
                            }
                          }}
                        >
                          <Trash2 className="w-4 h-4" />
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </Card>

      <CreateCurrencyModal
        isOpen={isCreateOpen}
        onClose={() => setIsCreateOpen(false)}
      />
      <EditCurrencyModal
        currency={selectedCurrency}
        isOpen={isEditOpen}
        onClose={() => {
          setIsEditOpen(false);
          setSelectedCurrency(null);
        }}
      />
    </motion.div>
  );
}

function CreateCurrencyModal({
  isOpen,
  onClose,
}: {
  isOpen: boolean;
  onClose: () => void;
}) {
  const { mutate: createCurrency, isPending, error } = useCreateCurrency();
  const [form, setForm] = React.useState({
    code: "",
    name: "",
    symbol: "",
    decimals: 2,
    isActive: true,
    exchangeRate: "1.000000",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    createCurrency(form, {
      onSuccess: () => {
        onClose();
        setForm({ code: "", name: "", symbol: "", decimals: 2, isActive: true, exchangeRate: "1.000000" });
      },
    });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Add Currency">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Code (ISO 4217)</label>
            <Input
              required
              maxLength={3}
              value={form.code}
              onChange={(e) => setForm({ ...form, code: e.target.value.toUpperCase() })}
              placeholder="USD"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Name</label>
            <Input
              required
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              placeholder="US Dollar"
            />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Symbol</label>
            <Input
              required
              maxLength={5}
              value={form.symbol}
              onChange={(e) => setForm({ ...form, symbol: e.target.value })}
              placeholder="$"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Decimals</label>
            <Input
              required
              type="number"
              min={0}
              max={8}
              value={form.decimals}
              onChange={(e) => setForm({ ...form, decimals: parseInt(e.target.value) || 0 })}
            />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Exchange Rate (to USD)</label>
          <Input
            required
            type="number"
            step="0.000001"
            value={form.exchangeRate}
            onChange={(e) => setForm({ ...form, exchangeRate: e.target.value })}
            placeholder="1.000000"
          />
        </div>
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            id="isActive"
            checked={form.isActive}
            onChange={(e) => setForm({ ...form, isActive: e.target.checked })}
            className="w-4 h-4"
          />
          <label htmlFor="isActive" className="text-sm">Active</label>
        </div>
        {error && (
          <p className="text-sm text-destructive">Failed to create currency.</p>
        )}
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" isLoading={isPending}>
            Create
          </Button>
        </div>
      </form>
    </Modal>
  );
}

function EditCurrencyModal({
  currency,
  isOpen,
  onClose,
}: {
  currency: Currency | null;
  isOpen: boolean;
  onClose: () => void;
}) {
  const { mutate: updateCurrency, isPending } = useUpdateCurrency();
  const [form, setForm] = React.useState({
    name: "",
    symbol: "",
    decimals: 2,
    isActive: true,
    exchangeRate: "",
  });

  React.useEffect(() => {
    if (currency) {
      setForm({
        name: currency.name,
        symbol: currency.symbol,
        decimals: currency.decimals,
        isActive: currency.isActive,
        exchangeRate: currency.exchangeRate,
      });
    }
  }, [currency]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!currency) return;
    updateCurrency({ id: currency.id, data: form }, { onSuccess: onClose });
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} title="Edit Currency">
      <form onSubmit={handleSubmit} className="space-y-4">
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Code</label>
            <Input value={currency?.code} disabled className="opacity-60" />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Name</label>
            <Input
              required
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
          </div>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Symbol</label>
            <Input
              required
              maxLength={5}
              value={form.symbol}
              onChange={(e) => setForm({ ...form, symbol: e.target.value })}
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Decimals</label>
            <Input
              required
              type="number"
              min={0}
              max={8}
              value={form.decimals}
              onChange={(e) => setForm({ ...form, decimals: parseInt(e.target.value) || 0 })}
            />
          </div>
        </div>
        <div>
          <label className="block text-sm font-medium mb-1">Exchange Rate (to USD)</label>
          <Input
            required
            type="number"
            step="0.000001"
            value={form.exchangeRate}
            onChange={(e) => setForm({ ...form, exchangeRate: e.target.value })}
          />
        </div>
        <div className="flex items-center gap-2">
          <input
            type="checkbox"
            id="editIsActive"
            checked={form.isActive}
            onChange={(e) => setForm({ ...form, isActive: e.target.checked })}
            className="w-4 h-4"
          />
          <label htmlFor="editIsActive" className="text-sm">Active</label>
        </div>
        <div className="pt-4 flex justify-end gap-2">
          <Button type="button" variant="ghost" onClick={onClose}>
            Cancel
          </Button>
          <Button type="submit" isLoading={isPending}>
            Save Changes
          </Button>
        </div>
      </form>
    </Modal>
  );
}
