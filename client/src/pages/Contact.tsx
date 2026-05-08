import { useState } from "react";
import { motion } from "framer-motion";
import { Card } from "@/components/ui/Card";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import { Mail, Phone, User, MessageSquare, Send } from "lucide-react";
import { toast } from "sonner";

export default function ContactPage() {
  const [form, setForm] = useState({ name: "", email: "", phone: "", message: "" });
  const [errors, setErrors] = useState({ name: "", email: "", phone: "", message: "" });
  const [isLoading, setIsLoading] = useState(false);

  const validate = () => {
    const newErrors = { name: "", email: "", phone: "", message: "" };
    let valid = true;

    if (!form.name.trim()) {
      newErrors.name = "Name is required";
      valid = false;
    }
    if (!form.email.trim()) {
      newErrors.email = "Email is required";
      valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      newErrors.email = "Enter a valid email address";
      valid = false;
    }
    if (!form.phone.trim()) {
      newErrors.phone = "Phone number is required";
      valid = false;
    } else if (!/^\d{10}$/.test(form.phone)) {
      newErrors.phone = "Phone number must be exactly 10 digits";
      valid = false;
    }
    if (!form.message.trim()) {
      newErrors.message = "Message is required";
      valid = false;
    }

    setErrors(newErrors);
    return valid;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setIsLoading(true);
    await new Promise(r => setTimeout(r, 1000));
    setIsLoading(false);
    toast.success("Message sent!", { description: "We'll get back to you shortly." });
    setForm({ name: "", email: "", phone: "", message: "" });
    setErrors({ name: "", email: "", phone: "", message: "" });
  };

  return (
    <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="max-w-2xl mx-auto space-y-6">
      <div>
        <h1 className="text-3xl font-display font-bold">Contact Us</h1>
        <p className="text-muted-foreground mt-1">Reach out and we'll get back to you as soon as possible.</p>
      </div>

      <Card className="p-6">
        <form onSubmit={handleSubmit} className="space-y-5">
          <div>
            <label className="block text-sm font-medium mb-1.5">
              <User className="w-4 h-4 inline mr-1.5" />
              Full Name
            </label>
            <Input
              placeholder="John Doe"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
            />
            {errors.name && <p className="text-xs text-danger mt-1">{errors.name}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-1.5">
              <Mail className="w-4 h-4 inline mr-1.5" />
              Email Address
            </label>
            <Input
              type="email"
              placeholder="john@example.com"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
            />
            {errors.email && <p className="text-xs text-danger mt-1">{errors.email}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-1.5">
              <Phone className="w-4 h-4 inline mr-1.5" />
              Phone Number
            </label>
            <Input
              type="tel"
              placeholder="1234567890"
              maxLength={10}
              value={form.phone}
              onChange={(e) => setForm({ ...form, phone: e.target.value.replace(/\D/g, "") })}
            />
            {errors.phone && <p className="text-xs text-danger mt-1">{errors.phone}</p>}
          </div>

          <div>
            <label className="block text-sm font-medium mb-1.5">
              <MessageSquare className="w-4 h-4 inline mr-1.5" />
              Message
            </label>
            <textarea
              placeholder="How can we help you?"
              rows={5}
              className="w-full px-4 py-3 rounded-xl border border-border bg-background text-foreground resize-none focus:ring-2 focus:ring-primary/50 outline-none"
              value={form.message}
              onChange={(e) => setForm({ ...form, message: e.target.value })}
            />
            {errors.message && <p className="text-xs text-danger mt-1">{errors.message}</p>}
          </div>

          <Button type="submit" className="w-full" isLoading={isLoading}>
            <Send className="w-4 h-4 mr-2" />
            Send Message
          </Button>
        </form>
      </Card>
    </motion.div>
  );
}
