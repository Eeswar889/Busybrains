import { useEffect, useState } from "react";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";

const emptyForm = { name: "", description: "", price: "" };

function DashboardPage() {
  const { auth } = useAuth();
  const isAdmin = auth.role === "ROLE_ADMIN";
  const [products, setProducts] = useState([]);
  const [form, setForm] = useState(emptyForm);
  const [editingId, setEditingId] = useState(null);
  const [error, setError] = useState("");

  const loadProducts = () => {
    api.get("/products").then((response) => setProducts(response.data));
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");
    try {
      const payload = { ...form, price: Number(form.price) };
      if (editingId) {
        await api.put(`/products/${editingId}`, payload);
      } else {
        await api.post("/products", payload);
      }
      setForm(emptyForm);
      setEditingId(null);
      loadProducts();
    } catch (err) {
      setError(err.response?.data?.message || "Product save failed");
    }
  };

  const handleEdit = (product) => {
    setEditingId(product.id);
    setForm({ name: product.name, description: product.description, price: product.price });
  };

  const handleDelete = async (id) => {
    await api.delete(`/products/${id}`);
    loadProducts();
  };

  return (
    <main className="page dashboard">
      <section className="hero">
        <h1>Product Dashboard</h1>
        <p>Browse products, manage inventory as admin, and test JWT + RBAC flows.</p>
      </section>

      <section className="panel">
        <h2>Products</h2>
        <div className="product-grid">
          {products.map((product) => (
            <article key={product.id} className="product-card">
              <h3>{product.name}</h3>
              <p>{product.description}</p>
              <strong>${product.price}</strong>
              {isAdmin && (
                <div className="actions">
                  <button onClick={() => handleEdit(product)}>Edit</button>
                  <button className="danger" onClick={() => handleDelete(product.id)}>Delete</button>
                </div>
              )}
            </article>
          ))}
        </div>
      </section>

      {isAdmin && (
        <section className="panel">
          <h2>{editingId ? "Update Product" : "Add Product"}</h2>
          <form onSubmit={handleSubmit}>
            <input placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            <textarea placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
            <input placeholder="Price" type="number" step="0.01" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} />
            {error && <p className="error">{error}</p>}
            <button type="submit">{editingId ? "Update" : "Create"}</button>
          </form>
        </section>
      )}
    </main>
  );
}

export default DashboardPage;
