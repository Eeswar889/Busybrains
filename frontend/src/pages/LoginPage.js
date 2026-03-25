import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function LoginPage() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [form, setForm] = useState({ username: "", password: "" });
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      await login(form);
      navigate("/dashboard");
    } catch (err) {
      setError(err.response?.data?.message || "Login failed");
    }
  };

  return (
    <main className="page auth-page">
      <section className="panel">
        <h1>Login</h1>
        <p>Use `admin / password` or `user / password`.</p>
        <form onSubmit={handleSubmit}>
          <input placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
          <input type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          {error && <p className="error">{error}</p>}
          <button type="submit">Login</button>
        </form>
        <a className="google-button" href="http://localhost:8080/oauth2/authorization/google">Continue with Google</a>
        <p>No account? <Link to="/register">Register</Link></p>
      </section>
    </main>
  );
}

export default LoginPage;
