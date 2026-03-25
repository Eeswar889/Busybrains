import { useEffect, useState } from "react";
import api from "../api/client";
import { useAuth } from "../context/AuthContext";

function ProfilePage() {
  const { profile, setProfile, syncAuth } = useAuth();
  const [details, setDetails] = useState({ username: "", email: "" });
  const [passwords, setPasswords] = useState({ currentPassword: "", newPassword: "" });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (profile) {
      setDetails({ username: profile.username, email: profile.email });
    }
  }, [profile]);

  const updateProfile = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    try {
      const response = await api.put("/profile", details);
      setProfile(response.data);
      if (response.data.token) {
        syncAuth({
          token: response.data.token,
          username: response.data.username,
          role: response.data.role
        });
      }
      setMessage("Profile updated");
    } catch (err) {
      setError(err.response?.data?.message || "Profile update failed");
    }
  };

  const changePassword = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");
    try {
      await api.put("/profile/change-password", passwords);
      setPasswords({ currentPassword: "", newPassword: "" });
      setMessage("Password changed");
    } catch (err) {
      setError(err.response?.data?.message || "Password change failed");
    }
  };

  return (
    <main className="page profile-page">
      <section className="panel">
        <h1>Profile</h1>
        {profile && <p>Role: {profile.role}</p>}
        <form onSubmit={updateProfile}>
          <input placeholder="Username" value={details.username} onChange={(e) => setDetails({ ...details, username: e.target.value })} />
          <input placeholder="Email" value={details.email} onChange={(e) => setDetails({ ...details, email: e.target.value })} />
          <button type="submit">Update Profile</button>
        </form>
      </section>

      <section className="panel">
        <h2>Change Password</h2>
        <form onSubmit={changePassword}>
          <input type="password" placeholder="Current password" value={passwords.currentPassword} onChange={(e) => setPasswords({ ...passwords, currentPassword: e.target.value })} />
          <input type="password" placeholder="New password" value={passwords.newPassword} onChange={(e) => setPasswords({ ...passwords, newPassword: e.target.value })} />
          <button type="submit">Change Password</button>
        </form>
        {message && <p className="success">{message}</p>}
        {error && <p className="error">{error}</p>}
      </section>
    </main>
  );
}

export default ProfilePage;
