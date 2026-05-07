import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import { format } from "date-fns";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatCurrency(amount: number, currency: string = "USD") {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: currency,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount);
}

export function formatDate(dateString: string) {
  try {
    return format(new Date(dateString), "MMM dd, yyyy HH:mm");
  } catch (e) {
    return dateString;
  }
}

export function formatNumber(num: number) {
  return new Intl.NumberFormat("en-US").format(num);
}
