import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function RegisterPage() {
  const navigate = useNavigate();
  const { register } = useAuth();
  const [form, setForm] = useState({ username: "", email: "", password: "" });
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      await register(form);
      navigate("/dashboard");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed");
    }
  };

  return (
    <main className="page auth-page">
      <section className="panel">
        <h1>Register</h1>
        <form onSubmit={handleSubmit}>
          <input placeholder="Username" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} />
          <input placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
          <input type="password" placeholder="Password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
          {error && <p className="error">{error}</p>}
          <button type="submit">Create account</button>
        </form>
        <p>Already registered? <Link to="/login">Login</Link></p>
      </section>
    </main>
  );
}

export default RegisterPage;
