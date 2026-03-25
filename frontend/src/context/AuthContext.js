import { createContext, useContext, useEffect, useState } from "react";
import api from "../api/client";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => ({
    token: localStorage.getItem("token"),
    username: localStorage.getItem("username"),
    role: localStorage.getItem("role")
  }));
  const [profile, setProfile] = useState(null);

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    localStorage.removeItem("role");
    setAuth({ token: null, username: null, role: null });
    setProfile(null);
  };

  const persist = (payload) => {
    localStorage.setItem("token", payload.token);
    localStorage.setItem("username", payload.username);
    localStorage.setItem("role", payload.role);
    setAuth(payload);
  };

  const syncAuth = (payload) => {
    const nextAuth = {
      token: payload.token,
      username: payload.username,
      role: payload.role
    };
    localStorage.setItem("token", nextAuth.token);
    localStorage.setItem("username", nextAuth.username);
    localStorage.setItem("role", nextAuth.role);
    setAuth(nextAuth);
  };

  const login = async (credentials) => {
    const response = await api.post("/auth/login", credentials);
    persist(response.data);
    return response.data;
  };

  const register = async (payload) => {
    const response = await api.post("/auth/register", payload);
    persist(response.data);
    return response.data;
  };

  const completeOauthLogin = async (token) => {
    localStorage.setItem("token", token);
    const response = await api.get("/profile", {
      headers: { Authorization: `Bearer ${token}` }
    });
    const nextAuth = {
      token,
      username: response.data.username,
      role: response.data.role
    };
    syncAuth(nextAuth);
    setProfile(response.data);
  };

  useEffect(() => {
    if (!auth.token) {
      return;
    }
    api.get("/profile")
      .then((response) => setProfile(response.data))
      .catch(() => logout());
  }, [auth.token]);

  return (
    <AuthContext.Provider value={{
      auth,
      profile,
      setProfile,
      login,
      register,
      logout,
      syncAuth,
      completeOauthLogin,
      isAuthenticated: Boolean(auth.token)
    }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
