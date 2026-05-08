// User Types
export interface CreateUserRequest {
  fullName: string;
  email: string;
  phone: string;
  country: string;
}

export interface UpdateUserRequest {
  kycStatus: string;
  accountTier: string;
}

// Wallet Types
export interface CreateWalletRequest {
  userId?: number;
  currency: string;
}

// Payment Types
export interface DepositRequest {
  walletId: number;
  amount: string;
  currency: string;
  description?: string;
}

export interface WithdrawalRequest {
  walletId: number;
  amount: string;
  currency: string;
  description?: string;
}

export interface TransferRequest {
  fromWalletId: number;
  toWalletId: number;
  amount: string;
  currency: string;
  description?: string;
}
