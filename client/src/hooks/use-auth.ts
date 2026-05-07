import { useState, useCallback } from "react";
import { navigate } from "wouter/use-browser-location";

export type UserRole = "ROLE_USER" | "ROLE_ADMIN";

export interface User {
  id: number;
  email: string;
  fullName: string;
  role: UserRole;
}

const TOKEN_KEY = "fintrack_token";
const USER_KEY = "fintrack_user";

export function useAuth() {
  const [user, setUser] = useState<User | null>(() => {
    const savedUser = localStorage.getItem(USER_KEY);
    return savedUser ? JSON.parse(savedUser) : null;
  });

  const [token, setToken] = useState<string | null>(() => {
    return localStorage.getItem(TOKEN_KEY);
  });

  const login = useCallback((newToken: string, newUser: User) => {
    setToken(newToken);
    setUser(newUser);
    localStorage.setItem(TOKEN_KEY, newToken);
    localStorage.setItem(USER_KEY, JSON.stringify(newUser));
  }, []);

  const logout = useCallback(() => {
    setToken(null);
    setUser(null);
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    navigate("/login");
  }, []);

  const isAuthenticated = !!token;
  const isAdmin = user?.role === "ROLE_ADMIN";

  return {
    user,
    token,
    login,
    logout,
    isAuthenticated,
    isAdmin,
  };
}
