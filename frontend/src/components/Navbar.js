import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function Navbar() {
  const { isAuthenticated, auth, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header className="navbar">
      <Link className="brand" to="/dashboard">BusyBrains Store</Link>
      <nav className="nav-links">
        {isAuthenticated ? (
          <>
            <span className="badge">{auth.role}</span>
            <Link to="/profile">Profile</Link>
            <button className="link-button" onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <Link to="/login">Login</Link>
            <Link to="/register">Register</Link>
          </>
        )}
      </nav>
    </header>
  );
}

export default Navbar;
